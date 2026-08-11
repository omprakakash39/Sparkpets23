package com.yourplugin.pets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PetAbilities implements Listener {

    private final PetsPlugin plugin;
    public static final HashMap<UUID, String> activePetTypes = new HashMap<>();
    public static final HashMap<UUID, String> activePetRarities = new HashMap<>();
    public static final HashMap<UUID, ItemStack> activePetItems = new HashMap<>();
    private final Random random = new Random();

    public PetAbilities(PetsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NamespacedKey typeKey = new NamespacedKey(plugin, "active_pet_type");
        NamespacedKey rarityKey = new NamespacedKey(plugin, "active_pet_rarity");

        if (player.getPersistentDataContainer().has(typeKey, PersistentDataType.STRING)) {
            String type = player.getPersistentDataContainer().get(typeKey, PersistentDataType.STRING);
            String rarity = player.getPersistentDataContainer().get(rarityKey, PersistentDataType.STRING);
            
            activePetTypes.put(player.getUniqueId(), type);
            activePetRarities.put(player.getUniqueId(), rarity);
            activePetItems.put(player.getUniqueId(), PetGui.createPetHead(type, rarity));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        // Save active pet state to player data container so it persists across relogs
        NamespacedKey typeKey = new NamespacedKey(plugin, "active_pet_type");
        NamespacedKey rarityKey = new NamespacedKey(plugin, "active_pet_rarity");

        if (activePetTypes.containsKey(uuid)) {
            player.getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, activePetTypes.get(uuid));
            player.getPersistentDataContainer().set(rarityKey, PersistentDataType.STRING, activePetRarities.get(uuid));
        } else {
            player.getPersistentDataContainer().remove(typeKey);
            player.getPersistentDataContainer().remove(rarityKey);
        }

        activePetTypes.remove(uuid);
        activePetRarities.remove(uuid);
        activePetItems.remove(uuid);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        Player player = (Player) event.getWhoClicked();

        if (title.contains("Pets")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

            if (itemName.contains("Next Page")) {
                if (title.contains("(1/3)")) PetGui.openPetsMenu(player, 2);
                else if (title.contains("(2/3)")) PetGui.openPetsMenu(player, 3);
            } else if (itemName.contains("Previous Page")) {
                if (title.contains("(3/3)")) PetGui.openPetsMenu(player, 2);
                else if (title.contains("(2/3)")) PetGui.openPetsMenu(player, 1);
            } else if (itemName.contains("Pet Fusion")) {
                PetGui.openFusionMenu(player);
            } else if (itemName.contains("Pet Egg")) {
                player.getInventory().addItem(event.getCurrentItem().clone());
                player.sendMessage(ChatColor.GREEN + "Claimed pet egg!");
            }
        } else if (title.contains("Pet Fusion")) {
            int slot = event.getRawSlot();
            if (slot >= 36) return;

            if (slot >= 10 && slot <= 16) return;

            if (slot == 31) {
                event.setCancelled(true);
                processFusion(player, event.getInventory());
                return;
            }
            event.setCancelled(true);
        }
    }

    private void processFusion(Player player, Inventory inv) {
        List<ItemStack> placedPets = new ArrayList<>();

        for (int slot = 10; slot <= 16; slot++) {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType() == Material.PLAYER_HEAD && item.hasItemMeta()) {
                placedPets.add(item);
            }
        }

        if (placedPets.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Please place pets in the fusion slots first!");
            return;
        }

        String firstType = null;
        String firstRarity = null;

        for (ItemStack pet : placedPets) {
            String type = getPetTypeFromItem(pet);
            String rarity = getPetRarityFromItem(pet);

            if (type == null || rarity == null) {
                returnItemsToPlayer(player, placedPets, inv);
                player.sendMessage(ChatColor.RED + "Fusion Failed: Invalid items detected!");
                return;
            }

            if (firstType == null) {
                firstType = type;
                firstRarity = rarity;
            } else {
                if (!firstType.equalsIgnoreCase(type) || !firstRarity.equalsIgnoreCase(rarity)) {
                    returnItemsToPlayer(player, placedPets, inv);
                    player.sendMessage(ChatColor.RED + "Fusion Failed: All pets must be of the exact same type and rarity!");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }
            }
        }

        int count = placedPets.size();
        String targetRarity;

        if (firstRarity.equalsIgnoreCase("Common") && count == 4) {
            targetRarity = "Rare";
        } else if (firstRarity.equalsIgnoreCase("Rare") && count == 3) {
            targetRarity = "Epic";
        } else if (firstRarity.equalsIgnoreCase("Epic") && count == 2) {
            targetRarity = "Shiny";
        } else {
            returnItemsToPlayer(player, placedPets, inv);
            player.sendMessage(ChatColor.RED + "Fusion Failed! Incorrect amount of pets for " + firstRarity + " tier.");
            player.sendMessage(ChatColor.YELLOW + "Required: 4 Common->Rare, 3 Rare->Epic, 2 Epic->Shiny.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        for (int slot = 10; slot <= 16; slot++) {
            inv.setItem(slot, null);
        }

        ItemStack upgradedPet = PetGui.createPetHead(firstType, targetRarity);
        player.getInventory().addItem(upgradedPet);
        
        player.closeInventory();
        player.sendMessage(ChatColor.GREEN + "§lSUCCESS! §aSuccessfully fused pets into a " + targetRarity + " " + firstType + " Pet!");
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
    }

    private void returnItemsToPlayer(Player player, List<ItemStack> pets, Inventory inv) {
        for (int slot = 10; slot <= 16; slot++) {
            inv.setItem(slot, null);
        }
        for (ItemStack pet : pets) {
            player.getInventory().addItem(pet);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains("Pet Fusion")) {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();
            
            for (int slot = 10; slot <= 16; slot++) {
                ItemStack item = inv.getItem(slot);
                if (item != null && item.getType() != Material.AIR) {
                    player.getInventory().addItem(item);
                    inv.setItem(slot, null);
                }
            }
        }
    }

    private String getPetTypeFromItem(ItemStack item) {
        if (!item.hasItemMeta()) return null;
        NamespacedKey key = new NamespacedKey(plugin, "pet_type");
        if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }
        // Fallback parsing from display name if PDC is missing
        String stripped = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        if (stripped != null && stripped.endsWith(" Pet")) {
            return stripped.replace(" Pet", "").trim();
        }
        return null;
    }

    private String getPetRarityFromItem(ItemStack item) {
        if (!item.hasItemMeta()) return null;
        NamespacedKey key = new NamespacedKey(plugin, "pet_rarity");
        if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }
        // Fallback parsing from lore
        if (item.getItemMeta().hasLore()) {
            for (String line : item.getItemMeta().getLore()) {
                String stripped = ChatColor.stripColor(line);
                if (stripped != null && stripped.startsWith("Rarity: ")) {
                    return stripped.replace("Rarity: ", "").trim();
                }
            }
        }
        return "Common";
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack clickedItem = event.getItem();
            if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                String name = clickedItem.getItemMeta().getDisplayName();
                Player player = event.getPlayer();

                if (name.contains("Pet Egg")) {
                    event.setCancelled(true);
                    clickedItem.setAmount(clickedItem.getAmount() - 1);

                    String rarity = "Common";
                    if (name.contains("Rare")) rarity = "Rare";
                    else if (name.contains("Epic")) rarity = "Epic";
                    else if (name.contains("Shiny")) rarity = "Shiny";

                    String[] petTypes = {"Wolf", "Golem", "Villager", "Witch", "Dragon", "Blaze", "Enderman", "Zombie", "Totem", "Guardian", "Banker", "Skeleton"};
                    String randomType = petTypes[random.nextInt(petTypes.length)];

                    ItemStack petHead = PetGui.createPetHead(randomType, rarity);
                    player.getInventory().addItem(petHead);

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    player.sendMessage(ChatColor.GREEN + "You hatched a " + rarity + " " + randomType + " Pet!");
                    return;
                }

                if (clickedItem.getType() == Material.PLAYER_HEAD && getPetTypeFromItem(clickedItem) != null) {
                    event.setCancelled(true);

                    if (activePetItems.containsKey(player.getUniqueId())) {
                        player.getInventory().addItem(activePetItems.get(player.getUniqueId()));
                    }

                    String type = getPetTypeFromItem(clickedItem);
                    String rarity = getPetRarityFromItem(clickedItem);

                    activePetTypes.put(player.getUniqueId(), type);
                    activePetRarities.put(player.getUniqueId(), rarity);
                    activePetItems.put(player.getUniqueId(), clickedItem.clone());
                    
                    clickedItem.setAmount(clickedItem.getAmount() - 1);
                    
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                    player.sendMessage(ChatColor.GREEN + "Successfully activated pet: " + clickedItem.getItemMeta().getDisplayName());
                }
            }
        }
    }

    public static void deactivatePet(Player player) {
        UUID uuid = player.getUniqueId();
        if (activePetTypes.containsKey(uuid)) {
            ItemStack petItem = activePetItems.get(uuid);
            if (petItem != null) {
                player.getInventory().addItem(petItem);
            }
            activePetTypes.remove(uuid);
            activePetRarities.remove(uuid);
            activePetItems.remove(uuid);
            
            // Clear persistent data
            NamespacedKey typeKey = new NamespacedKey(PetsPlugin.getInstance(), "active_pet_type");
            NamespacedKey rarityKey = new NamespacedKey(PetsPlugin.getInstance(), "active_pet_rarity");
            player.getPersistentDataContainer().remove(typeKey);
            player.getPersistentDataContainer().remove(rarityKey);

            player.sendMessage(ChatColor.RED + "Deactivated your active pet and returned it to your inventory.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You don't have any active pet equipped.");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker) {
            String type = activePetTypes.get(attacker.getUniqueId());
            String rarity = activePetRarities.get(attacker.getUniqueId());
            
            if (type != null && rarity != null) {
                double percentage = PetGui.getAbilityValue(rarity) / 100.0;
                double multiplier = 1.0 + percentage;

                if (type.equalsIgnoreCase("Wolf") || type.equalsIgnoreCase("Dragon") || type.equalsIgnoreCase("Skeleton") || type.equalsIgnoreCase("Blaze")) {
                    event.setDamage(event.getDamage() * multiplier);
                }
            }
        }

        if (event.getEntity() instanceof Player victim) {
            String type = activePetTypes.get(victim.getUniqueId());
            String rarity = activePetRarities.get(victim.getUniqueId());

            if (type != null && rarity != null) {
                double percentage = PetGui.getAbilityValue(rarity) / 100.0;
                double reductionMultiplier = 1.0 - percentage;

                if (type.equalsIgnoreCase("Golem") || type.equalsIgnoreCase("Enderman") || type.equalsIgnoreCase("Guardian")) {
                    event.setDamage(event.getDamage() * reductionMultiplier);
                }
            }
        }
    }
    }

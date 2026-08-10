package com.yourplugin.pets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PetAbilities implements Listener {

    private final PetsPlugin plugin;
    public static final HashMap<UUID, String> activePets = new HashMap<>();
    public static final HashMap<UUID, ItemStack> activePetItems = new HashMap<>();
    private final Random random = new Random();

    public PetAbilities(PetsPlugin plugin) {
        this.plugin = plugin;
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
            
            // If clicking inside the player's own inventory while fusion menu is open
            if (slot >= 36) {
                return; // Allow normal inventory interaction below
            }

            // Allowed ingredient slots: 10 to 16
            if (slot >= 10 && slot <= 16) {
                return; // Allow placing/taking pets in input slots
            }

            // Confirm Fusion button slot (31)
            if (slot == 31) {
                event.setCancelled(true);
                processFusion(player, event.getInventory());
                return;
            }

            // Block everything else in the GUI (borders, panes, etc.)
            event.setCancelled(true);
        }
    }

    private void processFusion(Player player, Inventory inv) {
        List<ItemStack> placedPets = new ArrayList<>();

        // Collect all valid pet heads placed in slots 10 to 16
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

        // Validate that all placed pets are of the EXACT same type and rarity
        String firstType = null;
        String firstRarity = null;

        for (ItemStack pet : placedPets) {
            String name = pet.getItemMeta().getDisplayName();
            String type = getPetTypeFromDisplay(name);
            String rarity = getPetRarityFromLore(pet);

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
        String targetRarity = null;

        // Fusion Rules check:
        // 4 Common -> 1 Rare
        // 3 Rare -> 1 Epic
        // 2 Epic -> 1 Shiny
        if (firstRarity.equalsIgnoreCase("Common") && count == 4) {
            targetRarity = "Rare";
        } else if (firstRarity.equalsIgnoreCase("Rare") && count == 3) {
            targetRarity = "Epic";
        } else if (firstRarity.equalsIgnoreCase("Epic") && count == 2) {
            targetRarity = "Shiny";
        } else {
            // Fusion failed due to wrong count/requirement mismatch
            returnItemsToPlayer(player, placedPets, inv);
            player.sendMessage(ChatColor.RED + "Fusion Failed! Incorrect amount of pets for " + firstRarity + " tier.");
            player.sendMessage(ChatColor.YELLOW + "Required: 4 Common->Rare, 3 Rare->Epic, 2 Epic->Shiny.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        // Clear ingredients from inventory slots so they aren't duplicated
        for (int slot = 10; slot <= 16; slot++) {
            inv.setItem(slot, null);
        }

        // Success: Give upgraded pet
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
            
            // Return any remaining items in fusion slots back to player when they close GUI
            for (int slot = 10; slot <= 16; slot++) {
                ItemStack item = inv.getItem(slot);
                if (item != null && item.getType() != Material.AIR) {
                    player.getInventory().addItem(item);
                    inv.setItem(slot, null);
                }
            }
        }
    }

    private String getPetTypeFromDisplay(String displayName) {
        String stripped = ChatColor.stripColor(displayName);
        if (stripped != null && stripped.endsWith(" Pet")) {
            return stripped.replace(" Pet", "").trim();
        }
        return null;
    }

    private String getPetRarityFromLore(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return null;
        for (String line : item.getItemMeta().getLore()) {
            String stripped = ChatColor.stripColor(line);
            if (stripped != null && stripped.startsWith("Rarity: ")) {
                return stripped.replace("Rarity: ", "").trim();
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

                if (clickedItem.getType() == Material.PLAYER_HEAD) {
                    event.setCancelled(true);

                    if (activePetItems.containsKey(player.getUniqueId())) {
                        player.getInventory().addItem(activePetItems.get(player.getUniqueId()));
                    }

                    activePets.put(player.getUniqueId(), name);
                    activePetItems.put(player.getUniqueId(), clickedItem.clone());
                    
                    clickedItem.setAmount(clickedItem.getAmount() - 1);
                    
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                    player.sendMessage(ChatColor.GREEN + "Successfully activated pet: " + name);
                }
            }
        }
    }

    public static void deactivatePet(Player player) {
        if (activePets.containsKey(player.getUniqueId())) {
            ItemStack petItem = activePetItems.get(player.getUniqueId());
            if (petItem != null) {
                player.getInventory().addItem(petItem);
            }
            activePets.remove(player.getUniqueId());
            activePetItems.remove(player.getUniqueId());
            player.sendMessage(ChatColor.RED + "Deactivated your active pet and returned it to your inventory.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "You don't have any active pet equipped.");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker) {
            String pet = activePets.get(attacker.getUniqueId());
            if (pet != null) {
                double multiplier = 1.05;
                if (pet.contains("Shiny")) multiplier = 1.20;
                else if (pet.contains("Epic")) multiplier = 1.15;
                else if (pet.contains("Rare")) multiplier = 1.10;

                if (pet.contains("Wolf") || pet.contains("Dragon") || pet.contains("Skeleton") || pet.contains("Blaze")) {
                    event.setDamage(event.getDamage() * multiplier);
                }
            }
        }

        if (event.getEntity() instanceof Player victim) {
            String pet = activePets.get(victim.getUniqueId());
            if (pet != null) {
                double reductionMultiplier = 0.95;
                if (pet.contains("Shiny")) reductionMultiplier = 0.80;
                else if (pet.contains("Epic")) reductionMultiplier = 0.85;
                else if (pet.contains("Rare")) reductionMultiplier = 0.90;

                if (pet.contains("Golem") || pet.contains("Enderman") || pet.contains("Guardian")) {
                    event.setDamage(event.getDamage() * reductionMultiplier);
                }
            }
        }
    }
    }

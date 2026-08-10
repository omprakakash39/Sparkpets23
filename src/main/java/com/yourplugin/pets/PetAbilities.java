package com.yourplugin.pets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PetAbilities implements Listener {

    private final PetsPlugin plugin;
    public static final HashMap<UUID, String> activePets = new HashMap<>();
    public static final HashMap<UUID, ItemStack> activePetItems = new HashMap<>();

    public PetAbilities(PetsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title.contains("Pets")) {
            if (event.getRawSlot() < 54) {
                event.setCancelled(true);
                if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

                Player player = (Player) event.getWhoClicked();
                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

                if (itemName.contains("Next Page")) {
                    if (title.contains("(1/3)")) PetGui.openPetsMenu(player, 2);
                    else if (title.contains("(2/3)")) PetGui.openPetsMenu(player, 3);
                } else if (itemName.contains("Previous Page")) {
                    if (title.contains("(3/3)")) PetGui.openPetsMenu(player, 2);
                    else if (title.contains("(2/3)")) PetGui.openPetsMenu(player, 1);
                } else if (itemName.contains("Pet Fusion")) {
                    PetGui.openFusionMenu(player);
                } else if (event.getCurrentItem().getType().name().contains("SPAWN_EGG")) {
                    player.getInventory().addItem(event.getCurrentItem());
                    player.sendMessage(ChatColor.GREEN + "Claimed pet egg to your inventory!");
                }
            }
        } else if (title.contains("Pet Fusion")) {
            int slot = event.getRawSlot();
            // Restrict interaction to slots 11, 12, 14, 15 and confirm button at 22 inside the GUI container (0-26)
            if (slot < 27) {
                if (slot != 11 && slot != 12 && slot != 14 && slot != 15 && slot != 22) {
                    event.setCancelled(true);
                } else if (slot == 22) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Fusion completed successfully!");
                    event.getWhoClicked().closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String name = item.getItemMeta().getDisplayName();
                if (name.contains("Pet")) {
                    Player player = event.getPlayer();
                    
                    // Return previous pet to inventory if active
                    if (activePetItems.containsKey(player.getUniqueId())) {
                        player.getInventory().addItem(activePetItems.get(player.getUniqueId()));
                    }

                    activePets.put(player.getUniqueId(), name);
                    activePetItems.put(player.getUniqueId(), item.clone());
                    
                    item.setAmount(item.getAmount() - 1);
                    
                    // Play Beacon Activation Sound
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                    
                    player.sendMessage(ChatColor.GREEN + "Successfully activated pet: " + name);
                    event.setCancelled(true);
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
            if (pet != null && pet.contains("Attack Boost")) {
                if (pet.contains("Shiny")) event.setDamage(event.getDamage() * 1.20);
                else if (pet.contains("Epic")) event.setDamage(event.getDamage() * 1.15);
                else if (pet.contains("Rare")) event.setDamage(event.getDamage() * 1.10);
                else event.setDamage(event.getDamage() * 1.05);
            }
        }

        if (event.getEntity() instanceof Player victim) {
            String pet = activePets.get(victim.getUniqueId());
            if (pet != null && pet.contains("Damage Reduction")) {
                if (pet.contains("Shiny")) event.setDamage(event.getDamage() * 0.80);
                else if (pet.contains("Epic")) event.setDamage(event.getDamage() * 0.85);
                else if (pet.contains("Rare")) event.setDamage(event.getDamage() * 0.90);
                else event.setDamage(event.getDamage() * 0.95);
            }
        }
    }
}

package com.yourplugin.pets;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.UUID;

public class PetAbilities implements Listener {

    private final PetsPlugin plugin;
    public static final HashMap<UUID, String> activePets = new HashMap<>();

    public PetAbilities(PetsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title.contains("Pets Menu")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            Player player = (Player) event.getWhoClicked();
            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

            int currentPage = 1;
            if (title.contains("(2/3)")) currentPage = 2;
            else if (title.contains("(3/3)")) currentPage = 3;

            if (itemName.contains("Next Page")) {
                if (currentPage == 1) PetGui.openPetsMenu(player, 2);
                else if (currentPage == 2) PetGui.openPetsMenu(player, 3);
            } else if (itemName.contains("Previous Page")) {
                if (currentPage == 3) PetGui.openPetsMenu(player, 2);
                else if (currentPage == 2) PetGui.openPetsMenu(player, 1);
            } else if (itemName.contains("Pet Fusion")) {
                player.sendMessage(ChatColor.YELLOW + "Place matching pets together to fuse them!");
            } else if (itemName.contains("Pet")) {
                activePets.put(player.getUniqueId(), itemName);
                player.sendMessage(ChatColor.GREEN + "Equipped: " + itemName);
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker) {
            String pet = activePets.get(attacker.getUniqueId());
            if (pet != null && pet.contains("Wolf")) {
                if (pet.contains("SHINY")) event.setDamage(event.getDamage() * 1.20);
                else if (pet.contains("Epic")) event.setDamage(event.getDamage() * 1.15);
                else if (pet.contains("Rare")) event.setDamage(event.getDamage() * 1.10);
                else event.setDamage(event.getDamage() * 1.05);
            }
        }

        if (event.getEntity() instanceof Player victim) {
            String pet = activePets.get(victim.getUniqueId());
            if (pet != null && pet.contains("Golem")) {
                if (pet.contains("SHINY")) event.setDamage(event.getDamage() * 0.84);
                else if (pet.contains("Epic")) event.setDamage(event.getDamage() * 0.88);
                else if (pet.contains("Rare")) event.setDamage(event.getDamage() * 0.92);
                else event.setDamage(event.getDamage() * 0.96);
            }
        }
    }
}

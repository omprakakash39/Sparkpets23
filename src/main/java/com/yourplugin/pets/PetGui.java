package com.yourplugin.pets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PetGui {

    public static void openPetsMenu(Player p, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "(" + page + "/3) Pets Menu");

        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta paneMeta = pane.getItemMeta();
        if (paneMeta != null) {
            paneMeta.setDisplayName(" ");
            pane.setItemMeta(paneMeta);
        }
        
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, pane);
        }

        ItemStack prev = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prev.getItemMeta();
        if (prevMeta != null) {
            prevMeta.setDisplayName(ChatColor.GRAY + "◀ Previous Page");
            prev.setItemMeta(prevMeta);
        }
        inv.setItem(0, prev);

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        if (nextMeta != null) {
            nextMeta.setDisplayName(ChatColor.GREEN + "Next Page ▶");
            next.setItemMeta(nextMeta);
        }
        inv.setItem(8, next);

        ItemStack fusion = new ItemStack(Material.ANVIL);
        ItemMeta fusionMeta = fusion.getItemMeta();
        if (fusionMeta != null) {
            fusionMeta.setDisplayName(ChatColor.YELLOW + "Pet Fusion");
            fusionMeta.setLore(List.of(ChatColor.GRAY + "Combine matching pets:", ChatColor.YELLOW + "• 4 Common ➔ Rare", ChatColor.YELLOW + "• 3 Rare ➔ Epic", ChatColor.YELLOW + "• 2 Epic ➔ Shiny"));
            fusion.setItemMeta(fusionMeta);
        }
        inv.setItem(4, fusion);

        if (page == 1) {
            inv.setItem(11, createPetHead("&cWolf Pet &7[Common]", "Attack Boost: +5%"));
            inv.setItem(12, createPetHead("&cWolf Pet &9[Rare]", "Attack Boost: +10%"));
            inv.setItem(14, createPetHead("&cWolf Pet &5[Epic]", "Attack Boost: +15%"));
            inv.setItem(15, createPetHead("&cWolf Pet &6[SHINY]", "Attack Boost: +20%!"));

            inv.setItem(20, createPetHead("&bGolem Pet &7[Common]", "Damage Reduction: 4%"));
            inv.setItem(21, createPetHead("&bGolem Pet &9[Rare]", "Damage Reduction: 8%"));
            inv.setItem(23, createPetHead("&bGolem Pet &5[Epic]", "Damage Reduction: 12%"));
            inv.setItem(24, createPetHead("&bGolem Pet &6[SHINY]", "Damage Reduction: 16%!"));

            inv.setItem(29, createPetHead("&aVillager Pet &7[Common]", "Hero of the Village II"));
            inv.setItem(30, createPetHead("&aVillager Pet &9[Rare]", "Hero of the Village IV"));
            inv.setItem(32, createPetHead("&aVillager Pet &5[Epic]", "Hero of the Village VI"));
            inv.setItem(33, createPetHead("&aVillager Pet &6[SHINY]", "Hero of the Village VIII"));

            inv.setItem(38, createPetHead("&dWitch Pet &7[Common]", "Potion duration +10%"));
            inv.setItem(39, createPetHead("&dWitch Pet &9[Rare]", "Potion duration +20%"));
            inv.setItem(41, createPetHead("&dWitch Pet &5[Epic]", "Potion duration +30%"));
            inv.setItem(42, createPetHead("&dWitch Pet &6[SHINY]", "Potion duration +40%"));

        } else if (page == 2) {
            inv.setItem(11, createPetHead("&6Dragon Pet &7[Common]", "Crit Chance +5%"));
            inv.setItem(12, createPetHead("&6Dragon Pet &9[Rare]", "Crit Chance +10%"));
            inv.setItem(14, createPetHead("&6Dragon Pet &5[Epic]", "Crit Chance +15%"));
            inv.setItem(15, createPetHead("&6Dragon Pet &6[SHINY]", "Crit Chance +25%!"));

            inv.setItem(20, createPetHead("&eBlaze Pet &7[Common]", "Fire Defense +10%"));
            inv.setItem(21, createPetHead("&eBlaze Pet &9[Rare]", "Fire Defense +20%"));
            inv.setItem(23, createPetHead("&eBlaze Pet &5[Epic]", "Fire Defense +30%"));
            inv.setItem(24, createPetHead("&eBlaze Pet &6[SHINY]", "Full Fire Immunity"));

            inv.setItem(29, createPetHead("&5Enderman Pet &7[Common]", "Speed Boost +5%"));
            inv.setItem(30, createPetHead("&5Enderman Pet &9[Rare]", "Speed Boost +10%"));
            inv.setItem(32, createPetHead("&5Enderman Pet &5[Epic]", "Speed Boost +15%"));
            inv.setItem(33, createPetHead("&5Enderman Pet &6[SHINY]", "Speed Boost +25%!"));

            inv.setItem(38, createPetHead("&2Zombie Pet &7[Common]", "Life Drain 2%"));
            inv.setItem(39, createPetHead("&2Zombie Pet &9[Rare]", "Life Drain 4%"));
            inv.setItem(41, createPetHead("&2Zombie Pet &5[Epic]", "Life Drain 6%"));
            inv.setItem(42, createPetHead("&2Zombie Pet &6[SHINY]", "Life Drain 10%!"));

        } else if (page == 3) {
            inv.setItem(11, createPetHead("&eTotem Pet &7[Common]", "Second Life 10%"));
            inv.setItem(12, createPetHead("&eTotem Pet &9[Rare]", "Second Life 20%"));
            inv.setItem(14, createPetHead("&eTotem Pet &5[Epic]", "Second Life 30%"));
            inv.setItem(15, createPetHead("&eTotem Pet &6[SHINY]", "Second Life 40%!"));

            inv.setItem(20, createPetHead("&bGuardian Pet &7[Common]", "XP Boost +10%"));
            inv.setItem(21, createPetHead("&bGuardian Pet &9[Rare]", "XP Boost +20%"));
            inv.setItem(23, createPetHead("&bGuardian Pet &5[Epic]", "XP Boost +30%"));
            inv.setItem(24, createPetHead("&bGuardian Pet &6[SHINY]", "XP Boost +40%!"));

            inv.setItem(29, createPetHead("&aBanker Pet &7[Common]", "Sell Multiplier +5%"));
            inv.setItem(30, createPetHead("&aBanker Pet &9[Rare]", "Sell Multiplier +10%"));
            inv.setItem(32, createPetHead("&aBanker Pet &5[Epic]", "Sell Multiplier +15%"));
            inv.setItem(33, createPetHead("&aBanker Pet &6[SHINY]", "Sell Multiplier +20%!"));

            inv.setItem(38, createPetHead("&7Skeleton Pet &7[Common]", "Projectile Damage +10%"));
            inv.setItem(39, createPetHead("&7Skeleton Pet &9[Rare]", "Projectile Damage +20%"));
            inv.setItem(41, createPetHead("&7Skeleton Pet &5[Epic]", "Projectile Damage +30%"));
            inv.setItem(42, createPetHead("&7Skeleton Pet &6[SHINY]", "Projectile Damage +40%!"));
        }

        p.openInventory(inv);
    }

    private static ItemStack createPetHead(String name, String ability) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            meta.setLore(List.of(ChatColor.GRAY + "Ability:", ChatColor.YELLOW + ability));
            item.setItemMeta(meta);
        }
        return item;
    }
}

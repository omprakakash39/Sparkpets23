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
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "(" + page + "/3) Pets");

        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta paneMeta = pane.getItemMeta();
        if (paneMeta != null) {
            paneMeta.setDisplayName(" ");
            pane.setItemMeta(paneMeta);
        }
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, pane);
        }

        // Top Row (Slots 1, 3, 5, 7): Allay Spawn Eggs for claiming
        inv.setItem(1, createSpawnEgg("Common"));
        inv.setItem(3, createSpawnEgg("Rare"));
        inv.setItem(5, createSpawnEgg("Epic"));
        inv.setItem(7, createSpawnEgg("Shiny"));

        inv.setItem(45, createGuiItem(Material.ARROW, ChatColor.GRAY + "Previous Page"));
        inv.setItem(49, createGuiItem(Material.ANVIL, ChatColor.YELLOW + "Pet Fusion"));
        inv.setItem(53, createGuiItem(Material.ARROW, ChatColor.GREEN + "Next Page"));

        if (page == 1) {
            inv.setItem(11, createPetHead("Wolf", "Common"));
            inv.setItem(12, createPetHead("Wolf", "Rare"));
            inv.setItem(14, createPetHead("Wolf", "Epic"));
            inv.setItem(15, createPetHead("Wolf", "Shiny"));

            inv.setItem(20, createPetHead("Golem", "Common"));
            inv.setItem(21, createPetHead("Golem", "Rare"));
            inv.setItem(23, createPetHead("Golem", "Epic"));
            inv.setItem(24, createPetHead("Golem", "Shiny"));

            inv.setItem(29, createPetHead("Villager", "Common"));
            inv.setItem(30, createPetHead("Villager", "Rare"));
            inv.setItem(32, createPetHead("Villager", "Epic"));
            inv.setItem(33, createPetHead("Villager", "Shiny"));

            inv.setItem(38, createPetHead("Witch", "Common"));
            inv.setItem(39, createPetHead("Witch", "Rare"));
            inv.setItem(41, createPetHead("Witch", "Epic"));
            inv.setItem(42, createPetHead("Witch", "Shiny"));
        } else if (page == 2) {
            inv.setItem(11, createPetHead("Dragon", "Common"));
            inv.setItem(12, createPetHead("Dragon", "Rare"));
            inv.setItem(14, createPetHead("Dragon", "Epic"));
            inv.setItem(15, createPetHead("Dragon", "Shiny"));

            inv.setItem(20, createPetHead("Blaze", "Common"));
            inv.setItem(21, createPetHead("Blaze", "Rare"));
            inv.setItem(23, createPetHead("Blaze", "Epic"));
            inv.setItem(24, createPetHead("Blaze", "Shiny"));

            inv.setItem(29, createPetHead("Enderman", "Common"));
            inv.setItem(30, createPetHead("Enderman", "Rare"));
            inv.setItem(32, createPetHead("Enderman", "Epic"));
            inv.setItem(33, createPetHead("Enderman", "Shiny"));

            inv.setItem(38, createPetHead("Zombie", "Common"));
            inv.setItem(39, createPetHead("Zombie", "Rare"));
            inv.setItem(41, createPetHead("Zombie", "Epic"));
            inv.setItem(42, createPetHead("Zombie", "Shiny"));
        } else if (page == 3) {
            inv.setItem(11, createPetHead("Totem", "Common"));
            inv.setItem(12, createPetHead("Totem", "Rare"));
            inv.setItem(14, createPetHead("Totem", "Epic"));
            inv.setItem(15, createPetHead("Totem", "Shiny"));

            inv.setItem(20, createPetHead("Guardian", "Common"));
            inv.setItem(21, createPetHead("Guardian", "Rare"));
            inv.setItem(23, createPetHead("Guardian", "Epic"));
            inv.setItem(24, createPetHead("Guardian", "Shiny"));

            inv.setItem(29, createPetHead("Banker", "Common"));
            inv.setItem(30, createPetHead("Banker", "Rare"));
            inv.setItem(32, createPetHead("Banker", "Epic"));
            inv.setItem(33, createPetHead("Banker", "Shiny"));

            inv.setItem(38, createPetHead("Skeleton", "Common"));
            inv.setItem(39, createPetHead("Skeleton", "Rare"));
            inv.setItem(41, createPetHead("Skeleton", "Epic"));
            inv.setItem(42, createPetHead("Skeleton", "Shiny"));
        }

        p.openInventory(inv);
    }

    public static void openFusionMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_AQUA + "Pet Fusion");
        
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        if(meta != null) {
            meta.setDisplayName(" ");
            pane.setItemMeta(meta);
        }
        for(int i=0; i<27; i++) inv.setItem(i, pane);

        inv.setItem(11, null);
        inv.setItem(12, null);
        inv.setItem(14, null);
        inv.setItem(15, null);
        inv.setItem(22, createGuiItem(Material.EMERALD_BLOCK, ChatColor.GREEN + "Confirm Fusion"));

        p.openInventory(inv);
    }

    public static ItemStack createSpawnEgg(String rarity) {
        ItemStack item = new ItemStack(Material.ALLAY_SPAWN_EGG);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + rarity + " Pet Egg");
            meta.setLore(List.of(
                ChatColor.GRAY + "Right click to hatch a " + rarity + " pet!"
            ));
            meta.setMaxStackSize(1);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createPetHead(String type, String rarity) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + type + " Pet");
            
            int percentage = getAbilityPercentage(rarity);
            String abilityName = getAbilityName(type);
            String abilityDesc = getAbilityDescription(type, percentage);

            meta.setLore(List.of(
                ChatColor.DARK_AQUA + "Rarity: " + getRarityColor(rarity) + rarity,
                "",
                ChatColor.LIGHT_PURPLE + abilityName,
                ChatColor.GRAY + abilityDesc
            ));
            
            meta.setMaxStackSize(1);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static String getAbilityName(String type) {
        return switch (type.toLowerCase()) {
            case "wolf" -> "Attack Boost";
            case "golem" -> "Damage Reduction";
            case "villager" -> "Trade Master";
            case "witch" -> "Alchemist";
            case "dragon" -> "Fierce Wrath";
            case "blaze" -> "Flame Aura";
            case "enderman" -> "Teleport Reflex";
            case "zombie" -> "Undead Resilience";
            case "totem" -> "Undying Grace";
            case "guardian" -> "Laser Thorns";
            case "banker" -> "Coin Master";
            case "skeleton" -> "Archery Focus";
            default -> "Special Perk";
        };
    }

    private static String getAbilityDescription(String type, int percentage) {
        return switch (type.toLowerCase()) {
            case "wolf" -> "Increases your attack damage by " + percentage + "%.";
            case "golem" -> "Decreases your damage taken by " + percentage + "%.";
            case "villager" -> "Reduces trade costs by " + percentage + "%.";
            case "witch" -> "Potion effects last " + percentage + "% longer.";
            case "dragon" -> "Deals " + percentage + "% extra critical damage.";
            case "blaze" -> "Ignites enemies for " + percentage + " extra damage ticks.";
            case "enderman" -> "Grants " + percentage + "% chance to dodge attacks.";
            case "zombie" -> "Regenerates health " + percentage + "% faster.";
            case "totem" -> "Gives " + percentage + "% chance to cheat death.";
            case "guardian" -> "Reflects " + percentage + "% damage back to attackers.";
            case "banker" -> "Earns " + percentage + "% more gold/money drops.";
            case "skeleton" -> "Increases bow damage by " + percentage + "%.";
            default -> "Grants special ability power.";
        };
    }

    private static int getAbilityPercentage(String rarity) {
        return switch (rarity.toLowerCase()) {
            case "rare" -> 10;
            case "epic" -> 15;
            case "shiny" -> 20;
            default -> 5;
        };
    }

    private static ChatColor getRarityColor(String rarity) {
        return switch (rarity.toLowerCase()) {
            case "rare" -> ChatColor.BLUE;
            case "epic" -> ChatColor.DARK_PURPLE;
            case "shiny" -> ChatColor.AQUA;
            default -> ChatColor.GRAY;
        };
    }

    private static ItemStack createGuiItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
                }
                

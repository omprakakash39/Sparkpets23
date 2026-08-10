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

        inv.setItem(0, createGuiItem(Material.ARROW, ChatColor.GRAY + "Previous Page"));
        inv.setItem(4, createGuiItem(Material.ANVIL, ChatColor.YELLOW + "Pet Fusion (Click to Open)"));
        inv.setItem(8, createGuiItem(Material.ARROW, ChatColor.GREEN + "Next Page"));

        if (page == 1) {
            inv.setItem(11, createCustomPetItem("Wolf", "Common"));
            inv.setItem(12, createCustomPetItem("Wolf", "Rare"));
            inv.setItem(14, createCustomPetItem("Wolf", "Epic"));
            inv.setItem(15, createCustomPetItem("Wolf", "Shiny"));

            inv.setItem(20, createCustomPetItem("Golem", "Common"));
            inv.setItem(21, createCustomPetItem("Golem", "Rare"));
            inv.setItem(23, createCustomPetItem("Golem", "Epic"));
            inv.setItem(24, createCustomPetItem("Golem", "Shiny"));

            inv.setItem(29, createCustomPetItem("Villager", "Common"));
            inv.setItem(30, createCustomPetItem("Villager", "Rare"));
            inv.setItem(32, createCustomPetItem("Villager", "Epic"));
            inv.setItem(33, createCustomPetItem("Villager", "Shiny"));

            inv.setItem(38, createCustomPetItem("Witch", "Common"));
            inv.setItem(39, createCustomPetItem("Witch", "Rare"));
            inv.setItem(41, createCustomPetItem("Witch", "Epic"));
            inv.setItem(42, createCustomPetItem("Witch", "Shiny"));
        } else if (page == 2) {
            inv.setItem(11, createCustomPetItem("Dragon", "Common"));
            inv.setItem(12, createCustomPetItem("Dragon", "Rare"));
            inv.setItem(14, createCustomPetItem("Dragon", "Epic"));
            inv.setItem(15, createCustomPetItem("Dragon", "Shiny"));

            inv.setItem(20, createCustomPetItem("Blaze", "Common"));
            inv.setItem(21, createCustomPetItem("Blaze", "Rare"));
            inv.setItem(23, createCustomPetItem("Blaze", "Epic"));
            inv.setItem(24, createCustomPetItem("Blaze", "Shiny"));

            inv.setItem(29, createCustomPetItem("Enderman", "Common"));
            inv.setItem(30, createCustomPetItem("Enderman", "Rare"));
            inv.setItem(32, createCustomPetItem("Enderman", "Epic"));
            inv.setItem(33, createCustomPetItem("Enderman", "Shiny"));

            inv.setItem(38, createCustomPetItem("Zombie", "Common"));
            inv.setItem(39, createCustomPetItem("Zombie", "Rare"));
            inv.setItem(41, createCustomPetItem("Zombie", "Epic"));
            inv.setItem(42, createCustomPetItem("Zombie", "Shiny"));
        } else if (page == 3) {
            inv.setItem(11, createCustomPetItem("Totem", "Common"));
            inv.setItem(12, createCustomPetItem("Totem", "Rare"));
            inv.setItem(14, createCustomPetItem("Totem", "Epic"));
            inv.setItem(15, createCustomPetItem("Totem", "Shiny"));

            inv.setItem(20, createCustomPetItem("Guardian", "Common"));
            inv.setItem(21, createCustomPetItem("Guardian", "Rare"));
            inv.setItem(23, createCustomPetItem("Guardian", "Epic"));
            inv.setItem(24, createCustomPetItem("Guardian", "Shiny"));

            inv.setItem(29, createCustomPetItem("Banker", "Common"));
            inv.setItem(30, createCustomPetItem("Banker", "Rare"));
            inv.setItem(32, createCustomPetItem("Banker", "Epic"));
            inv.setItem(33, createCustomPetItem("Banker", "Shiny"));

            inv.setItem(38, createCustomPetItem("Skeleton", "Common"));
            inv.setItem(39, createCustomPetItem("Skeleton", "Rare"));
            inv.setItem(41, createCustomPetItem("Skeleton", "Epic"));
            inv.setItem(42, createCustomPetItem("Skeleton", "Shiny"));
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

    public static ItemStack createCustomPetItem(String type, String rarity) {
        // Using Allay Spawn Egg as the Pet Egg base
        ItemStack item = new ItemStack(Material.ALLAY_SPAWN_EGG);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Setting name based on ability without bold or emojis
            String baseName = switch (type.toLowerCase()) {
                case "wolf" -> "Attack Boost Pet";
                case "golem" -> "Damage Reduction Pet";
                case "villager" -> "Trade Master Pet";
                case "witch" -> "Alchemist Pet";
                default -> type + " Pet";
            };

            meta.setDisplayName(ChatColor.WHITE + baseName);
            
            String abilityName = "Ability";
            String abilityDesc = "Special Perk";
            int percentage = getAbilityPercentage(rarity);

            if (type.equalsIgnoreCase("Wolf")) {
                abilityName = "Attack Boost";
                abilityDesc = "Increases attack damage by " + percentage + "%!";
            } else if (type.equalsIgnoreCase("Golem")) {
                abilityName = "Damage Reduction";
                abilityDesc = "Decreases damage taken by " + percentage + "%!";
            } else if (type.equalsIgnoreCase("Villager")) {
                abilityName = "Trade Master";
                abilityDesc = "Reduces trade costs by " + percentage + "%!";
            } else if (type.equalsIgnoreCase("Witch")) {
                abilityName = "Alchemist";
                abilityDesc = "Potion duration increased by " + percentage + "%!";
            }

            meta.setLore(List.of(
                ChatColor.DARK_AQUA + "Rarity: " + getRarityColor(rarity) + rarity,
                "",
                ChatColor.LIGHT_PURPLE + abilityName,
                ChatColor.GRAY + abilityDesc
            ));
            
            // Prevent stacking
            meta.setMaxStackSize(1);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static int getAbilityPercentage(String rarity) {
        return switch (rarity.toLowerCase()) {
            case "rare" -> 10;
            case "epic" -> 15;
            case "shiny" -> 20;
            default -> 5; // Common
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

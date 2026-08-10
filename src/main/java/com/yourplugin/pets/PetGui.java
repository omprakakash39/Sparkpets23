package com.yourplugin.pets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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
        // Updated Fusion GUI size to 36 slots so ingredients and confirm button have clear spaces
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.DARK_AQUA + "Pet Fusion");
        
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        if(meta != null) {
            meta.setDisplayName(" ");
            pane.setItemMeta(meta);
        }
        for(int i = 0; i < 36; i++) {
            inv.setItem(i, pane);
        }

        // Allowed input slots for fusion (Slots 10, 11, 12, 13, 14, 15, 16 - Middle rows)
        // Let's set standard input slots: 10, 11, 12, 13, 14, 15, 16 as empty/editable slots
        for (int slot = 10; slot <= 16; slot++) {
            inv.setItem(slot, null);
        }

        // Confirm Button at slot 31
        ItemStack confirmBtn = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirmBtn.getItemMeta();
        if (confirmMeta != null) {
            confirmMeta.setDisplayName(ChatColor.GREEN + "§lConfirm Fusion");
            confirmMeta.setLore(List.of(
                ChatColor.GRAY + "Place required pets in slots above",
                ChatColor.GRAY + "4 Common -> 1 Rare",
                ChatColor.GRAY + "3 Rare -> 1 Epic",
                ChatColor.GRAY + "2 Epic -> 1 Shiny"
            ));
            confirmBtn.setItemMeta(confirmMeta);
        }
        inv.setItem(31, confirmBtn);

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
            String formattedName = getPetFormattedName(type, rarity);
            meta.setDisplayName(formattedName);
            
            double baseValue = getAbilityValue(rarity);
            List<String> lore = new ArrayList<>();
            
            lore.add("§7Rarity: " + getRarityColor(rarity) + rarity);
            lore.add("");

            switch (type.toLowerCase()) {
                case "wolf" -> {
                    lore.add("§cAttack Boost");
                    lore.add("§fIncreases attack damage");
                    lore.add("§c" + String.format("%.2f", baseValue) + "% §ffurther when");
                    lore.add("§fattacking enemies");
                }
                case "golem" -> {
                    lore.add("§bDamage Reduction");
                    lore.add("§fDecreases damage taken");
                    lore.add("§b" + String.format("%.2f", baseValue) + "% §ffrom all incoming");
                    lore.add("§fattacks");
                }
                case "villager" -> {
                    lore.add("§aTrade Master");
                    lore.add("§fReduces trade costs");
                    lore.add("§a" + String.format("%.2f", baseValue) + "% §fwhen dealing");
                    lore.add("§fwith villagers");
                }
                case "witch" -> {
                    lore.add("§5Alchemist");
                    lore.add("§fPotion effects last");
                    lore.add("§b" + String.format("%.2f", baseValue) + "% §flonger when");
                    lore.add("§fconsumed");
                }
                case "dragon" -> {
                    lore.add("§6Fierce Wrath");
                    lore.add("§fDeals critical hit");
                    lore.add("§e" + String.format("%.2f", baseValue) + "% §fextra damage");
                    lore.add("§fto opponents");
                }
                case "blaze" -> {
                    lore.add("§6Flame Aura");
                    lore.add("§fBurns your targets");
                    lore.add("§c" + String.format("%.2f", baseValue) + "% §flonger with");
                    lore.add("§ffire damage ticks");
                }
                case "enderman" -> {
                    lore.add("§dTeleport Reflex");
                    lore.add("§fGrants chance to");
                    lore.add("§d" + String.format("%.2f", baseValue) + "% §fdodge incoming");
                    lore.add("§f enemy attacks");
                }
                case "zombie" -> {
                    lore.add("§2Undead Resilience");
                    lore.add("§fRegenerates your HP");
                    lore.add("§a" + String.format("%.2f", baseValue) + "% §ffaster during");
                    lore.add("§fcombat situations");
                }
                case "totem" -> {
                    lore.add("§eUndying Grace");
                    lore.add("§fGives chance to");
                    lore.add("§e" + String.format("%.2f", baseValue) + "% §fsurvive fatal");
                    lore.add("§fdamage instances");
                }
                case "guardian" -> {
                    lore.add("§bLaser Thorns");
                    lore.add("§fReflects back");
                    lore.add("§b" + String.format("%.2f", baseValue) + "% §fdamage to");
                    lore.add("§fyour attackers");
                }
                case "banker" -> {
                    lore.add("§6Coin Master");
                    lore.add("§fIncreases earnings");
                    lore.add("§e" + String.format("%.2f", baseValue) + "% §ffrom mob drops");
                    lore.add("§fand resources");
                }
                case "skeleton" -> {
                    lore.add("§fArchery Focus");
                    lore.add("§fBoosts bow damage");
                    lore.add("§7" + String.format("%.2f", baseValue) + "% §ffurther against");
                    lore.add("§ftargeted foes");
                }
                default -> {
                    lore.add("§7Special Perk");
                    lore.add("§fBoosts stats by");
                    lore.add("§b" + String.format("%.2f", baseValue) + "% §for overall");
                    lore.add("§feffectiveness");
                }
            }

            lore.add("");
            lore.add("§8§l» §7" + rarity + " Pet");

            meta.setLore(lore);
            meta.setMaxStackSize(1);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static String getPetFormattedName(String type, String rarity) {
        String colorCode = switch (type.toLowerCase()) {
            case "wolf" -> "§c";       // Red
            case "golem" -> "§b";      // Aqua
            case "villager" -> "§a";   // Light Green
            case "witch" -> "§5";      // Purple
            case "dragon" -> "§6";     // Gold
            case "blaze" -> "§e";      // Yellow
            case "enderman" -> "§d";   // Light Purple
            case "zombie" -> "§2";     // Dark Green
            case "totem" -> "§e";      // Yellow
            case "guardian" -> "§3";   // Dark Aqua
            case "banker" -> "§6";     // Gold
            case "skeleton" -> "§f";   // White
            default -> "§7";
        };
        return colorCode + type + " Pet";
    }

    private static double getAbilityValue(String rarity) {
        return switch (rarity.toLowerCase()) {
            case "rare" -> 10.00;
            case "epic" -> 15.00;
            case "shiny" -> 20.00;
            default -> 5.00;
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

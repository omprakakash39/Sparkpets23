package com.yourplugin.pets;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        for (int slot = 10; slot <= 16; slot++) {
            inv.setItem(slot, null);
        }

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
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            String formattedName = getPetFormattedName(type, rarity);
            meta.setDisplayName(formattedName);
            
            // Apply Custom Texture Base64 based on Pet Type
            setSkullTexture(meta, getTextureUrl(type));

            NamespacedKey typeKey = new NamespacedKey(PetsPlugin.getInstance(), "pet_type");
            NamespacedKey rarityKey = new NamespacedKey(PetsPlugin.getInstance(), "pet_rarity");
            meta.getPersistentDataContainer().set(typeKey, PersistentDataType.STRING, type);
            meta.getPersistentDataContainer().set(rarityKey, PersistentDataType.STRING, rarity);

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
            item.setItemMeta(meta);
        }
        return item;
    }

    private static void setSkullTexture(SkullMeta skullMeta, String base64Texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", base64Texture));
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTextureUrl(String type) {
        return switch (type.toLowerCase()) {
            case "wolf" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTgzNzg1YTE3MmFjMTA2YTI2YzNmMWVkZmY1NzgxMzY4NWI2MjcxNTllNzkzY2NhMzkzYjJhN2ViMzVlOCJ9fX0=";
            case "golem" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTRiMGRlMzg3ZDhjOWQ0MWUxOWU1M2Q0NmY2MTczODU4OTI1ZDllZGRmMjQ0OGUzZmZiYWU5ZTI1YTNmZjM2In19fQ==";
            case "villager" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmN2NhNDU3ZDlmNDQ0NTk3ZTdkYzNmYTUxZmY5ZWQ5NzcwYjJiMGQ2MzNmNTc1YjljZjM2MTQ2ODNkNDkyMiJ9fX0=";
            case "witch" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM1MTU2MmVjMjgyNTg1NmMxODg0YWNkZDhlNjQyMWVjMmQyNzU0NTY2MzA3YjhlMTdhOGQ0M2FkNmY0NWMifX19";
            case "dragon" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjI3MzhlOGI5Nzc1ZmNhMWU4ZDlhNTdmMTAxYTQ3OTY4OWViZDE2OTE3ZTM3Y2JhZjk3NWVlZjgyYmEwNjliMiJ9fX0=";
            case "blaze" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjg0ODMzMzljYTczMWY0ZGMxZWI0NDdjZTA5ZjgzOTU4Y2Y0Yzg5MWQyZThjZWVhOTdjOGU4NDU5MmQyZSJ9fX0=";
            case "enderman" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNkYmYyYjJhOWY4NzU3YWUzZThkZGM1NDY5OGM2MTQyODhiYWNkMjg0YzJmMjU5NjEwMTRkMzFiMjc4YSJ9fX0=";
            case "zombie" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZkMmU2ZGU4N2E3MmM5ZDRlMTgyOTliNDczNzJmNGNmOWJmNGJiNGU0NDNjMjNmZjEzODQ3NjUzZjY3ZiJ9fX0=";
            case "totem" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTllMWQzYjU5OWM4NjM3ZmFhMGEwNmU0Nzk5ZTE1MmE3ZThkNTQ2M2Y3MGNlOGJiMzA2ZjUxNTRlODhlY2EifX19";
            case "guardian" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ1YzRhYzQzOTU3MTU0OTQzMjgzNWYyNzM4ZjFlM2MxYTNmODQzMzhkYjlmODQyOTdjZTMzMWVkNzY3YjUifX19";
            case "banker" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGM0NzJjNzEzY2E4YTQ3YjU2YThkZjg2NGE4ODk1M2FmYjhlMzEwODhkN2U4ODliMmI4YWU3MTBiYmVmMDY3In19fQ==";
            case "skeleton" -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2FiYjg0ZGU0MTZkZWRlZDg3Yzk0YzA2YmY3N2Q1ZjE1NTk2YTc2MmFiMWQ5MTdmMGMyMjQxMmJkZjViZjgifX19";
            default -> "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTllMWQzYjU5OWM4NjM3ZmFhMGEwNmU0Nzk5ZTE1MmE3ZThkNTQ2M2Y3MGNlOGJiMzA2ZjUxNTRlODhlY2EifX19";
        };
    }

    public static String getPetFormattedName(String type, String rarity) {
        String colorCode = switch (type.toLowerCase()) {
            case "wolf" -> "§c";
            case "golem" -> "§b";
            case "villager" -> "§a";
            case "witch" -> "§5";
            case "dragon" -> "§6";
            case "blaze" -> "§e";
            case "enderman" -> "§d";
            case "zombie" -> "§2";
            case "totem" -> "§e";
            case "guardian" -> "§3";
            case "banker" -> "§6";
            case "skeleton" -> "§f";
            default -> "§7";
        };
        return colorCode + type + " Pet";
    }

    public static double getAbilityValue(String rarity) {
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

package com.yourplugin.pets;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class PetsPlugin extends JavaPlugin {

    private static PetsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PetAbilities(this), this);
        getLogger().info("PetsPlugin has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PetsPlugin has been disabled.");
    }

    public static PetsPlugin getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("pets") || command.getName().equalsIgnoreCase("pet")) {
            if (sender instanceof Player player) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("give") && args.length >= 3) {
                        String petType = args[1];
                        String rarity = args[2];
                        player.getInventory().addItem(PetGui.createPetHead(petType, rarity));
                        player.sendMessage("§aGiven " + rarity + " " + petType + " Pet!");
                        return true;
                    } else if (args[0].equalsIgnoreCase("deactivate")) {
                        PetAbilities.deactivatePet(player);
                        return true;
                    }
                }
                PetGui.openPetsMenu(player, 1);
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return true;
        }
        return false;
    }
}

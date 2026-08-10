package com.yourplugin.pets;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class PetsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PetAbilities(this), this);
        getLogger().info("PetsPlugin has been enabled successfully for 1.21.3!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PetsPlugin has been disabled.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("pets")) {
            if (sender instanceof Player player) {
                PetGui.openPetsMenu(player, 1);
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return true;
        }
        return false;
    }
}

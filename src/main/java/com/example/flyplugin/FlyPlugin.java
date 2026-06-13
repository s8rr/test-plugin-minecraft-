package com.example.flyplugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the /fly command
        FlyCommand flyCommand = new FlyCommand(this);
        getCommand("fly").setExecutor(flyCommand);
        getCommand("fly").setTabCompleter(flyCommand);

        // Register event listeners
        getServer().getPluginManager().registerEvents(new FlyListener(this), this);

        // Handle reloads: restore allowFlight for all currently online players with permission
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("flyplugin.fly")
                    && player.getGameMode() != GameMode.CREATIVE
                    && player.getGameMode() != GameMode.SPECTATOR) {
                player.setAllowFlight(true);
            }
        }

        getLogger().info("FlyPlugin enabled! Players can now /fly or double-jump to fly.");
    }

    @Override
    public void onDisable() {
        // Clean up: land all non-creative/spectator players when plugin shuts down
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.CREATIVE
                    && player.getGameMode() != GameMode.SPECTATOR) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }
        }
        getLogger().info("FlyPlugin disabled.");
    }
}

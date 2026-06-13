package com.example.flyplugin;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements CommandExecutor, TabCompleter {

    private final FlyPlugin plugin;

    public FlyCommand(FlyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Only players can fly
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("flyplugin.fly")) {
            player.sendMessage(ChatColor.RED + "✗ You don't have permission to fly.");
            return true;
        }

        // Creative/Spectator already handle flight natively
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage(ChatColor.YELLOW + "⚠ Your game mode already controls your flight.");
            return true;
        }

        // /fly toggle — same behavior as /fly (both toggle flight)
        // args[0] = "toggle" is just an alias, we ignore it and always toggle
        toggleFly(player);
        return true;
    }

    /**
     * Toggles flight on or off for the given player.
     * Called both from the command and from FlyListener.
     */
    public void toggleFly(Player player) {
        if (player.isFlying()) {
            // Currently flying → land the player
            player.setFlying(false);
            // Keep allowFlight=true so they can double-jump to re-enable at any time
            player.sendMessage(ChatColor.RED + "✈ Flight " + ChatColor.BOLD + "disabled"
                    + ChatColor.RED + "! Double-jump or /fly to enable again.");
        } else {
            // Not flying → launch them into the air
            player.setAllowFlight(true); // Ensure this is set (safety net)
            player.setFlying(true);
            player.sendMessage(ChatColor.GREEN + "✈ Flight " + ChatColor.BOLD + "enabled"
                    + ChatColor.GREEN + "! Double-jump or /fly to toggle.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("toggle");
        }
        return completions;
    }
}

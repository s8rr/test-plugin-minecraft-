package com.tarara77.simplefly;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(ChatColor.GOLD + "Flight mode " + ChatColor.RED + "disabled" + ChatColor.GOLD + ".");
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(ChatColor.GOLD + "Flight mode " + ChatColor.GREEN + "enabled" + ChatColor.GOLD + ".");
        }

        return true;
    }
}

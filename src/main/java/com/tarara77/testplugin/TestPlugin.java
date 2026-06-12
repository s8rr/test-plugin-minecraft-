package com.tarara77.testplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("TestPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TestPlugin has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hello")) {
            
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.GREEN + "Hello, " + player.getName() + "! The plugin is working!");
            } else {
                sender.sendMessage("Hello, Console! The plugin is working!");
            }
            
            return true;
        }
        
        return false; 
    }
}

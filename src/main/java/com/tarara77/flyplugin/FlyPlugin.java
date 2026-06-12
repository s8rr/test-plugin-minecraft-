package com.tarara77.flyplugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FlyPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        if (this.getCommand("fly") != null) {
            this.getCommand("fly").setExecutor(this);
        }
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("FlyPlugin has been enabled successfully for version 26.1.2!");
    }

    @Override
    public void onDisable() {
        getLogger().info("FlyPlugin has been disabled.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(miniMessage.deserialize("<red>Only players can execute this command.</red>"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("flyplugin.use")) {
            player.sendMessage(miniMessage.deserialize("<red>You do not have permission to use this command.</red>"));
            return true;
        }

        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("toggle"))) {
            toggleFlight(player);
            return true;
        }

        player.sendMessage(miniMessage.deserialize("<red>Usage: /fly or /fly toggle</red>"));
        return true;
    }

    private void toggleFlight(Player player) {
        if (!player.getAllowFlight()) {
            player.setAllowFlight(true);
        }

        if (player.isFlying()) {
            player.setFlying(false);
            player.sendMessage(miniMessage.deserialize("<gold>Flight mode <red>disabled</red>.</gold>"));
        } else {
            player.setFlying(true);
            player.sendMessage(miniMessage.deserialize("<gold>Flight mode <green>enabled</green>.</gold>"));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("flyplugin.use")) {
            // Allows the client to double-tap space to engage or disengage flight
            player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Player player = (Player) event.getEntity();
            if (player.hasPermission("flyplugin.use")) {
                // Prevents fall damage when dropping out of flight mode
                event.setCancelled(true);
            }
        }
    }
}

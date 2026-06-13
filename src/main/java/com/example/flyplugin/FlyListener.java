package com.example.flyplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class FlyListener implements Listener {

    private final FlyPlugin plugin;

    public FlyListener(FlyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Core double-jump handler.
     *
     * How Minecraft works:
     *  - With allowFlight=true, pressing jump twice fires PlayerToggleFlightEvent.
     *  - event.isFlying()=true  → player double-jumped to START flying.
     *  - event.isFlying()=false → player double-jumped / toggled to STOP flying.
     *
     * We let the server apply the actual flight state change (don't cancel the event)
     * and just send feedback messages + enforce permissions.
     */
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        // Never interfere with creative or spectator flight
        if (player.getGameMode() == GameMode.CREATIVE
                || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        // If they somehow lack permission, cancel and silently disallow
        if (!player.hasPermission("flyplugin.fly")) {
            event.setCancelled(true);
            player.setAllowFlight(false);
            return;
        }

        // Player has permission — let flight toggle through and send a message
        if (event.isFlying()) {
            // Double-jumped from ground → about to start flying
            player.sendMessage(ChatColor.GREEN + "✈ Flight " + ChatColor.BOLD + "enabled"
                    + ChatColor.GREEN + "! Double-jump or /fly to toggle.");
        } else {
            // Toggled off while mid-air → about to land
            player.sendMessage(ChatColor.RED + "✈ Flight " + ChatColor.BOLD + "disabled"
                    + ChatColor.RED + "! Double-jump or /fly to enable again.");
        }
        // Do NOT cancel — the server applies the state change for us.
    }

    /**
     * When a player joins, give them allowFlight=true if they have the permission.
     * This enables the double-jump detection without them needing to run /fly first.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("flyplugin.fly")
                && player.getGameMode() != GameMode.CREATIVE
                && player.getGameMode() != GameMode.SPECTATOR) {
            player.setAllowFlight(true);
        }
    }

    /**
     * When a player leaves, land them and remove allowFlight.
     * (The server usually resets this anyway, but this is explicit cleanup.)
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE
                && player.getGameMode() != GameMode.SPECTATOR) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    /**
     * Handle game mode switches so allowFlight stays correct.
     * e.g. switching out of Creative should restore/remove allowFlight properly.
     * We use a 1-tick delay because the game mode hasn't applied yet when this event fires.
     */
    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) return;

            GameMode current = player.getGameMode();
            if (current == GameMode.CREATIVE || current == GameMode.SPECTATOR) {
                // Creative/Spectator manage their own flight — nothing to do
                return;
            }

            if (player.hasPermission("flyplugin.fly")) {
                // Restore allowFlight so double-jump still works
                player.setAllowFlight(true);
                player.setFlying(false); // Land them; they can re-enable with double-jump or /fly
            } else {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }, 1L);
    }
}

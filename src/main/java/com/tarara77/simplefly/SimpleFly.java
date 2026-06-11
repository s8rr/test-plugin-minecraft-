package com.tarara77.simplefly;

import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleFly extends JavaPlugin {

    @Override
    public void onEnable() {
        if (this.getCommand("fly") != null) {
            this.getCommand("fly").setExecutor(new FlyCommand());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

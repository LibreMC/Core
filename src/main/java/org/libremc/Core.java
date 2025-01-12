package org.libremc;

import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("LibreMC Core: plugin started");
        this.getCommand("msg").setExecutor(new Message());
        this.getCommand("r").setExecutor(new Reply());
        getServer().getPluginManager().registerEvents(new JoinLeave(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("LibreMC Core: Cya");
    }
}

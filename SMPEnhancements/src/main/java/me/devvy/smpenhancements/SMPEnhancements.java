package me.devvy.smpenhancements;

import me.devvy.smpenhancements.listeners.DeathDropProtector;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMPEnhancements extends JavaPlugin {

    private static SMPEnhancements INSTANCE;

    public static SMPEnhancements getInstance() {
        return INSTANCE;
    }

    private DeathDropProtector deathDropProtector;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        deathDropProtector = new DeathDropProtector();
        getServer().getPluginManager().registerEvents(deathDropProtector, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

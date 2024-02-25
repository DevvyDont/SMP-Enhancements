package me.devvy.deathdrops;

import me.devvy.deathdrops.listeners.DeathDropListener;
import org.bukkit.event.Listener;

import org.bukkit.plugin.java.JavaPlugin;

public final class DeathDrops extends JavaPlugin implements Listener {

    private static DeathDrops INSTANCE;

    public static DeathDrops getInstance() {
        return INSTANCE;
    }

    private DeathDropListener deathDropProtector;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.deathDropProtector = new DeathDropListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

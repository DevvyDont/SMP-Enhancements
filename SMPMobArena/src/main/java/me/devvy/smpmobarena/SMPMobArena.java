package me.devvy.smpmobarena;

import me.devvy.smpmobarena.arena.MobArena;
import me.devvy.smpmobarena.commands.MobArenaCommand;
import me.devvy.smpmobarena.config.ConfigManager;
import me.devvy.smpmobarena.player.ArenaPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMPMobArena extends JavaPlugin {

    private static SMPMobArena INSTANCE;

    public static SMPMobArena getInstance() {
        return INSTANCE;
    }
    private MobArena arena;

    public MobArena getArena() {
        return arena;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        // Setup commands
        MobArenaCommand mobArenaCommand = new MobArenaCommand();
        getCommand("mobarena").setExecutor(mobArenaCommand);
        getCommand("mobarena").setTabCompleter(mobArenaCommand);

        // First make sure a location is set
        if (ConfigManager.getArenaLocation() == null) {
            Bukkit.getLogger().severe("No arena location set! Please set one with /mobarena setlocation");
            Bukkit.getLogger().severe("You can then reload the plugin to make it work!");
            return;
        }

        // Now that we know a location is set we can proceed with enabling the rest of the plugin

        // Setup managers
        arena = new MobArena(ConfigManager.getArenaLocation());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if (arena != null)
            arena.cleanup();



    }
}

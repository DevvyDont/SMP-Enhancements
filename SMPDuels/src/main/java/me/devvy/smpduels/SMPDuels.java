package me.devvy.smpduels;

import me.devvy.smpduels.commands.DuelCommand;
import me.devvy.smpduels.commands.DuelLocationCommand;
import me.devvy.smpduels.commands.ForceDuelCommand;
import me.devvy.smpduels.duels.DuelManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMPDuels extends JavaPlugin {

    private static SMPDuels INSTANCE;

    public static final String CONFIG_DUEL_ARENA_LOCATION = "event-hub-location";

    private DuelManager duelManager;

    public static SMPDuels getInstance() {
        return INSTANCE;
    }

    public DuelManager getDuelManager() {
        return duelManager;
    }

    public World getMainWorld() {
        return Bukkit.getWorlds().get(0);
    }

    public Location getDuelArenaLocation() {
        Location loc = getConfig().getLocation(CONFIG_DUEL_ARENA_LOCATION);

        if (loc == null || loc.equals(getMainWorld().getSpawnLocation().zero()))
            return null;

        return loc;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        // managers
        duelManager = new DuelManager();

        // commands
        getCommand("forceduel").setExecutor(new ForceDuelCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("duellocation").setExecutor(new DuelLocationCommand());

        // Setup the config
        FileConfiguration cfg = getConfig();
        cfg.addDefault(CONFIG_DUEL_ARENA_LOCATION, getMainWorld().getSpawnLocation().zero());
        cfg.options().copyDefaults(true);
        saveConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (duelManager.ongoingDuel())
            duelManager.getCurrentDuel().stop();

    }
}

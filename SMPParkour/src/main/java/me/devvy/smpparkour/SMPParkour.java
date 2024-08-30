package me.devvy.smpparkour;

import me.devvy.smpparkour.config.ConfigManager;
import me.devvy.smpparkour.items.ItemManager;
import me.devvy.smpparkour.listeners.ParkourCompletedListener;
import me.devvy.smpparkour.map.MapManager;
import me.devvy.smpparkour.player.PlayerManager;
import me.devvy.smpparkour.util.Announcer;
import me.devvy.smpparkour.util.MinecraftScoreboardUtil;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMPParkour extends JavaPlugin {


    private static SMPParkour INSTANCE;

    public static SMPParkour getInstance() {
        return INSTANCE;
    }

    private PlayerManager playerManager;
    private ItemManager itemManager;
    private MapManager mapManager;
    private MinecraftScoreboardUtil scoreboardUtil;
    private Announcer announcer;

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public MinecraftScoreboardUtil getScoreboardUtil() {
        return scoreboardUtil;
    }

    public Announcer getAnnouncer() {
        return announcer;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        ConfigManager.initConfig();

        initializeParkourWorlds();

        // Setup the managers and listeners
        playerManager = new PlayerManager();
        itemManager = new ItemManager();
        mapManager = new MapManager();
        scoreboardUtil = new MinecraftScoreboardUtil();
        announcer = new Announcer();

        new ParkourCompletedListener();
    }

    @Override
    public void onDisable() {
        playerManager.cleanup();
        itemManager.cleanup();
        mapManager.cleanup();
    }

    public void initializeParkourWorlds() {

        getLogger().info("Initializing parkour worlds!");

        for (World world : getServer().getWorlds()) {
            // World settings
            world.setPVP(false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DISABLE_RAIDS, true);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.DO_INSOMNIA, false);
            world.setGameRule(GameRule.DO_MOB_LOOT, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
            world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.FALL_DAMAGE, false);
            world.setGameRule(GameRule.MOB_GRIEFING, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
            world.setGameRule(GameRule.SPAWN_RADIUS, 0);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);

            world.getWorldBorder().setCenter(world.getSpawnLocation());
            world.getWorldBorder().setSize(400);

            world.setDifficulty(Difficulty.PEACEFUL);
        }
    }

    public World getParkourWorld() {
        return Bukkit.getWorlds().get(0);
    }
}

package me.devvy.smpparkour;

import me.devvy.smpparkour.commands.ParkourWorldCommand;
import me.devvy.smpparkour.config.ConfigManager;
import me.devvy.smpparkour.items.ItemManager;
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

    private final String PARKOUR_WORLD_FOLDER_NAME = "world_parkour";
    private World parkourWorld;

    public World getParkourWorld() {
        return parkourWorld;
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

        loadParkourWorld();

        // Setup the managers and listeners
        playerManager = new PlayerManager();
        itemManager = new ItemManager();
        mapManager = new MapManager();
        scoreboardUtil = new MinecraftScoreboardUtil();
        announcer = new Announcer();

        ParkourWorldCommand parkourWorldCommand = new ParkourWorldCommand();
        getCommand("parkour").setExecutor(parkourWorldCommand);
        getCommand("parkour").setTabCompleter(parkourWorldCommand);

    }

    @Override
    public void onDisable() {
        playerManager.cleanup();
        itemManager.cleanup();
        mapManager.cleanup();

        unloadParkourWorld();
    }

    public void loadParkourWorld() {

        getLogger().info("Loading parkour world!");

        // Load the world
        WorldCreator wc = new WorldCreator(PARKOUR_WORLD_FOLDER_NAME);
        wc.generateStructures(false);
        wc.type(WorldType.FLAT);
        parkourWorld = Bukkit.createWorld(wc);

        if (parkourWorld == null)
            throw new IllegalStateException("Build world failed to load, aborting enabling of plugin");

        // World settings
        parkourWorld.setPVP(false);
        parkourWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        parkourWorld.setGameRule(GameRule.DISABLE_RAIDS, true);
        parkourWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        parkourWorld.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        parkourWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
        parkourWorld.setGameRule(GameRule.DO_INSOMNIA, false);
        parkourWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
        parkourWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        parkourWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        parkourWorld.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
        parkourWorld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        parkourWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        parkourWorld.setGameRule(GameRule.FALL_DAMAGE, false);
        parkourWorld.setGameRule(GameRule.MOB_GRIEFING, false);
        parkourWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
        parkourWorld.setGameRule(GameRule.SPAWN_RADIUS, 0);
        parkourWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);

        parkourWorld.getWorldBorder().setCenter(parkourWorld.getSpawnLocation());
        parkourWorld.getWorldBorder().setSize(400);

        parkourWorld.setDifficulty(Difficulty.PEACEFUL);
    }

    public void unloadParkourWorld() {

        if (parkourWorld == null) {
            getLogger().warning("Tried to disable parkour world but it is already off!");
            return;
        }

        getLogger().info("Disabling parkour world!");
        playerManager.cleanup();

        Bukkit.unloadWorld(parkourWorld, true);
    }
}

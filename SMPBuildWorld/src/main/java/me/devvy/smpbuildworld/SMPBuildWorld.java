package me.devvy.smpbuildworld;

import me.devvy.smpbuildworld.commands.BuildWorldCommand;
import me.devvy.smpbuildworld.listeners.ExplosionGriefProtection;
import me.devvy.smpbuildworld.player.PlayerManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMPBuildWorld extends JavaPlugin {

    private static SMPBuildWorld INSTANCE;

    public static SMPBuildWorld getInstance() {
        return INSTANCE;
    }

    /**
     * CONFIG CONSTANTS
     */
    private final String BUILD_WORLD_FOLDER_NAME = "world_buildworld";
    private final String CONFIG_PLUGIN_ENABLED_PATH = "buildworld-enabled";  // This field determines whether or not the plugin will allow players to go to build world
    private final String CONFIG_PLAYER_PLOT_LOCATIONS = "player-plots";  // This field holds two xz coords that denote what is owned

    private boolean buildWorldEnabled = false;
    private World buildWorld;

    private PlayerManager playerManager;

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        // Setup the config
        FileConfiguration cfg = getConfig();
        cfg.addDefault(CONFIG_PLUGIN_ENABLED_PATH, false);
        cfg.options().copyDefaults(true);
        saveConfig();

        // Should we turn on build world
        if (getConfig().getBoolean(CONFIG_PLUGIN_ENABLED_PATH)) {
            enableBuildWorld();
        }

        // Setup the managers and listeners
        playerManager = new PlayerManager();
        BuildWorldCommand buildWorldCommand = new BuildWorldCommand();
        getCommand("buildworld").setExecutor(buildWorldCommand);
        getCommand("buildworld").setTabCompleter(buildWorldCommand);
        new ExplosionGriefProtection();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (isBuildWorldEnabled())
            disableBuildWorld();
    }


    public boolean isBuildWorldEnabled() {
        return buildWorldEnabled;
    }

    public World getBuildWorld() {
        return buildWorld;
    }

    public void enableBuildWorld() {

        if (isBuildWorldEnabled()) {
            getLogger().warning("Tried to enable build world but it is already on!");
            return;
        }

        getLogger().info("Enabling build world!!!");

        // Announce to the world we enabled the plugin
        buildWorldEnabled = true;
        getConfig().set(CONFIG_PLUGIN_ENABLED_PATH, true);
        saveConfig();

        // Load the world
        WorldCreator wc = new WorldCreator(BUILD_WORLD_FOLDER_NAME);
        wc.generateStructures(false);
        wc.type(WorldType.FLAT);
        buildWorld = Bukkit.createWorld(wc);

        if (buildWorld == null) {
            getLogger().severe("Build world failed to load, aborting enabling of plugin");
            disableBuildWorld();
            return;
        }

        // World settings
        buildWorld.setSpawnLocation(new Location(buildWorld, 0, buildWorld.getHighestBlockYAt(0, 0), 0));
        buildWorld.setPVP(false);
        buildWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        buildWorld.setGameRule(GameRule.DISABLE_RAIDS, true);
        buildWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        buildWorld.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        buildWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
        buildWorld.setGameRule(GameRule.DO_INSOMNIA, false);
        buildWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
        buildWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        buildWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        buildWorld.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
        buildWorld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        buildWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        buildWorld.setGameRule(GameRule.FALL_DAMAGE, false);
        buildWorld.setGameRule(GameRule.MOB_GRIEFING, false);
        buildWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
        buildWorld.setGameRule(GameRule.SPAWN_RADIUS, 0);
        buildWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);

        buildWorld.getWorldBorder().setCenter(buildWorld.getSpawnLocation());
        buildWorld.getWorldBorder().setSize(1024);

        buildWorld.setDifficulty(Difficulty.PEACEFUL);

    }

    public void disableBuildWorld() {

        if (!buildWorldEnabled) {
            getLogger().warning("Tried to disable build world but it is already off!");
            return;
        }

        getLogger().info("Disabling build world!!!");
        playerManager.cleanup();


        Bukkit.unloadWorld(buildWorld, true);
        buildWorldEnabled = false;
        getConfig().set(CONFIG_PLUGIN_ENABLED_PATH, false);
        saveConfig();

    }
}

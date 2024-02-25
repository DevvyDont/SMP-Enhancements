package me.devvy.smpevents;

import me.devvy.smpevents.commands.EventCommand;
import me.devvy.smpevents.events.EventManager;
import me.devvy.smpevents.player.PlayerStateManager;
import me.devvy.smpevents.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class SMPEvents extends JavaPlugin {

    private static SMPEvents INSTANCE;

    public static SMPEvents getInstance() {
        return INSTANCE;
    }

    public static final String CONFIG_EVENT_HUB_PATH = "event-hub-location";

    private EventManager eventManager;
    private PlayerStateManager playerStateManager;

    public EventManager getEventManager() {
        return eventManager;
    }

    public PlayerStateManager getPlayerStateManager() {
        return playerStateManager;
    }

    public World getMainWorld() {
        return Bukkit.getWorlds().get(0);
    }

    public Location getEventHubLocation() {
        Location loc = getConfig().getLocation(CONFIG_EVENT_HUB_PATH);

        if (loc == null || loc.equals(getMainWorld().getSpawnLocation().zero()))
            return null;

        return loc;
    }



    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        // Any managers we need
        eventManager = new EventManager();
        playerStateManager = new PlayerStateManager();
        //todo persistent player state loading

        // Instantiate some commands
        EventCommand eventCommand = new EventCommand();
        getCommand("event").setExecutor(eventCommand);
        getCommand("event").setTabCompleter(eventCommand);

        // Setup the config
        FileConfiguration cfg = getConfig();
        cfg.addDefault(CONFIG_EVENT_HUB_PATH, getMainWorld().getSpawnLocation().zero());
        cfg.options().copyDefaults(true);
        saveConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        eventManager.stopEvent();

        playerStateManager.empty();
        if (playerStateManager.count() > 0) {
            // todo: persistent player state saving
            getLogger().warning("Some players are not online to be synced out of event mode");
        }
    }

    /**
     * Starts tracking a player and teleports a player to the event hub
     *
     * @param player
     */
    public void teleportToEventHub(Player player) {

        Location eventHubLoc = getEventHubLocation();
        if (eventHubLoc == null) {
            player.sendMessage(
                    Component.text("Event hub location is not set!", TextColor.color(200, 0, 0))
            );
            return;
        }

        playerStateManager.registerPlayer(player);

        player.teleport(eventHubLoc);
        player.sendMessage(
                ComponentUtil.getEventPrefix()
                        .append(Component.text("Welcome to the ", TextColor.color(200, 200, 200)))
                        .append(Component.text("Event Hub!", TextColor.color(255, 180, 0), TextDecoration.BOLD))
        );
        player.playSound(player.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

    }
}

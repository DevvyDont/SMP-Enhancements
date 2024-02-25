package me.devvy.smpevents.player;

import me.devvy.smpevents.SMPEvents;
import me.devvy.smpevents.hooks.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.*;

public class PlayerStateManager implements Listener {


    private final Map<UUID, EventPlayer> playerTracker = new HashMap<>();
    private Set<Listener> hooks = new HashSet<>();

    public PlayerStateManager() {
        SMPEvents.getInstance().getServer().getPluginManager().registerEvents(this, SMPEvents.getInstance());

        // Loop every second to do things to our players
        new PlayerStateManagerLoop(this).runTaskTimer(SMPEvents.getInstance(), 1, 20);

        // If DeathDrops is loaded, add a death cert hook
        if (SMPEvents.getInstance().getServer().getPluginManager().getPlugin("DeathDrops") != null)
            hooks.add(new DeathDropsHooks());

        // If SMPDuels is loaded, add a hook for its methods
        if (SMPEvents.getInstance().getServer().getPluginManager().getPlugin("SMPDuels") != null)
            hooks.add(new SMPDuelsHooks());

        // If Dodgebolt is loaded, add a hook for its methods
        if (SMPEvents.getInstance().getServer().getPluginManager().getPlugin("Dodgebolt") != null)
            hooks.add(new DodgeboltHooks());

        // If SMPParkour is loaded, add a hook for its methods
        if (SMPEvents.getInstance().getServer().getPluginManager().getPlugin("SMPParkour") != null)
            hooks.add(new SMPParkourHooks());

        // If Stimmys is loaded, add a hook for its methods
        if (SMPEvents.getInstance().getServer().getPluginManager().getPlugin("Stimmys") != null)
            hooks.add(new StimmysHooks());

        for (Listener hook : hooks)
            Bukkit.getPluginManager().registerEvents(hook, SMPEvents.getInstance());

        SMPEvents.getInstance().getLogger().info("Successfully hooked into " + hooks.size() + " plugins");
    }

    public Collection<EventPlayer> getAllEventPlayers() {
        return playerTracker.values();
    }

    public Collection<Player> getAllEventPlayerInstances() {
        List<Player> players = new ArrayList<>();
        for (EventPlayer ep : getAllEventPlayers())
            if (ep.isOnline())
                players.add(ep.getPlayer());

        return players;
    }

    public EventPlayer getEventPlayer(UUID playerID) {
        return playerTracker.get(playerID);
    }

    /**
     * See how many players are in the event area
     *
     * @return
     */
    public int count() {
        return playerTracker.size();
    }

    /**
     * Called when a player is entering the hub area and we need to put them in event mode and start tracking them
     *
     * @param player
     */
    public void registerPlayer(Player player) {

        // Edge case, are they somehow already here? Deregister them and reregister
        if (getEventPlayer(player.getUniqueId()) != null) {
            SMPEvents.getInstance().getLogger().warning(String.format("Tried to register event player %s but they were already being tracked", player.getName()));
            unregisterPlayer(player);
        }

        // Create an event player instance for them and track
        EventPlayer ep = new EventPlayer(player);
        playerTracker.put(player.getUniqueId(), ep);
        ep.enter();
    }

    /**
     * Called when a player is/wants to leave the hub area and back into smp mode
     *
     * @param player
     */
    public void unregisterPlayer(Player player) {

        // Edge case, is the player not being tracked? If so, we can probably ignore it because it should not happen
        EventPlayer ep = playerTracker.get(player.getUniqueId());
        if (ep == null) {
            SMPEvents.getInstance().getLogger().warning(String.format("Tried to unregister event player %s but they were not being tracked", player.getName()));
            return;
        }

        // Force their snapshot to sync, teleport them away, stop tracking them
        ep.exit();
        playerTracker.remove(player.getUniqueId());

    }

    /**
     * Deregisters everyone and sends them back home
     */
    public void empty() {
        Set<UUID> ids = new HashSet<>(playerTracker.keySet());
        for (UUID pID : ids) {
            Player p = Bukkit.getPlayer(pID);
            if (p != null)
                unregisterPlayer(p);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onRespawn(PlayerRespawnEvent event) {

        Location spawn = SMPEvents.getInstance().getEventHubLocation();
        if (spawn == null)
            return;

        // When a player is respawning and we own them, spawn them at the event hub
        if (playerTracker.containsKey(event.getPlayer().getUniqueId()))
            event.setRespawnLocation(spawn);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {

        // When a player dies and we own them, keep inventory should be on in most cases, of course we can override this
        // somewhere else
        if (!playerTracker.containsKey(event.getPlayer().getUniqueId()))
            return;

        EventPlayer ep = playerTracker.get(event.getPlayer().getUniqueId());
        if (!ep.shouldDropItemsOnDeath()) {
            event.setKeepLevel(true);
            event.setKeepInventory(true);
            event.getDrops().clear();
        }


        // If there is a death tracker, subtract one
        Objective obj = SMPEvents.getInstance().getServer().getScoreboardManager().getMainScoreboard().getObjective("deaths");
        if (obj == null)
            return;

        Score deathScore = obj.getScore(event.getPlayer());
        deathScore.setScore(deathScore.getScore() - 1);
    }

    // Just in case we don't allow invulnerable players on the server
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getEventPlayer(event.getPlayer().getUniqueId()) == null)
                    event.getPlayer().setInvulnerable(false);
            }
        }.runTaskLater(SMPEvents.getInstance(), 20*3);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {

        if (getEventPlayer(event.getPlayer().getUniqueId()) == null)
            return;

        unregisterPlayer(event.getPlayer());
    }


}

package me.devvy.smpmobarena.arena;


import me.devvy.smpmobarena.SMPMobArena;
import me.devvy.smpmobarena.hooks.scoreboard.ArenaScoreboard;
import me.devvy.smpmobarena.player.ArenaPlayerManager;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Contains a mob arena instance, manages it by spawning enemies, keeping track of players, etc.
 */
public class MobArena implements Listener {

    private final Location arenaOrigin;

    private final ArenaPlayerManager playerManager;
    private final ArenaGameplayManager gameplayManager;
    private final ArenaScoreboard scoreboard;

    public MobArena(Location location) {
        SMPMobArena.getInstance().getServer().getPluginManager().registerEvents(this, SMPMobArena.getInstance());
        this.arenaOrigin = location;

        this.playerManager = new ArenaPlayerManager(this);
        this.gameplayManager = new ArenaGameplayManager(this);
        this.scoreboard = new ArenaScoreboard(this);
    }

    public Location getArenaOrigin() {
        return arenaOrigin;
    }

    public ArenaGameplayManager getGameplayManager() {
        return gameplayManager;
    }

    public ArenaPlayerManager getPlayerManager() {
        return playerManager;
    }

    public ArenaScoreboard getScoreboard() {
        return scoreboard;
    }

    public boolean isInArena(Location location) {

        // todo: actually get specifics of arena
        return location.distance(getArenaOrigin()) < 30;
    }


    public void cleanup() {
        playerManager.cleanup();
        gameplayManager.cleanup();
        HandlerList.unregisterAll(this);
    }
}

package me.devvy.smpevents.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Keeps track of certain attributes of a player to revert to so people cannot hunger/hp exploit
 */
public class PlayerState {

    private final double health;
    private final double healthScale;
    private final int hunger;
    private final float saturation;
    private final Location location;
    private final int xpLevel;
    private final float xpProgress;

    /**
     * Copies a specific state of a player
     *
     * @param player
     */
    public PlayerState(Player player) {
        this.health = player.getHealth();
        this.healthScale = player.getHealthScale();
        this.hunger = player.getFoodLevel();
        this.saturation = player.getSaturation();
        this.location = player.getLocation().clone();
        this.xpLevel = player.getLevel();
        this.xpProgress = player.getExp();
    }

    /**
     * Syncs this player state to a given player
     *
     * @param player
     */
    public void sync(Player player) {
        player.setHealth(health);
        player.setHealthScale(healthScale);
        player.setFoodLevel(hunger);
        player.setSaturation(saturation);
        player.setLevel(xpLevel);
        player.setExp(xpProgress);
        player.teleport(location);
    }
}

package me.devvy.smpparkour.checkpoints;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.events.PlayerEnteredCheckpointEvent;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Base class for checkpoints that exist as regions in the world, responsible for listening when players cross into them
 */
public abstract class CheckpointRegion {


    public abstract World getResidingWorld();

    /**
     * Determines whether another location is within this boundary
     *
     * @param location Location to check
     * @return true if we are in it false otherwise
     */
    public abstract boolean contains(Location location);

    /**
     * Helper method to visualize where this area is
     */
    public abstract void visualize();

    protected void spawnBoundParticle(double x, double y, double z) {
        getResidingWorld().spawnParticle(Particle.REDSTONE, new Location(getResidingWorld(), x, y, z), 1, 0, 0, 0, 1, new Particle.DustOptions(Color.PURPLE, 1));
    }
}

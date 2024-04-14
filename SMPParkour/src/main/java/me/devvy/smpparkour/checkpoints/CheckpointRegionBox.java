package me.devvy.smpparkour.checkpoints;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class CheckpointRegionBox extends CheckpointRegion {

    private Location corner1;
    private Location corner2;

    public CheckpointRegionBox(Location corner1, Location corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;

        if (corner1.getWorld() != corner2.getWorld())
            throw new IllegalArgumentException("Worlds must be equal for both corners!");
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    @Override
    public World getResidingWorld() {
        return corner1.getWorld();
    }

    @Override
    public boolean contains(Location location) {

        // Not in the same world
        if (location.getWorld() != getResidingWorld())
            return false;

        // Find the min and max xyz coords so we can find two optimal corners to check from
        double minX = Math.min(corner1.x(), corner2.x());
        double minY = Math.min(corner1.y(), corner2.y());
        double minZ = Math.min(corner1.z(), corner2.z());

        double maxX = Math.max(corner1.x(), corner2.x());
        double maxY = Math.max(corner1.y(), corner2.y());
        double maxZ = Math.max(corner1.z(), corner2.z());


        // Check if the location xyz coords are within the bounded coords we just found
        return  location.x() >= minX && location.x() <= maxX &&
                location.y() >= minY && location.y() <= maxY &&
                location.z() >= minZ && location.z() <= maxZ;
    }

    @Override
    public void visualize() {

        // Visualize the 4 lines where only the x axis changes (constant y and z coordinates)
        for (double x = Math.min(corner1.x(), corner2.x()); x <= Math.max(corner1.x(), corner2.x()); x += .5) {
            spawnBoundParticle(x, corner1.y(), corner1.z());
            spawnBoundParticle(x, corner1.y(), corner2.z());
            spawnBoundParticle(x, corner2.y(), corner1.z());
            spawnBoundParticle(x, corner2.y(), corner2.z());
        }

        // Now do the same thing but for z
        for (double z = Math.min(corner1.z(), corner2.z()); z <= Math.max(corner1.z(), corner2.z()); z += .5) {
            spawnBoundParticle(corner1.x(), corner1.y(), z);
            spawnBoundParticle(corner2.x(), corner1.y(), z);
            spawnBoundParticle(corner1.x(), corner2.y(), z);
            spawnBoundParticle(corner2.x(), corner2.y(), z);
        }

        // Now do the same thing but for y
        for (double y = Math.min(corner1.y(), corner2.y()); y <= Math.max(corner1.y(), corner2.y()); y += .5) {
            spawnBoundParticle(corner1.x(), y, corner1.z());
            spawnBoundParticle(corner2.x(), y, corner1.z());
            spawnBoundParticle(corner1.x(), y, corner2.z());
            spawnBoundParticle(corner2.x(), y, corner2.z());
        }
    }
}

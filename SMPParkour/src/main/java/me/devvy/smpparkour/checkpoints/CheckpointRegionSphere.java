package me.devvy.smpparkour.checkpoints;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class CheckpointRegionSphere extends CheckpointRegion {

    private Location origin;
    private double radius;

    public CheckpointRegionSphere(Location origin, double radius) {
        this.origin = origin;
        this.radius = radius;
    }

    @Override
    public World getResidingWorld() {
        return origin.getWorld();
    }

    /**
     * If the location is close enough to the origin we are in
     *
     * @param location Location to check
     * @return
     */
    @Override
    public boolean contains(Location location) {

        // If same world cannot be in
        if (location.getWorld() != getResidingWorld())
            return false;

        return location.distance(origin) <= radius;
    }

    @Override
    public void visualize() {

        // Visualize 30 random points on the sphere's surface

        for (int i = 0; i < 50; i++) {
            Random rng = new Random();
            double x = rng.nextGaussian();
            double y = rng.nextGaussian();
            double z = rng.nextGaussian();

            // Normalize and extend to radius
            double normalization = Math.sqrt(x * x + y * y + z * z);

            double newx = x * (1 / normalization) * radius;
            double newy = y * (1 / normalization) * radius;
            double newz = z * (1 / normalization) * radius;

            // We now have coordinates to a point on our sphere IF it was at 0,0,0, so just translate it to origin pos
            spawnBoundParticle(newx+origin.x(), newy+origin.y(), newz+origin.z());
        }

    }
}

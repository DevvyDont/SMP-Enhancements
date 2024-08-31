package xyz.devvydont.smprpg.util.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class ParticleUtil {

    public static void spawnParticlesBetweenTwoPoints(Particle particle, World world, Vector p1, Vector p2, int density) {

        Vector direction = p2.subtract(p1);
        Location origin = new Location(world, p1.getX(), p1.getY(), p1.getZ());
        for (int i = 0; i < density; i++){
            Location particleLocation = origin.clone();
            float multiplier = (float) i / density;
            particleLocation.add(direction.clone().multiply(multiplier));
            world.spawnParticle(particle, particleLocation.getX(), particleLocation.getY(), particleLocation.getZ(), 1, 0, 0, 0, 0);
        }

    }

}

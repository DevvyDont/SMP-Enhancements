package xyz.devvydont.smprpg.entity.spawning;

import org.bukkit.Location;

public interface EntitySpawnCondition {

    boolean valid(Location location);

}

package xyz.devvydont.smprpg.entity.spawning;

import org.bukkit.Location;

/**
 * The default spawn condition for custom entities. Having this condition means custom entities can not spawn naturally.
 */
public class ImpossibleSpawnCondition implements EntitySpawnCondition {

    @Override
    public boolean valid(Location location) {
        return false;
    }
}

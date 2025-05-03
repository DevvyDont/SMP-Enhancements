package xyz.devvydont.smprpg.entity.components;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple entity component that tracks how much damage other entities have done to it.
 * Can be used to figure out what players contributed to kills, or if other entities also
 * helped. (Such as a player's pet dog)
 */
public class DamageTracker {

    // Tracks damage done by players (and other entities if desired)
    private final Map<UUID, Integer> damageTracker = new HashMap<>();
    private final Map<UUID, Integer> hitTracker = new HashMap<>();

    /**
     * Gets how much damage another entity has dealt to this entity
     * @param entity The entity we are querying damage for
     * @return The amount of damage the entity did.
     */
    public int getDamageDealtByEntity(Entity entity) {
        return damageTracker.getOrDefault(entity.getUniqueId(), 0);
    }

    /**
     * Gets how many times another entity has hit this entity.
     * @param entity The entity we are querying hits for
     * @return The amount of times the entity has damaged this entity.
     */
    public int getNumberOfHitsDealtByEntity(Entity entity) {
        return hitTracker.getOrDefault(entity.getUniqueId(), 0);
    }

    /**
     * Adds damage dealt to this entity to its tracker to be retrieved later if desired
     * @param entity The entity that dealt damage.
     * @param damage The amount of damage.
     * @return The new amount of total damage this entity has done.
     */
    public int addDamageDealtByEntity(Entity entity, int damage) {
        int old = damageTracker.getOrDefault(entity.getUniqueId(), 0);
        int oldHits = hitTracker.getOrDefault(entity.getUniqueId(), 0);
        int _new = damage + old;
        oldHits++;
        damageTracker.put(entity.getUniqueId(), _new);
        hitTracker.put(entity.getUniqueId(), oldHits);
        return _new;
    }

    /**
     * Gets a version of the damage tracker that only contains online players at the time of execution
     * @return A newly created map that only contains currently online players that have damaged this entity.
     */
    public Map<Player, Integer> getPlayerDamageTracker() {

        var map = new HashMap<Player, Integer>();
        for (Map.Entry<UUID, Integer> entry : damageTracker.entrySet()) {

            Player p = Bukkit.getPlayer(entry.getKey());
            if (p == null)
                continue;

            map.put(p, entry.getValue());
        }
        return map;
    }

}

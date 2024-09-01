package xyz.devvydont.smprpg.entity.base;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.devvydont.smprpg.SMPRPG;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class EnemyEntity extends LeveledEntity {

    // Tracks damage done by players (and other entities if desired)
    private Map<UUID, Integer> damageTracker = new HashMap<>();
    private Map<UUID, Integer> hitTracker = new HashMap<>();

    public EnemyEntity(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    /**
     * Gets how much damage another entity has dealt to this entity
     *
     * @param entity The entity we are querying damage for
     * @return
     */
    public int getDamageDealtByEntity(Entity entity) {
        return damageTracker.getOrDefault(entity.getUniqueId(), 0);
    }

    /**
     * Gets how many times another entity has hit this entity.
     *
     * @param entity The entity we are querying hits for
     * @return
     */
    public int getNumberOfHitsDealtByEntity(Entity entity) {
        return hitTracker.getOrDefault(entity.getUniqueId(), 0);
    }

    /**
     * Adds damage dealt to this entity to its tracker to be retrieved later if desired
     *
     * @param entity
     * @param damage
     * @return
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

    public Map<UUID, Integer> getDamageTracker() {
        return damageTracker;
    }

    /**
     * Gets a version of the damage tracker that only contains online players at the time of execution
     *
     * @return
     */
    public Map<Player, Integer> getPlayerDamageTracker() {
        Map<Player, Integer> map = new HashMap<>();
        for (Map.Entry<UUID, Integer> entry : damageTracker.entrySet()) {

            Player p = Bukkit.getPlayer(entry.getKey());
            if (p == null)
                continue;

            map.put(p, entry.getValue());
        }
        return map;
    }



}

package xyz.devvydont.smprpg.entity.base;

import org.bukkit.entity.LivingEntity;
import xyz.devvydont.smprpg.SMPRPG;

public abstract class EnemyEntity extends LeveledEntity {

    public EnemyEntity(SMPRPG plugin, LivingEntity entity) {
        super(plugin, entity);
    }
}

package xyz.devvydont.smprpg.entity.base;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;

/**
 * Can be used for enemies that don't have any special functionality defined in CustomEntityType.
 * If a custom entity needs to listen and react to events, make a child class that extends this and implement
 * Listener
 */
public abstract class CustomEntityInstance extends EnemyEntity {

    private final CustomEntityType entityType;

    public CustomEntityInstance(SMPRPG plugin, LivingEntity entity, CustomEntityType entityType) {
        super(plugin, entity);
        this.entityType = entityType;
    }

    public CustomEntityType getEntityType() {
        return entityType;
    }

    @Override
    public String getClassKey() {
        return entityType.key();
    }

    @Override
    public EntityType getDefaultEntityType() {
        return entityType.entityType;
    }

    @Override
    public String getDefaultName() {
        return entityType.name;
    }

    @Override
    public int getDefaultLevel() {
        return entityType.baseLevel;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return entityType.baseDamage;
    }

    @Override
    public void updateAttributes() {
        updateBaseAttribute(Attribute.GENERIC_MAX_HEALTH, entityType.baseHp);
        heal();
        updateBaseAttribute(Attribute.GENERIC_ATTACK_DAMAGE, calculateBaseAttackDamage());
    }

    public boolean isEntityOfType(Entity entity) {
        return getEntityType().isOfType(plugin.getEntityService(), entity);
    }

    public boolean isEntity(Entity entity) {
        return entity.equals(this.entity);
    }
}

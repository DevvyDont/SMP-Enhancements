package xyz.devvydont.smprpg.entity.base;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;

public class CustomBossInstance extends BossInstance {

    private final CustomEntityType type;

    public CustomBossInstance(SMPRPG plugin, Entity entity, CustomEntityType type) {
        super(plugin, entity);
        this.type = type;
    }

    @Override
    public String getClassKey() {
        return type.key();
    }

    @Override
    public EntityType getDefaultEntityType() {
        return type.entityType;
    }

    @Override
    public String getEntityName() {
        return type.name;
    }

    @Override
    public int getDefaultLevel() {
        return type.baseLevel;
    }

    @Override
    public double calculateBaseHealth() {
        return type.baseHp;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return type.baseDamage;
    }

    @Override
    public void updateAttributes() {
        updateBaseAttribute(Attribute.GENERIC_MAX_HEALTH, type.baseHp);
        heal();
        updateBaseAttribute(Attribute.GENERIC_ATTACK_DAMAGE, calculateBaseAttackDamage());
    }

    public boolean isEntityOfType(Entity entity) {
        return type.isOfType(plugin.getEntityService(), entity);
    }

    public boolean isEntity(Entity entity) {
        return entity.equals(this.entity);
    }
}

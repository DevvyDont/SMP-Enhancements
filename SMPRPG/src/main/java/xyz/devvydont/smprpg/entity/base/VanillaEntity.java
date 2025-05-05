package xyz.devvydont.smprpg.entity.base;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.EntityGlobals;
import xyz.devvydont.smprpg.entity.components.DamageTracker;
import xyz.devvydont.smprpg.entity.components.EntityConfiguration;
import xyz.devvydont.smprpg.entity.interfaces.IDamageTrackable;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;


public class VanillaEntity<T extends Entity> extends LeveledEntity<T> implements IDamageTrackable {

    public static final String VANILLA_CLASS_KEY = "vanilla";

    private final DamageTracker _tracker = new DamageTracker();


    public VanillaEntity(T entity) {
        super(entity);
    }

    @Override
    public DamageTracker getDamageTracker() {
        return _tracker;
    }

    @Override
    public int getInvincibilityTicks() {
        return 0;
    }

    @Override
    public String getClassKey() {
        return VANILLA_CLASS_KEY;
    }

    @Override
    public EntityType getDefaultEntityType() {
        return _entity.getType();
    }

    @Override
    public String getEntityName() {
        return MinecraftStringUtils.getTitledString(_entity.getType().name());
    }

    @Override
    public double getCombatExperienceMultiplier() {
        return EntityGlobals.resolveVanillaEntityCombatExperienceMultiplier(_entity) * super.getCombatExperienceMultiplier();
    }

    @Override
    public void updateAttributes() {
        updateBaseAttribute(AttributeWrapper.HEALTH, this._config.getBaseHealth());
        updateBaseAttribute(AttributeWrapper.STRENGTH, this._config.getBaseDamage());
        heal();
    }

    @Override
    public EntityConfiguration getDefaultConfiguration() {
        return EntityConfiguration.builder()
                .withLevel(this.getLevel())
                .withHealth(EntityGlobals.calculateExpectedEntityEhp(this.getLevel()))
                .withDamage(EntityGlobals.calculateExpectedEntityEhp(this.getLevel()) / EntityGlobals.ENTITY_HITS_TO_KILL_PLAYER)
                .build();
    }

    @Override
    public boolean hasVanillaDrops() {
        return true;
    }
}

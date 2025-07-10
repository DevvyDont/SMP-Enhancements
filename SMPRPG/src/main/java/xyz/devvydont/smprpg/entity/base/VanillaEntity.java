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
    public double getSkillExperienceMultiplier() {
        return EntityGlobals.resolveVanillaEntityCombatExperienceMultiplier(_entity) * super.getSkillExperienceMultiplier();
    }

    @Override
    public void updateAttributes() {
        updateBaseAttribute(AttributeWrapper.HEALTH, this._config.getBaseHealth());
        updateBaseAttribute(AttributeWrapper.STRENGTH, this._config.getBaseDamage());
        heal();
    }

    @Override
    public EntityConfiguration getDefaultConfiguration() {

        // Get the default configuration of this entity at its minimum level.
        var lvl = EntityGlobals.getMinimumLevel(_entity.getType());
        return EntityConfiguration.builder()
                .withLevel(lvl)  // Always use the default level as a default config.
                .withHealth(EntityGlobals.softRoundHealth(EntityGlobals.calculateExpectedEntityEhp(lvl)))
                .withDamage(EntityGlobals.calculateExpectedEntityEhp(lvl) / EntityGlobals.ENTITY_HITS_TO_KILL_PLAYER)
                .build();
    }

    @Override
    public void setup() {
        super.setup();

        // Since this is a vanilla entity, we need to calculate its level.
        // We essentially just want to make sure the entity is at least the level high enough compared to the location.
        // A minimum level was set, but depending on the spawn location of this entity, it can be modified.
        var locationModifiedLevel = EntityGlobals.determineLocationLevel(_entity.getLocation());
        // If it should be higher, scale their stats.
        this.setLevel(Math.max(locationModifiedLevel, getLevel()));
    }

    @Override
    public boolean hasVanillaDrops() {
        return true;
    }
}

package xyz.devvydont.smprpg.entity.base;

import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import xyz.devvydont.smprpg.util.attributes.EntityHelpers;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;


public class VanillaEntity<T extends Entity> extends EnemyEntity<T> {

    // How aggresively should stats of this mob scale?
    public static final double STAT_SCALING_FACTOR = 1.0964782;

    // How many hits should it take for this mob to kill another mob if its level?
    public static final double DPS_FACTOR = 9.0;

    public static final String VANILLA_CLASS_KEY = "vanilla";


    public VanillaEntity(T entity) {
        super(entity);
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

    public int getLevelDistanceBoost() {
        return (int) (_entity.getLocation().distance(_entity.getWorld().getSpawnLocation()) / 5000) * 5;
    }

    public int getLevelDepthBoost() {

        if (_entity.getLocation().getY() >= 64)
            return 0;

        double x = Math.abs(64.0 - _entity.getLocation().getY());
        return (int) (x / 20);
    }

    public int getMinimumEnvironmentLevel(World.Environment environment) {
        return switch (environment) {
            case NETHER -> 20;
            case THE_END -> 40;
            default -> 1;
        };
    }

    /**
     * This is the default handling for entities that do not have a defined class handler.
     * All default enemies will start with a level of 1.
     * For every 5k blocks, minimum level is increased by 5 per 5k blocks.
     * For every 10 blocks underground, minimum level is increased by 1.
     * The max level a naturally spawned mob can be is 99.
     *
     * @return
     */
    @Override
    public int getDefaultLevel() {
        int level = EntityHelpers.getMinimumLevel(getDefaultEntityType());
        level = Math.max(level, getMinimumEnvironmentLevel(_entity.getLocation().getWorld().getEnvironment()));
        level += (int) (Math.random() * 6);

        level += getLevelDistanceBoost();
        level += getLevelDepthBoost();
        if (level > 99)
            level = 99;
        return level;
    }

    public double calculateBaseHealthMultiplier() {

        if (_entity instanceof Monster)
            return 1.0;

        if (_entity instanceof Animals)
            return .2;

        if (_entity instanceof Fish)
            return .1;

        if (_entity instanceof Ambient)
            return .1;

        return 1.0;
    }

    public double calculateBaseHealth() {
        double hp = calculateBaseHealthMultiplier() * (int) Math.ceil(100*Math.pow(STAT_SCALING_FACTOR, getLevel()));;

        // If the HP is less than 30 leave it be
        if (hp < 20)
            return Math.round(hp);

        // If the HP is less than 500, round the number to be divisible by 5
        if (hp < 500)
            return Math.round(hp / 5) * 5;

        // If the HP is less than 1000, round the number to be divisible by 10
        if (hp < 1000)
            return Math.round(hp / 10) * 10;

        // If the HP is less than 5000, round the number to be divisible by 100
        if (hp < 5000)
            return Math.round(hp / 100) * 100;

        // 5000 and great round by 250
        return Math.round(hp / 500) * 500;
    }

    public double calculateBaseDamageMultiplier() {

        if (!(_entity instanceof LivingEntity living))
            return 1;

        // Based on vanilla minecraft's rules for damage, figure out a suitable damage multiplier for this entity
        double averageDamage = 2.0;
        AttributeInstance attack = living.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attack == null)
            return averageDamage;

        return attack.getDefaultValue() / averageDamage;
    }

    @Override
    public double getCombatExperienceMultiplier() {
        return calculateBaseHealthMultiplier() * super.getCombatExperienceMultiplier();
    }

    @Override
    public double calculateBaseAttackDamage() {
        // Return the value that would make this mob 10 shot another mob of the same level with default stats
        return calculateBaseHealth() / DPS_FACTOR * calculateBaseDamageMultiplier();
    }

    @Override
    public void updateAttributes() {
        updateBaseAttribute(Attribute.MAX_HEALTH, calculateBaseHealth());
        heal();
        updateBaseAttribute(Attribute.ATTACK_DAMAGE, calculateBaseAttackDamage());
    }

    @Override
    public boolean hasVanillaDrops() {
        return true;
    }
}

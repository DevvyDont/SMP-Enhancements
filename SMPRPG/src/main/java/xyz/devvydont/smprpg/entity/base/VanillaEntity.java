package xyz.devvydont.smprpg.entity.base;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.attributes.EntityHelpers;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;


public class VanillaEntity extends EnemyEntity {

    // How aggresively should stats of this mob scale?
    public static final double STAT_SCALING_FACTOR = 2.5;

    // How many hits should it take for this mob to kill another mob if its level?
    public static final double DPS_FACTOR = 10.0;

    public static final String VANILLA_CLASS_KEY = "vanilla";


    public VanillaEntity(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public String getClassKey() {
        return VANILLA_CLASS_KEY;
    }

    @Override
    public EntityType getDefaultEntityType() {
        return entity.getType();
    }

    @Override
    public String getDefaultName() {
        return MinecraftStringUtils.getTitledString(entity.getType().name());
    }

    public int getLevelDistanceBoost() {
        return (int) (entity.getLocation().distance(entity.getWorld().getSpawnLocation()) / 5000) * 5;
    }

    public int getLevelDepthBoost() {

        if (entity.getLocation().getY() >= 64)
            return 0;

        double x = Math.abs(64.0 - entity.getLocation().getY());
        return (int) (x / 10);
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

        level += (int) (Math.random() * 6);

        level += getLevelDistanceBoost();
        level += getLevelDepthBoost();
        if (level > 99)
            level = 99;
        return level;
    }

    public double calculateBaseHealthMultiplier() {
        return 1.0;
    }

    public double calculateBaseHealth() {
        double hp = calculateBaseHealthMultiplier() * Math.pow(getLevel(), STAT_SCALING_FACTOR) + 90.0;

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

        if (!(entity instanceof LivingEntity living))
            return 1;

        // Based on vanilla minecraft's rules for damage, figure out a suitable damage multiplier for this entity
        double averageDamage = 2.0;
        AttributeInstance attack = living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
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
        updateBaseAttribute(Attribute.GENERIC_MAX_HEALTH, calculateBaseHealth());
        heal();
        updateBaseAttribute(Attribute.GENERIC_ATTACK_DAMAGE, calculateBaseAttackDamage());
    }

    @Override
    public boolean hasVanillaDrops() {
        return true;
    }
}

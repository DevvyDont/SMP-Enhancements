package xyz.devvydont.smprpg.entity;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

public class EntityGlobals {

    /**
     * How aggressively should stats of this mob scale?
     * Keep in mind, we can't just change this for fun as gear is
     * not scaled to take into account entity EHP.
     */
    public static final double STAT_SCALING_FACTOR = 1.0964782;

    /**
     * What's the highest level an entity is able to spawn naturally as?
     * This will not affect entities that have been spawned in by other means.
     */
    public static final int NATURAL_ENTITY_LEVEL_CAP = 120;

    /**
     * By default, how many times should a standard entity have to hit another player/entity
     * that is similar in level to them to kill them?
     */
    public static final int ENTITY_HITS_TO_KILL_PLAYER = 9;

    /**
     * Helper method to determine a color to associate with a certain health percentage given HP and max HP
     *
     * @param hp The HP of the entity
     * @param maxHp The max HP of an entity
     * @return A text color to associate with a certain percentage of health
     */
    public static TextColor getChatColorFromHealth(double hp, double maxHp) {
        double percent = hp / maxHp;
        if (percent <= 0)
            return NamedTextColor.DARK_GRAY;
        else if (percent < .20)
            return NamedTextColor.DARK_RED;
        else if (percent < .50)
            return NamedTextColor.GOLD;
        else if (percent < .8)
            return NamedTextColor.YELLOW;
        else if (percent <= 1.0)
            return NamedTextColor.GREEN;
        else
            return NamedTextColor.AQUA;
    }

    /**
     * Calculates the expected EHP of an entity with a 1.0 HP multiplier.
     * @param level The level of the entity.
     * @return How much effective health they should have.
     */
    public static int calculateExpectedEntityEhp(int level) {
        return (int) Math.ceil(100*Math.pow(STAT_SCALING_FACTOR, level));
    }

    /**
     * Given a health value, determine a new health value that has a prettier number.
     * Used to make procedurally generated enemies have prettier health values.
     * For example, 74,783 will become 75,000
     * @param hp The hp to prettify.
     * @return A more clean amount of health.
     */
    public static int softRoundHealth(double hp) {

        // If the HP is less than 20 leave it be
        if (hp < 20)
            return (int) Math.round(hp);

        // If the HP is less than 500, round the number to be divisible by 5
        if (hp < 500)
            return (int) Math.round(hp / 5) * 5;

        // If the HP is less than 1000, round the number to be divisible by 10
        if (hp < 1000)
            return (int) Math.round(hp / 10) * 10;

        // If the HP is less than 5000, round the number to be divisible by 100
        if (hp < 5000)
            return (int) Math.round(hp / 100) * 100;

        // 5000 and great round by 250
        return (int) Math.round(hp / 500) * 500;
    }

    /**
     * The expected combat experience multiplier for vanilla entities that don't have a specific handler.
     * Keep in mind that if an entity has a handler, the experience is defined there.
     * @param entity The entity to check for combat experience.
     * @return The multiplier for combat xp.
     */
    public static float resolveVanillaEntityCombatExperienceMultiplier(Entity entity) {
        return switch (entity) {
            case Animals ignored -> 0.35f;
            case Fish ignored -> 0.2f;
            case Ambient ignored -> 0.1f;
            default -> 1f;
        };
    }

    /**
     * Some vanilla entities will have a bare minimum level that they can spawn in as
     * @param entityType The type of entity.
     * @return What level they should always at least be.
     */
    public static int getMinimumLevel(EntityType entityType) {

        return switch (entityType) {

            case WARDEN -> 100;

            case BREEZE -> 25;
            case BOGGED -> 25;

            case SHULKER -> 50;
            case ENDER_DRAGON -> 40;
            case WITHER -> 30;

            case ENDERMAN -> 35;
            case ENDERMITE -> 35;
            case SILVERFISH -> 35;

            case PIGLIN_BRUTE -> 40;

            case ALLAY -> 30;

            case PIGLIN -> 30;
            case BLAZE -> 30;
            case WITHER_SKELETON -> 25;
            case MAGMA_CUBE -> 20;
            case ZOMBIFIED_PIGLIN -> 20;
            case STRIDER -> 25;
            case GHAST -> 20;
            case HOGLIN -> 25;
            case ZOGLIN -> 25;
            case SNIFFER -> 25;

            case VEX -> 16;
            case VINDICATOR -> 20;
            case PILLAGER -> 15;
            case RAVAGER -> 20;
            case ILLUSIONER -> 20;
            case EVOKER -> 20;

            case HUSK -> 5;
            case STRAY -> 5;
            case DROWNED -> 5;

            case ELDER_GUARDIAN -> 20;
            case GUARDIAN -> 18;

            case WOLF -> 15;
            case HORSE -> 15;

            case SLIME -> 15;
            case WITCH -> 15;

            case IRON_GOLEM -> 25;
            case VILLAGER -> 20;
            case ZOMBIE_VILLAGER -> 10;

            case GOAT -> 10;
            case CAMEL -> 10;
            case DONKEY -> 10;
            case MULE -> 10;
            case PANDA -> 10;

            default -> 1;
        };
    }

    public static int getMinimumEnvironmentLevel(World.Environment environment) {
        return switch (environment) {
            case NETHER -> 20;
            case THE_END -> 40;
            default -> 1;
        };
    }

    /**
     * Gets the flat level boost to give an entity at a certain depth location.
     * @param location The location of the entity.
     * @return How many extra levels to give them.
     */
    public static int getLevelDepthBoost(Location location) {

        if (location.getY() >= 64)
            return 0;

        double x = Math.abs(64.0 - location.getY());
        return (int) (x / 20);
    }


    /**
     * Gets the flat level boost to give an entity at a certain distance in the world.
     * @param location The location of the entity.
     * @return How many extra levels to give them.
     */
    public static int getLevelDistanceBoost(Location location) {
        return (int) (location.distance(location.getWorld().getSpawnLocation()) / 5000) * 5;
    }

    /**
     * Determines a suitable default level for an entity to be assuming it is spawning at the given location.
     * Useful for assigning levels for entities that don't have any strict definition for what they should be.
     * @param location The location of the entity.
     * @return A suitable level for the entity to be.
     */
    public static int determineLocationLevel(Location location) {

        // Determine the level of the environment and location this entity is at.
        var level = Math.max(1, getMinimumEnvironmentLevel(location.getWorld().getEnvironment()));
        level += getLevelDistanceBoost(location);
        level += getLevelDepthBoost(location);

        // Add variance.
        level += (int) (Math.random() * 6 - 3);

        // Make sure we are not above the level cap.
        return Math.min(level, NATURAL_ENTITY_LEVEL_CAP);
    }

}

package xyz.devvydont.smprpg.entity.components;

import xyz.devvydont.smprpg.entity.EntityGlobals;

/**
 * Holds information that is used to build the properties that an entity has, like damage and health.
 */
public interface EntityConfiguration {

    /**
     * The base level of this entity.
     * This is the level that will be used for dynamic stat calculation, meaning this entity doesn't always need
     * to spawn at this level, it just defines a baseline to use for getHealth() and getDamage().
     * @return The base level of the entity.
     */
    int getBaseLevel();

    /**
     * The base health for this entity when the entity is at the level defined by getBaseLevel().
     * @return The base health of the entity.
     */
    long getBaseHealth();

    /**
     * The base attack damage for this entity when the entity is at the level defined by getBaseLevel().
     * @return The base attack damage of the entity.
     */
    long getBaseDamage();

    /**
     * The default entity configuration. If an entity doesn't specify a configuration to use, default to this one.
     */
    EntityConfiguration DEFAULT = builder().withLevel(1).withHealth(100).withDamage(10).build();

    /**
     * The configuration to use for players.
     */
    EntityConfiguration PLAYER = builder().withHealth(100).withDamage(5).build();

    /**
     * There may be a time when an entity spawns at a level that is different from their base configuration.
     * When this happens, we should scale the stats of the entity to reflect the new level.
     * This function will create a new EntityConfiguration instance that will have adjusted attributes
     * that should fit for the entity if it were the desired level.
     * @param base The base entity configuration.
     * @param desiredLevel The level to scale the config to.
     * @return A completely new EntityConfiguration, resulting the new base options.
     */
    static EntityConfiguration scale(EntityConfiguration base, int desiredLevel) {

        // Calculate the expected EHP of a normal entity that the config defined, and work out its health multiplier.
        var oldEhp = EntityGlobals.calculateExpectedEntityEhp(base.getBaseLevel());
        var hpMultiplier = (double) base.getBaseHealth() / oldEhp;

        // Do the same logic for damage. Since damage is just a ratio of how much health they have, we can do the same thing.
        var damageMultiplier = (double) base.getBaseDamage() / oldEhp;

        // Now convert to the new values by using the EHP multipliers.
        var newEhp = EntityGlobals.calculateExpectedEntityEhp(desiredLevel);
        var newHp = EntityGlobals.softRoundHealth(newEhp * hpMultiplier);
        var newDamage = (int) Math.ceil(newEhp * damageMultiplier);

        return builder()
                .withLevel(desiredLevel)
                .withHealth(newHp)
                .withDamage(newDamage)
                .build();
    }

    static EntityConfigurationBuilder builder() {
        return new EntityConfigurationBuilder();
    }

    class EntityConfigurationBuilder {

        private int _level;
        private long _health;
        private long _damage;

        private EntityConfigurationBuilder() {
            this._level = 1;
            this._health = 100;
            this._damage = 10;
        }

        public EntityConfigurationBuilder withLevel(int level) {
            this._level = level;
            return this;
        }

        public EntityConfigurationBuilder withHealth(long health) {
            this._health = health;
            return this;
        }

        public EntityConfigurationBuilder withDamage(long damage) {
            this._damage = damage;
            return this;
        }

        public EntityConfiguration build() {
            return new EntityConfiguration() {
                @Override
                public int getBaseLevel() {
                    return _level;
                }

                @Override
                public long getBaseHealth() {
                    return _health;
                }

                @Override
                public long getBaseDamage() {
                    return _damage;
                }
            };
        }

    }

}

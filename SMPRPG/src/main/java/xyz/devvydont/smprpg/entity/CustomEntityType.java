package xyz.devvydont.smprpg.entity;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.creatures.TestZombie;
import xyz.devvydont.smprpg.entity.creatures.WitheredSeraph;
import xyz.devvydont.smprpg.entity.spawning.BiomeSpawnCondition;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawnCondition;
import xyz.devvydont.smprpg.entity.spawning.ImpossibleSpawnCondition;
import xyz.devvydont.smprpg.services.EntityService;

// Enums to use for the retrieval, storage, and statistics of "custom" entities.
public enum CustomEntityType {

    // Wither skeletons that spawn on the end island
    WITHERED_SERAPH(EntityType.WITHER_SKELETON, "Withered Seraph",
            45, 5_000, 900,
            new BiomeSpawnCondition[]{new BiomeSpawnCondition(Biome.THE_END)}, 15,
            WitheredSeraph.class
    ),

    TEST_ZOMBIE(EntityType.ZOMBIE, "Test Zombie", 10, 15, 50, TestZombie.class),
    TEST_SKELETON(EntityType.SKELETON, "Test Skeleton", 5, 10, 25, CustomEntityInstance.class),
    ;


    // The vanilla entity that this entity will display as and spawn as.
    public final EntityType entityType;
    // The name of this entity
    public final String name;
    // The "power level" that this entity is, affects default spawning level as well as base for scaling
    public final int baseLevel;
    // The HP this entity will have when their level is at its base level
    public final int baseHp;
    // The base damage this entity will do when their level is at its base level
    public final int baseDamage;
    public final EntitySpawnCondition[] spawnConditions;
    // The chance that this entity will override a vanilla creature spawn given the conditions passed
    public final int chance;
    // The handler class to use when instantiating an instance for this class, used if any special event handlers or attributes need to be set for the entity
    public final Class<? extends LeveledEntity> entityHandler;

    CustomEntityType(EntityType entityType, String name, int baseLevel, int baseHp, int baseDamage, Class<? extends LeveledEntity> entityHandler) {
        this.entityType = entityType;
        this.name = name;
        this.baseLevel = baseLevel;
        this.baseHp = baseHp;
        this.baseDamage = baseDamage;
        this.chance = 0;
        this.spawnConditions = new EntitySpawnCondition[]{new ImpossibleSpawnCondition()};
        this.entityHandler = entityHandler;
    }

    CustomEntityType(EntityType entityType, String name, int baseLevel, int baseHp, int baseDamage, final EntitySpawnCondition[] spawnConditions, int chance, Class<? extends LeveledEntity> entityHandler) {
        this.entityType = entityType;
        this.name = name;
        this.baseLevel = baseLevel;
        this.baseHp = baseHp;
        this.baseDamage = baseDamage;
        this.spawnConditions = spawnConditions;
        this.chance = chance;
        this.entityHandler = entityHandler;
    }

    /**
     * Returns the key of the entity to store in the PDC
     *
     * @return
     */
    public String key() {
        return name().toLowerCase();
    }

    /**
     * Returns whether or not a given entity is of this custom entity type.
     *
     * @param entityService
     * @param entity
     * @return
     */
    public boolean isOfType(EntityService entityService, Entity entity) {
        return entity.getPersistentDataContainer().getOrDefault(entityService.getClassNamespacedKey(), PersistentDataType.STRING, "").equals(key());
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getName() {
        return name;
    }

    public int getBaseLevel() {
        return baseLevel;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public EntitySpawnCondition[] getSpawnConditions() {
        return spawnConditions;
    }

    public boolean canSpawnNaturally() {
        for (EntitySpawnCondition spawnCondition : spawnConditions)
            if (spawnCondition instanceof ImpossibleSpawnCondition)
                return false;
        return true;
    }

    public boolean testNaturalSpawn(Location location) {

        // Test every spawn condition for a location. If any of them fail, then the whole spawn attempt will fail.
        for (EntitySpawnCondition spawnCondition : spawnConditions)
            if (!spawnCondition.valid(location))
                return false;

        // We passed, pass true
        return true;
    }

    public boolean rollRandomSpawnChance() {
        return Math.random() * 100 < chance;
    }

    public Class<? extends LeveledEntity> getEntityHandler() {
        return entityHandler;
    }
}

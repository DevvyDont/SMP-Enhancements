package xyz.devvydont.smprpg.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.creatures.TestZombie;
import xyz.devvydont.smprpg.services.EntityService;

// Enums to use for the retrieval, storage, and statistics of "custom" entities.
public enum CustomEntityType {

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
    // The handler class to use when instantiating an instance for this class, used if any special event handlers or attributes need to be set for the entity
    public final Class<? extends LeveledEntity> entityHandler;

    CustomEntityType(EntityType entityType, String name, int baseLevel, int baseHp, int baseDamage, Class<? extends LeveledEntity> entityHandler) {
        this.entityType = entityType;
        this.name = name;
        this.baseLevel = baseLevel;
        this.baseHp = baseHp;
        this.baseDamage = baseDamage;
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
}

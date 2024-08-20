package xyz.devvydont.smprpg.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.structure.Structure;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.base.NPCEntity;
import xyz.devvydont.smprpg.entity.creatures.*;
import xyz.devvydont.smprpg.entity.npc.ReforgeNPC;
import xyz.devvydont.smprpg.entity.spawning.*;
import xyz.devvydont.smprpg.services.EntityService;

// Enums to use for the retrieval, storage, and statistics of "custom" entities.
public enum CustomEntityType {

    // Mobs that spawn in castles.
    CASTLE_DWELLER(EntityType.ZOMBIE_VILLAGER, "Castle Dweller",
            15, 900, 80, CastleDweller.class),

    UNDEAD_ARCHER(EntityType.SKELETON, "Undead Archer",
            15, 750, 60, UndeadArcher.class),

    // Mobs that spawn in woodland mansions.
    MANSION_SPIDER(EntityType.SPIDER, "Mansion Spider",
            25, 3_000, 290,
            new EntitySpawnCondition[]{new StructureSpawnCondition(Structure.MANSION)}, 20,
            MansionSpider.class),

    WOODLAND_EXILE(EntityType.PILLAGER, "Woodland Exile",
            25, 3_500, 315,
            new EntitySpawnCondition[]{new StructureSpawnCondition(Structure.MANSION)}, 35,
            WoodlandExile.class),

    WOODLAND_BERSERKER(EntityType.VINDICATOR, "Woodland Berserker",
            25, 3_250, 450,
            new EntitySpawnCondition[]{new StructureSpawnCondition(Structure.MANSION)}, 35,
            WoodlandExile.class),

    PALACE_THUG(EntityType.WITHER_SKELETON, "Palace Thug",
            35, 7_500, 920,
            new EntitySpawnCondition[]{new StructureSpawnCondition(Structure.FORTRESS)}, 10,
            PalaceThug.class),

    FIERY_SYLPH(EntityType.BLAZE, "Fiery Sylph",
            35, 7_000, 1000,
            new EntitySpawnCondition[]{new StructureSpawnCondition(Structure.FORTRESS)}, 10,
            FierySylph.class),

    // Wither skeletons that spawn on the end island
    WITHERED_SERAPH(EntityType.WITHER_SKELETON, "Withered Seraph",
            45, 16_000, 1800,
            new BiomeSpawnCondition[]{new BiomeSpawnCondition(Biome.THE_END)}, 15,
            WitheredSeraph.class
    ),

    TEST_ZOMBIE(EntityType.ZOMBIE, "Test Zombie", 10, 15, 50, TestZombie.class),
    TEST_SKELETON(EntityType.SKELETON, "Test Skeleton", 5, 10, 25, CustomEntityInstance.class),

    // NPCs
    REFORGE_NPC(EntityType.VILLAGER, "Tool Reforger", ReforgeNPC.class),

    // Spawner
    SPAWNER(EntityType.ITEM_DISPLAY, "Spawner", EntitySpawner.class)
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

    CustomEntityType(EntityType entityType, String name, Class<? extends LeveledEntity> entityHandler) {
        this.entityType = entityType;
        this.name = name;
        this.baseLevel = 0;
        this.baseHp = 999_999;
        this.baseDamage = 0;
        this.spawnConditions = new EntitySpawnCondition[]{new ImpossibleSpawnCondition()};
        this.chance = 0;
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

    /**
     * Determine if an entity type is allowed to spawn from a custom entity spawner instance.
     *
     * @return
     */
    public boolean canBeSpawnerSpawned() {
        return switch (this) {
            case SPAWNER, TEST_SKELETON, TEST_ZOMBIE, REFORGE_NPC -> false;
            default -> true;
        };
    }

    public Material getInterfaceButton() {
        return switch (this) {
            case REFORGE_NPC -> Material.ANVIL;
            case CASTLE_DWELLER -> Material.WOODEN_SHOVEL;
            case UNDEAD_ARCHER -> Material.BOW;
            case SPAWNER -> Material.BARRIER;
            case TEST_ZOMBIE -> Material.ROTTEN_FLESH;
            case TEST_SKELETON -> Material.BONE;
            case WITHERED_SERAPH -> Material.NETHERITE_HOE;
            case MANSION_SPIDER -> Material.STRING;
            case WOODLAND_EXILE -> Material.CROSSBOW;
            case WOODLAND_BERSERKER -> Material.IRON_AXE;
            case FIERY_SYLPH -> Material.BLAZE_ROD;
            case PALACE_THUG -> Material.DIAMOND_BLOCK;
            default -> Material.SKELETON_SKULL;
        };
    }
}

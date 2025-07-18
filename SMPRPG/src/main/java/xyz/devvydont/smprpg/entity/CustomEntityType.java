package xyz.devvydont.smprpg.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.generator.structure.Structure;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.bosses.BlazeBoss;
import xyz.devvydont.smprpg.entity.creatures.*;
import xyz.devvydont.smprpg.entity.fishing.*;
import xyz.devvydont.smprpg.entity.npc.ReforgeNPC;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawnCondition;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawner;
import xyz.devvydont.smprpg.gui.base.IMenuDisplayable;
import xyz.devvydont.smprpg.services.EntityService;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

// Enums to use for the retrieval, storage, and statistics of "custom" entities.
public enum CustomEntityType implements IMenuDisplayable {

    // Mobs that spawn in castles.
    CASTLE_DWELLER(EntityType.ZOMBIE_VILLAGER, "Castle Dweller",
            15, 400, 50, CastleDweller::new),

    UNDEAD_ARCHER(EntityType.SKELETON, "Undead Archer",
            15, 350, 40, UndeadArcher::new),

    // Mobs that spawn in woodland mansions.
    MANSION_SPIDER(EntityType.SPIDER, "Mansion Spider",
            25, 800, 90,
            MansionSpider::new,
            EntitySpawnCondition.StructureSpawnCondition
                    .structure(Structure.MANSION)
                    .withChance(.20f)),

    WOODLAND_EXILE(EntityType.PILLAGER, "Woodland Exile",
            25, 1_100, 120,
            WoodlandExile::new,
            EntitySpawnCondition.StructureSpawnCondition
                    .structure(Structure.MANSION)
                    .withChance(.35f)),

    WOODLAND_BERSERKER(EntityType.VINDICATOR, "Woodland Berserker",
            25, 950, 140,
            WoodlandExile::new,
            EntitySpawnCondition.StructureSpawnCondition
                    .structure(Structure.MANSION)
                    .withChance(.35f)),

    PALACE_THUG(EntityType.WITHER_SKELETON, "Palace Thug",
            35, 2_500, 350,
            PalaceThug::new,
            EntitySpawnCondition.StructureSpawnCondition
                    .structure(Structure.MANSION)
                    .withChance(.10f)),

    FIERY_SYLPH(EntityType.BLAZE, "Fiery Sylph",
            35, 2_100, 300,
            FierySylph::new,
            EntitySpawnCondition.StructureSpawnCondition
                    .structure(Structure.FORTRESS)
                    .withChance(.10f)),

    PHOENIX(EntityType.BLAZE, "Phoenix",
            40, 5_000, 600,
            CustomEntityInstance::new),

    INFERNAL_PHOENIX(EntityType.BLAZE, "Infernal Phoenix",
            40, 750_000, 750, BlazeBoss::new),

    // Wither skeletons that spawn on the end island
    WITHERED_SERAPH(EntityType.WITHER_SKELETON, "Withered Seraph",
            45, 6_000, 550,
            WitheredSeraph::new,
            EntitySpawnCondition.BiomeSpawnCondition.biome(Biome.THE_END).withChance(.15f)),

    // Golems that spawn on the end island
    PROTOCOL_SENTINEL(EntityType.IRON_GOLEM, "Protocol Sentinel",
            45, 7_500, 400,
            ProtocolSentinel::new,
            EntitySpawnCondition.BiomeSpawnCondition.biome(Biome.THE_END).withChance(.15f)),

    // Fishing creatures.
    MINNOW(EntityType.SILVERFISH, "Minnow",
            5, 120, 10, Minnow::new),
    SNAPPING_TURTLE(EntityType.TURTLE, "Snapping Turtle",
            10, 200, 25, SnappingTurtle::new),
    SEA_BEAR(EntityType.POLAR_BEAR, "Sea Bear",
            20, 500, 45, SeaBear::new),

    CINDERLING(EntityType.MAGMA_CUBE, "Cinderling",
            20, 650, 50, Cinderling::new),

    ECHO_RAY(EntityType.PHANTOM, "Echo Ray",
            40, 3_500, 450, EchoRay::new),

    TEST_ZOMBIE(EntityType.ZOMBIE, "Test Zombie",
            5, 120, 15,
            TestZombie::new),

    TEST_SKELETON(EntityType.SKELETON, "Test Skeleton", 5, 100, 10),

    // NPCs
    REFORGE_NPC(EntityType.VILLAGER, "Tool Reforger", ReforgeNPC::new),

    // Spawner
    SPAWNER(EntityType.ITEM_DISPLAY, "Spawner", EntitySpawner.class)
    ;


    // The vanilla entity that this entity will display as and spawn as.
    public final EntityType Type;
    // The name of this entity
    public final String Name;
    // The "power level" that this entity is, affects default spawning level as well as base for scaling
    public final int Level;
    // The HP this entity will have when their level is at its base level
    public final int Hp;
    // The base damage this entity will do when their level is at its base level
    public final int Damage;
    public final EntitySpawnCondition SpawnCondition;

    // More of a compatibility thing for weird entities like spawners. Most entities shouldn't have this property.
    public  @Nullable Class<CustomEntityInstance<Entity>> rawClass = null;

    private final BiFunction<LivingEntity, CustomEntityType, LeveledEntity<?>> Factory;

    CustomEntityType(EntityType type, String name, int Level, int Hp, int Damage, BiFunction<LivingEntity, CustomEntityType, LeveledEntity<?>> factory, EntitySpawnCondition condition) {
        this.Type = type;
        this.Name = name;
        this.Level = Level;
        this.Hp = Hp;
        this.Damage = Damage;
        this.Factory = factory;
        this.SpawnCondition = condition;
    }

    CustomEntityType(EntityType type, String name, int Level, int Hp, int Damage, BiFunction<LivingEntity, CustomEntityType, LeveledEntity<?>> factory) {
        this(type, name, Level, Hp, Damage, factory, EntitySpawnCondition.ImpossibleSpawnCondition.create());
    }

    CustomEntityType(EntityType Type, String name, int Level, int Hp, int Damage) {
        this(Type, name, Level, Hp, Damage, CustomEntityInstance::new);
    }

    CustomEntityType(EntityType Type, String name, BiFunction<LivingEntity, CustomEntityType, LeveledEntity<?>> factory) {
        this(Type, name, 0, 999_999_999, 0, factory, EntitySpawnCondition.ImpossibleSpawnCondition.create());
    }

    CustomEntityType(EntityType entityType, String name, Class entitySpawnerClass) {
        this.Type = entityType;
        this.Name = name;
        this.Level = 0;
        this.Hp = 2_000_000_000;
        this.Damage = 0;
        this.Factory = null;
        this.SpawnCondition = EntitySpawnCondition.ImpossibleSpawnCondition.create();
        this.rawClass = entitySpawnerClass;
    }

    public LeveledEntity<?> create(LivingEntity entity) {
        return Factory.apply(entity, this);
    }

    /**
     * Creates an entity instance by using reflection since the LivingEntity factory is not available.
     * This is considered a pretty unsafe operation, so it should be used sparingly.
     * @param entity The entity to wrap over.
     * @return A wrapper instance.
     */
    public LeveledEntity<?> create(Entity entity) {
        if (this.rawClass == null)
            throw new IllegalStateException("Entity " + entity + " has no CustomEntityInstance class association. Are you using the right CustomEntityType constructor?");
        try {
            var constructor = this.rawClass.getConstructor(Entity.class, CustomEntityType.class);
            return constructor.newInstance(entity, this);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
        return entity.getPersistentDataContainer().getOrDefault(EntityService.getClassNamespacedKey(), PersistentDataType.STRING, "").equals(key());
    }

    public EntityType getType() {
        return Type;
    }

    public String getName() {
        return Name;
    }

    public int getLevel() {
        return Level;
    }

    public int getHp() {
        return Hp;
    }

    public int getDamage() {
        return Damage;
    }

    public boolean testNaturalSpawn(Location location) {
        return SpawnCondition.test(location);
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

    @Override
    public Material getDisplayMaterial() {
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
            case INFERNAL_PHOENIX -> Material.BLAZE_POWDER;
            case PHOENIX -> Material.FIRE_CHARGE;
            case SNAPPING_TURTLE -> Material.TURTLE_SCUTE;
            case SEA_BEAR -> Material.SALMON;
            case MINNOW -> Material.COD;
            default -> Material.SKELETON_SKULL;
        };
    }

}

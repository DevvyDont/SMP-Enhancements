package xyz.devvydont.smprpg.fishing.pools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.fishing.SeaBear;
import xyz.devvydont.smprpg.entity.fishing.SnappingTurtle;
import xyz.devvydont.smprpg.fishing.loot.FishingLootBase;
import xyz.devvydont.smprpg.fishing.loot.FishingLootType;
import xyz.devvydont.smprpg.fishing.loot.ItemStackFishingLoot;
import xyz.devvydont.smprpg.fishing.loot.SeaCreatureFishingLoot;
import xyz.devvydont.smprpg.fishing.loot.requirements.FishingLootRequirement;

import java.util.Collection;

/**
 * Statically contains all the fishing rewards that are possible to retrieve in the plugin.
 * Rewards are not meant to be dynamic, our plugin just needs some point to register what's available.
 * If you want something to be available in the fishing pool, add it here. Just make sure to include
 * any requirements you want to loot to have!
 */
public class FishingRewardRegistry {

    private static final Multimap<FishingLootType, FishingLootBase> REGISTRY;

    // Initializes the registry.
    static {

        ImmutableMultimap.Builder<FishingLootType, FishingLootBase> builder = ImmutableMultimap.builder();

        // Initialize the fish pool.
        builder.putAll(FishingLootType.FISH,

                new ItemStackFishingLoot.Builder(Material.COD)
                        .withWeight(1)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.GOLD_INGOT)
                        .withWeight(1)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.NETHERITE_INGOT)
                        .withWeight(1)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.NETHER_STAR)
                        .withWeight(1)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.DRAGON_EGG)
                        .build()

//                new ItemStackFishingLoot.Builder(Material.COD)
//                        .withWeight(25)
//                        .build(),
//
//                new ItemStackFishingLoot.Builder(Material.SALMON)
//                        .withWeight(10)
//                        .build(),
//
//                new ItemStackFishingLoot.Builder(Material.PUFFERFISH)
//                        .withWeight(5)
//                        .build(),
//
//                new ItemStackFishingLoot.Builder(Material.TROPICAL_FISH)
//                        .build()
        );

        // Initialize the treasure pool.
        builder.putAll(FishingLootType.TREASURE,

                new ItemStackFishingLoot.Builder(Material.SADDLE)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.NAUTILUS_SHELL)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.NAME_TAG)
                        .build()
        );

        // Initialize the sea creature pool.
        builder.putAll(FishingLootType.CREATURE,

                new SeaCreatureFishingLoot.Builder(CustomEntityType.MINNOW)
                        .build(),

                new SeaCreatureFishingLoot.Builder(CustomEntityType.SNAPPING_TURTLE)
                        .withRequirement(FishingLootRequirement.quality(SnappingTurtle.REQUIREMENT))
                        .build(),

                new SeaCreatureFishingLoot.Builder(CustomEntityType.SEA_BEAR)
                        .withRequirement(FishingLootRequirement.quality(SeaBear.REQUIREMENT))
                        .build()
        );

        // Initialize the junk pool.
        builder.putAll(FishingLootType.JUNK,

                new ItemStackFishingLoot.Builder(Material.LILY_PAD)
                        .withWeight(15)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.BOWL)
                        .withWeight(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.LEATHER)
                        .withWeight(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.LEATHER_BOOTS)
                        .withWeight(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.ROTTEN_FLESH)
                        .withWeight(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.STICK)
                        .withWeight(5)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.STRING)
                        .withWeight(5)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.POTION)
                        .withWeight(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.BONE)
                        .withWeight(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.TRIPWIRE_HOOK)
                        .withWeight(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.INK_SAC)
                        .build()
        );

        REGISTRY = builder.build();
    }

    public static Multimap<FishingLootType, FishingLootBase> getRegisteredRewards() {
        return REGISTRY;
    }

    public static Collection<FishingLootBase> getRegisteredRewards(FishingLootType type) {
        return REGISTRY.get(type);
    }

}

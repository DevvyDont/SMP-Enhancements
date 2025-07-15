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
import xyz.devvydont.smprpg.fishing.utils.TemperatureReading;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.interfaces.IFishingRod;

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

                // Start with fish that can be fished up anywhere. Introduction to the system essentially.
                new ItemStackFishingLoot.Builder(CustomItemType.COD)
                        .withWeight(10)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new ItemStackFishingLoot.Builder(CustomItemType.SALMON)
                        .withWeight(7)
                        .withMinecraftExperience(5)
                        .withSkillExperience(20)
                        .withRequirement(FishingLootRequirement.quality(10))
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new ItemStackFishingLoot.Builder(CustomItemType.PUFFERFISH)
                        .withWeight(3)
                        .withMinecraftExperience(10)
                        .withSkillExperience(30)
                        .withRequirement(FishingLootRequirement.quality(25))
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new ItemStackFishingLoot.Builder(CustomItemType.CLOWNFISH)
                        .withMinecraftExperience(20)
                        .withSkillExperience(100)
                        .withRequirement(FishingLootRequirement.quality(50))
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),


                new ItemStackFishingLoot.Builder(CustomItemType.BLISTERFISH)
                        .withMinecraftExperience(30)
                        .withSkillExperience(150)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.LAVA))
                        .build(),

                new ItemStackFishingLoot.Builder(CustomItemType.VOIDFIN)
                        .withMinecraftExperience(40)
                        .withSkillExperience(200)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.VOID))
                        .build()
        );

        // Initialize the treasure pool.
        builder.putAll(FishingLootType.TREASURE,

                new ItemStackFishingLoot.Builder(Material.SADDLE)
                        .withMinecraftExperience(50)
                        .withSkillExperience(100)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.DIAMOND)
                        .withMinecraftExperience(10)
                        .withSkillExperience(250)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.NAUTILUS_SHELL)
                        .withMinecraftExperience(50)
                        .withSkillExperience(500)
                        .withRequirement(FishingLootRequirement.quality(50))
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new ItemStackFishingLoot.Builder(Material.HEART_OF_THE_SEA)
                        .withRequirement(FishingLootRequirement.quality(100))
                        .withMinecraftExperience(50)
                        .withSkillExperience(750)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new ItemStackFishingLoot.Builder(Material.NETHERITE_INGOT)
                        .withMinecraftExperience(50)
                        .withSkillExperience(200)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.LAVA))
                        .build(),

                new ItemStackFishingLoot.Builder(Material.END_CRYSTAL)
                        .withMinecraftExperience(50)
                        .withSkillExperience(200)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.VOID))
                        .build()
        );

        // Initialize the sea creature pool.
        builder.putAll(FishingLootType.CREATURE,

                // The minnow can always be caught, no matter what. Can't leave a pool potentially empty.
                new SeaCreatureFishingLoot.Builder(CustomEntityType.MINNOW)
                        .withMinecraftExperience(10)
                        .withSkillExperience(50)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),


                new SeaCreatureFishingLoot.Builder(CustomEntityType.SNAPPING_TURTLE)
                        .withMinecraftExperience(25)
                        .withSkillExperience(200)
                        .withRequirement(FishingLootRequirement.quality(SnappingTurtle.REQUIREMENT))
                        .withRequirement(FishingLootRequirement.temperature(TemperatureReading.TEMPERATE))
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new SeaCreatureFishingLoot.Builder(CustomEntityType.SEA_BEAR)
                        .withMinecraftExperience(50)
                        .withSkillExperience(500)
                        .withRequirement(FishingLootRequirement.quality(SeaBear.REQUIREMENT))
                        .withRequirement(FishingLootRequirement.temperature(TemperatureReading.TEMPERATE))
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new SeaCreatureFishingLoot.Builder(CustomEntityType.CINDERLING)
                        .withMinecraftExperience(50)
                        .withSkillExperience(200)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.LAVA))
                        .build(),

                new SeaCreatureFishingLoot.Builder(CustomEntityType.ECHO_RAY)
                        .withMinecraftExperience(50)
                        .withSkillExperience(200)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.VOID))
                        .build()
        );

        // Initialize the junk pool.
        builder.putAll(FishingLootType.JUNK,

                new ItemStackFishingLoot.Builder(Material.LILY_PAD)
                        .withWeight(10)
                        .withMaximumAmount(5)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new ItemStackFishingLoot.Builder(Material.BOWL)
                        .withWeight(3)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new ItemStackFishingLoot.Builder(Material.LEATHER)
                        .withWeight(2)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.ROTTEN_FLESH)
                        .withWeight(3)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.STICK)
                        .withWeight(3)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.STRING)
                        .withWeight(2)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new ItemStackFishingLoot.Builder(Material.BONE)
                        .withWeight(2)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.TRIPWIRE_HOOK)
                        .withWeight(2)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .build(),

                new ItemStackFishingLoot.Builder(Material.INK_SAC)
                        .withMinecraftExperience(1)
                        .withSkillExperience(10)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.NORMAL))
                        .build(),

                new ItemStackFishingLoot.Builder(CustomItemType.PREMIUM_MAGMA_CREAM)
                        .withMinecraftExperience(3)
                        .withSkillExperience(20)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.LAVA))
                        .build(),

                new ItemStackFishingLoot.Builder(Material.GOLD_BLOCK)
                        .withMinecraftExperience(3)
                        .withSkillExperience(20)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.LAVA))
                        .build(),

                new ItemStackFishingLoot.Builder(CustomItemType.DRAGON_SCALES)
                        .withMinecraftExperience(5)
                        .withSkillExperience(30)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.LAVA))
                        .build(),

                new ItemStackFishingLoot.Builder(Material.ENDER_PEARL)
                        .withMinecraftExperience(5)
                        .withSkillExperience(30)
                        .withRequirement(FishingLootRequirement.rod(IFishingRod.FishingFlag.LAVA))
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

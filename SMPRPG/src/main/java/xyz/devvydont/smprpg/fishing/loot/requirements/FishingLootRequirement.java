package xyz.devvydont.smprpg.fishing.loot.requirements;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Biome;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.fishing.utils.TemperatureReading;
import xyz.devvydont.smprpg.items.interfaces.IFishingRod;

/**
 * Represents a condition that must be met given a fishing context.
 */
public interface FishingLootRequirement {

    /**
     * Checks if the given fishing context passes this requirement.
     * @return true if the context allows this loot to exist, false otherwise.
     */
    boolean passes(FishingContext context);

    /**
     * Displays this requirement as a component. Very useful if you want to lay out requirements in a GUI.
     * @return The component that represents this requirement.
     */
    Component display();

    /**
     * Returns a temperature requirement that passes if the given temperature equals the temperature of the context.
     * @param requirement The required temperature to pass.
     * @return The {@link FishingLootRequirement requirement.}
     */
    static FishingLootRequirement temperature(TemperatureReading requirement) {
        return new TemperatureRequirement(requirement);
    }

    /**
     * Returns a rod type requirement that passes if the given rod type is contained within the fishing flags.
     * @param requirement The required {@link xyz.devvydont.smprpg.items.interfaces.IFishingRod.FishingFlag} to pass.
     * @return The {@link FishingLootRequirement requirement.}
     */
    static FishingLootRequirement rod(IFishingRod.FishingFlag requirement) {
        return new RodRequirement(requirement);
    }

    /**
     * Returns a biome requirement that passes if the given biome equals the biome of the context.
     * @param biome The required biome to pass.
     * @return The {@link FishingLootRequirement requirement.}
     */
    static FishingLootRequirement biome(Biome biome) {
        return new BiomeRequirement(biome);
    }

    /**
     * Returns a fishing rating requirement that passes if the given rating is high enough given the rating of the ctx.
     * @param requirement The required fishing rating to pass.
     * @return The {@link FishingLootRequirement requirement.}
     */
    static FishingLootRequirement quality(int requirement) {
        return new CatchQualityRequirement(requirement);
    }
}

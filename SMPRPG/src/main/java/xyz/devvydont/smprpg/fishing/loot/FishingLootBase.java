package xyz.devvydont.smprpg.fishing.loot;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.fishing.loot.requirements.FishingLootRequirement;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents something that can be fished up. At a high level, this is essentially a wrapper over
 * an entity (creature/item).
 */
public abstract class FishingLootBase {

    private final int weight;
    private final int fishingExperience;
    private final int minecraftExperience;
    private final Collection<FishingLootRequirement> requirements;

    protected FishingLootBase(int weight, int fishingExperience, int minecraftExperience, Collection<FishingLootRequirement> requirements) {
        this.weight = weight;
        this.fishingExperience = fishingExperience;
        this.minecraftExperience = minecraftExperience;
        this.requirements = requirements;
    }

    /**
     * Generate the loot! Returns an entity that can be attached to the fishing hook to be reeled in.
     * @param ctx The {@link FishingContext} relevant to the generation of this loot.
     * @return An entity that is able to attach to a fishing rod hook.
     */
    public abstract @Nullable Entity generate(FishingContext ctx);

    /**
     * Get the fishing skill experience to award for catching this.
     * @return an int representing experience amount.
     */
    public int getFishingExperience() {
        return fishingExperience;
    }

    /**
     * Get the minecraft experience to award for catching this.
     * @return an int representing experience amount.
     */
    public int getMinecraftExperience() {
        return minecraftExperience;
    }

    /**
     * Get the requirements for this loot to be available.
     * @return A collection of requirements.
     */
    public Collection<FishingLootRequirement> getRequirements() {
        return requirements;
    }

    /**
     * Utility method to check if all the requirements that are present on this loot pass a context check.
     * @return True if the given {@link FishingContext} satisfies this loot's requirements.
     */
    public boolean passesAllRequirements(FishingContext ctx) {
        for (var requirement : requirements)
            if (!requirement.passes(ctx))
                return false;
        return true;
    }

    /**
     * Get the weight of this item when it is present in the pool. The higher this number, the more common it is.
     * @return The weight of this loot.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * A base loot builder that loot generators should inherit from. Provides all the common functionality that all
     * loot instances will have.
     * @param <T> The loot class.
     * @param <B> The loot builder class.
     */
    public static abstract class FishingLootBuilder<T extends FishingLootBase, B extends FishingLootBuilder<T, B>> {

        protected final List<FishingLootRequirement> requirements = new ArrayList<>();
        protected int experienceReward = 0;
        protected int minecraftExperience = 0;
        protected int weight = 1;

        @SuppressWarnings("unchecked")
        public B withRequirement(FishingLootRequirement req) {
            this.requirements.add(req);
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B withWeight(int weight) {
            this.weight = weight;
            return (B) this;
        }

        /**
         * Add fishing experience to this cast if we fish it up. Defaults to 0.
         * @param experience The fishing experience this loot awards.
         * @return Returns back the same instance for a builder pattern.
         */
        @SuppressWarnings("unchecked")
        public B withSkillExperience(int experience) {
            this.experienceReward = experience;
            return (B) this;
        }

        /**
         * Add vanilla minecraft experience to this cast if we fish it up. Defaults to 1.
         * @param experience The minecraft experience this loot awards.
         * @return Returns back the same instance for a builder pattern.
         */
        @SuppressWarnings("unchecked")
        public B withMinecraftExperience(int experience) {
            this.minecraftExperience = experience;
            return (B) this;
        }

        public abstract T build();
    }
}

package xyz.devvydont.smprpg.fishing.loot.requirements;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.items.interfaces.IFishingRod;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

/**
 * A fishing context requirement that passes if the rod flag given the context contains the flag needed.
 * @param requirement The flag to match.
 */
public record RodRequirement(IFishingRod.FishingFlag requirement) implements FishingLootRequirement {

    @Override
    public boolean passes(FishingContext context) {
        return context.getFlags().contains(requirement);
    }

    @Override
    public Component display() {
        return ComponentUtils.merge(ComponentUtils.create("Rod Type"), ComponentUtils.create(": "), ComponentUtils.create(requirement.Display, requirement.Color));
    }
}
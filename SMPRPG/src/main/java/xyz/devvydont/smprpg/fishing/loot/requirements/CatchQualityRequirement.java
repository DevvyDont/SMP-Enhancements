package xyz.devvydont.smprpg.fishing.loot.requirements;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public record CatchQualityRequirement(int requirement) implements FishingLootRequirement {

    @Override
    public boolean passes(FishingContext context) {
        return context.getCatchQuality() >= requirement;
    }

    @Override
    public Component display() {
        return ComponentUtils.merge(
                ComponentUtils.create(AttributeWrapper.FISHING_RATING.DisplayName, NamedTextColor.GOLD),
                ComponentUtils.create(" of "),
                ComponentUtils.create("" + requirement, NamedTextColor.GREEN)
        );
    }
}

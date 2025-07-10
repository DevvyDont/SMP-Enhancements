package xyz.devvydont.smprpg.fishing.loot.requirements;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.fishing.utils.TemperatureReading;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

/**
 * A fishing context requirement that passes if the location of the context has a matching temperature.
 * @param requirement The temperature to match.
 */
public record TemperatureRequirement(TemperatureReading requirement) implements FishingLootRequirement {

    @Override
    public boolean passes(FishingContext context) {
        return context.getTemperature().equals(requirement);
    }

    @Override
    public Component display() {
        return ComponentUtils.merge(ComponentUtils.create("Temperature of "), requirement.Component);
    }
}

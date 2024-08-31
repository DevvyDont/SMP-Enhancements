package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class DullReforge extends ReforgeBase {

    public DullReforge(ReforgeType type) {
        super(type);
    }

    public static float getSweepBoost(ItemRarity rarity) {
        return .04f * rarity.ordinal() + .20f;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new MultiplicativeAttributeEntry(AttributeWrapper.STRENGTH, -.15f),
                new AdditiveAttributeEntry(AttributeWrapper.SWEEPING, getSweepBoost(rarity))
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.getDefaultText("Decreases base damage but increases"),
                ComponentUtils.getDefaultText("effectiveness of sweeping attacks")
        );
    }

    @Override
    public int getPowerRating() {
        return 1;
    }
}

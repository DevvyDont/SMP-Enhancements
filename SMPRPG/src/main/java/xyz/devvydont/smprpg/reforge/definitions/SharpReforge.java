package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class SharpReforge extends ReforgeBase {

    public SharpReforge(ReforgeType type) {
        super(type);
    }

    public static float getDamageBonus(ItemRarity rarity) {
        return .01f * rarity.ordinal() + .05f;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, getDamageBonus(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, .01f)
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.create("Provides small damage boost")
        );
    }

    @Override
    public int getPowerRating() {
        return 1;
    }
}

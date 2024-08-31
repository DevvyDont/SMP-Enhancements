package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class SluggishReforge extends ReforgeBase {

    public static float getDamageBonus(ItemRarity rarity) {
        return .06f * rarity.ordinal() + .12f;
    }

    public SluggishReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new MultiplicativeAttributeEntry(AttributeWrapper.STRENGTH, getDamageBonus(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5f)
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.getDefaultText("Majorly decreases attack speed"),
                ComponentUtils.getDefaultText("for a moderate boost in damage")
        );
    }

    @Override
    public int getPowerRating() {
        return 1;
    }
}

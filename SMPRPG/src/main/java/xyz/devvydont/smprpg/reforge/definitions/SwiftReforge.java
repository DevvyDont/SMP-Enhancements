package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class SwiftReforge extends ReforgeBase {

    public static float getMovementSpeedBuff(ItemRarity rarity) {
        return rarity.ordinal() * .02f + .05f;
    }

    public static float getAttackSpeedBuff(ItemRarity rarity) {
        return rarity.ordinal() * .01f + .04f;
    }

    public SwiftReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, getMovementSpeedBuff(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, getAttackSpeedBuff(rarity))
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.create("Greatly increases movement speed"),
                ComponentUtils.create("with a small boost in attack speed")
        );
    }

    @Override
    public int getPowerRating() {
        return 1;
    }
}

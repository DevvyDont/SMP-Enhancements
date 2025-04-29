package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class PolishedReforge extends ReforgeBase {

    public PolishedReforge(ReforgeType type) {
        super(type);
    }

    public static int getDefenseBonus(ItemRarity rarity) {
        return (rarity.ordinal() + 1) * 2 / 2;
    }

    public static float getMovementSpeedBonus(ItemRarity rarity) {
        if (rarity.ordinal() >= ItemRarity.EPIC.ordinal())
            return .02f;
        return .01f;
    }

    public static float getStrengthBonus(ItemRarity rarity) {
        if (rarity.ordinal() >= ItemRarity.EPIC.ordinal())
            return .03f;
        return .02f;
    }


    public static float getLuckBonus(ItemRarity rarity) {
        if (rarity.ordinal() >= ItemRarity.EPIC.ordinal())
            return .02f;
        return .01f;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefenseBonus(rarity)),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getDefenseBonus(rarity)),
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, rarity.ordinal() >= ItemRarity.EPIC.ordinal() ? 3 : 2),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, getMovementSpeedBonus(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, getMovementSpeedBonus(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapper.STRENGTH, getStrengthBonus(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapper.LUCK, getLuckBonus(rarity))
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.create("Provides a small boost"),
                ComponentUtils.create("for all stats")

        );
    }

    @Override
    public int getPowerRating() {
        return 3;
    }
}

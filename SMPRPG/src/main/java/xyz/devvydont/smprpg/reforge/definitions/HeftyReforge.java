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

public class HeftyReforge extends ReforgeBase {

    public static float getKnockbackResist(ItemRarity rarity) {

        return switch (rarity) {
            case COMMON -> .14f;
            case UNCOMMON, RARE, EPIC -> .16f;
            case LEGENDARY, MYTHIC -> .19f;
            case DIVINE -> .24f;
            default -> .25f;
        };

    }

    public static float getSpeedDebuff(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> .03f;
            case UNCOMMON, RARE, EPIC -> .02f;
            case LEGENDARY, MYTHIC -> .02f;
            default -> .01f;
        };
    }

    public HeftyReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, getKnockbackResist(rarity)),
                new AdditiveAttributeEntry(AttributeWrapper.EXPLOSION_KNOCKBACK_RESISTANCE, getKnockbackResist(rarity)),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, -getSpeedDebuff(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -getSpeedDebuff(rarity) * 2),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, ((rarity.ordinal()+1) / 500.0))
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.getDefaultText("Provides a generous boost"),
                ComponentUtils.getDefaultText("to knockback resistance and strength"),
                ComponentUtils.getDefaultText("at the cost of general speed")
        );
    }

    @Override
    public int getPowerRating() {
        return 2;
    }

}

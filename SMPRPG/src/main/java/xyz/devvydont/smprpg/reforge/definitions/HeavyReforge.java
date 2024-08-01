package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.Collection;
import java.util.List;

public class HeavyReforge extends ReforgeBase {


    public static float getKnockbackResist(ItemRarity rarity) {

        return switch (rarity) {
            case COMMON -> .08f;
            case UNCOMMON, RARE, EPIC -> .10f;
            case LEGENDARY, MYTHIC -> .12f;
            case DIVINE -> .15f;
            default -> .11f;
        };

    }

    public static float getSpeedDebuff(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> .05f;
            case UNCOMMON, RARE, EPIC -> .03f;
            case LEGENDARY, MYTHIC -> .02f;
            default -> .01f;
        };
    }

    public HeavyReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, getKnockbackResist(rarity)),
                new AdditiveAttributeEntry(AttributeWrapper.EXPLOSION_KNOCKBACK_RESISTANCE, getKnockbackResist(rarity)),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, -getSpeedDebuff(rarity))
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtil.getDefaultText("Provides a moderate boost"),
                ComponentUtil.getDefaultText("to knockback resistance"),
                ComponentUtil.getDefaultText("at the cost of movement speed")
        );
    }

    @Override
    public int getPowerRating() {
        return 1;
    }

}

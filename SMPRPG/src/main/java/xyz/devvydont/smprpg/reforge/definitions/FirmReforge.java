package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class FirmReforge extends ReforgeBase {

    public static float getKnockbackResist(ItemRarity rarity) {

        return switch (rarity) {
            case COMMON -> .01f;
            case UNCOMMON, RARE, EPIC -> .02f;
            case LEGENDARY, MYTHIC -> .03f;
            case DIVINE -> .04f;
            default -> .05f;
        };

    }

    public FirmReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, getKnockbackResist(rarity)),
                new AdditiveAttributeEntry(AttributeWrapper.EXPLOSION_KNOCKBACK_RESISTANCE, getKnockbackResist(rarity))
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.getDefaultText("Provides a small boost"),
                ComponentUtils.getDefaultText("to knockback resistance")
        );
    }

    @Override
    public int getPowerRating() {
        return 1;
    }
}

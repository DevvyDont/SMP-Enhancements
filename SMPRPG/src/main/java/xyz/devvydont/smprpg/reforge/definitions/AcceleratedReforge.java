package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

public class AcceleratedReforge extends ReforgeBase {

    public static float getMovementSpeedBuff(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> .10f;
            case UNCOMMON -> .15f;
            case RARE -> .20f;
            case EPIC -> .30f;
            case LEGENDARY -> .40f;
            case MYTHIC -> .50f;
            case DIVINE -> .65f;
            case TRANSCENDENT -> .8f;
            case SPECIAL -> .95f;
        };
    }

    public AcceleratedReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.create("Provides a").append(ComponentUtils.create(" SIGNIFICANT", NamedTextColor.GOLD)),
                ComponentUtils.create("boost in ").append(ComponentUtils.create("movement/attack speed", NamedTextColor.WHITE))
        );
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, getMovementSpeedBuff(rarity)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, (rarity.ordinal()+1)*5 / 100.0)
        );
    }

    @Override
    public int getPowerRating() {
        return 4;
    }
}

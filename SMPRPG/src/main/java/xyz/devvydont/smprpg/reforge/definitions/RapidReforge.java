package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.Collection;
import java.util.List;

public class RapidReforge extends ReforgeBase {


    public RapidReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, .1 * rarity.ordinal() + .2)
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(ComponentUtil.getDefaultText("Greatly improves attack speed"));
    }

    @Override
    public int getPowerRating() {
        return 1;
    }
}

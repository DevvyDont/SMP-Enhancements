package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class UnimplementedReforge extends ReforgeBase {

    public UnimplementedReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public List<Component> getDescription() {
        return List.of(ComponentUtils.create("This reforge is not implemented", NamedTextColor.RED));
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, -1)
        );
    }

    @Override
    public int getPowerRating() {
        return 0;
    }
}

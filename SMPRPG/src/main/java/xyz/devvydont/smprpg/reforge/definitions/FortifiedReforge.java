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

public class FortifiedReforge extends ReforgeBase  {

    public FortifiedReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, (rarity.ordinal()+1)*10),
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 1)
        );
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.create("Provides a moderate boost"),
                ComponentUtils.create("to defense")
        );
    }

    @Override
    public int getPowerRating() {
        return 2;
    }

}

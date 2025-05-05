package xyz.devvydont.smprpg.items.blueprints.sets.snowfall;

import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapperLegacy;

import java.util.Collection;
import java.util.List;

public class SnowfallBoots extends SnowfallArmorSet {

    public SnowfallBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapperLegacy.DEFENSE, 355),
                new AdditiveAttributeEntry(AttributeWrapperLegacy.HEALTH, 250),
                new ScalarAttributeEntry(AttributeWrapperLegacy.STRENGTH, .45),
                new ScalarAttributeEntry(AttributeWrapperLegacy.LUCK, .25)
        );
    }



    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }
}

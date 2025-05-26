package xyz.devvydont.smprpg.items.blueprints.sets.snowfall;

import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class SnowfallBoots extends SnowfallArmorSet {

    public SnowfallBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 355),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 250),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .45),
                new ScalarAttributeEntry(AttributeWrapper.LUCK, .25)
        );
    }



    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }
}

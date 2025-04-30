package xyz.devvydont.smprpg.items.blueprints.sets.mystbloom;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class MystbloomBoots extends MystbloomArmorSet {

    public MystbloomBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 65),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 20),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .05),
                new AdditiveAttributeEntry(Attribute.SAFE_FALL_DISTANCE, 5)
        );
    }
}

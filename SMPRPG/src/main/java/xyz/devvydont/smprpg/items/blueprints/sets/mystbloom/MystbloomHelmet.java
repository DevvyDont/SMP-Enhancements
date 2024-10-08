package xyz.devvydont.smprpg.items.blueprints.sets.mystbloom;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class MystbloomHelmet extends MystbloomArmorSet {

    public MystbloomHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 45),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 10),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .1),
                new ScalarAttributeEntry(AttributeWrapper.LUCK, .1)
        );
    }
}

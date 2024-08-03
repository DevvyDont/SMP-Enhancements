package xyz.devvydont.smprpg.items.blueprints.sets.snowfall;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class SnowfallBoots extends SnowfallArmorSet {

    public SnowfallBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }
}

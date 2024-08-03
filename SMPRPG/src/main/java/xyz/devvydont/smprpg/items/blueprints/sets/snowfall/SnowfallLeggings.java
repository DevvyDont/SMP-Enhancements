package xyz.devvydont.smprpg.items.blueprints.sets.snowfall;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class SnowfallLeggings extends SnowfallArmorSet {

    public SnowfallLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }
}

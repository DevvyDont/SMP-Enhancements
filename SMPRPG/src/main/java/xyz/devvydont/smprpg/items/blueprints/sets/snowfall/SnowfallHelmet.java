package xyz.devvydont.smprpg.items.blueprints.sets.snowfall;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class SnowfallHelmet extends SnowfallArmorSet {

    public SnowfallHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }
}

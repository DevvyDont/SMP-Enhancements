package xyz.devvydont.smprpg.items.blueprints.sets.elderflame;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class ElderflameLeggings extends ElderflameArmorSet {

    public ElderflameLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }
}

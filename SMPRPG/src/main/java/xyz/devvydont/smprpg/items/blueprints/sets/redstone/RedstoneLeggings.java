package xyz.devvydont.smprpg.items.blueprints.sets.redstone;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class RedstoneLeggings extends RedstoneArmorSet {

    public RedstoneLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public int getDefense() {
        return 50;
    }
}

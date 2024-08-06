package xyz.devvydont.smprpg.items.blueprints.sets.redstone;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class RedstoneBoots extends RedstoneArmorSet {


    public RedstoneBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public int getDefense() {
        return 30;
    }
}

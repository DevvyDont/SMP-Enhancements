package xyz.devvydont.smprpg.items.blueprints.sets.emerald;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class EmeraldBoots extends EmeraldArmorSet {

    public EmeraldBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public int getDefense() {
        return 70;
    }

    @Override
    public int getHealth() {
        return 2;
    }
}

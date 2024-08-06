package xyz.devvydont.smprpg.items.blueprints.sets.emerald;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class EmeraldHelmet extends EmeraldArmorSet {

    public EmeraldHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public int getDefense() {
        return 90;
    }

    @Override
    public int getHealth() {
        return 5;
    }
}

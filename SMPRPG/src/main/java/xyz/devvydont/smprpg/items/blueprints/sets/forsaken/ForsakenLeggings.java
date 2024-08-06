package xyz.devvydont.smprpg.items.blueprints.sets.forsaken;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class ForsakenLeggings extends ForsakenArmorSet {

    public ForsakenLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 310;
    }

    @Override
    public int getHealth() {
        return 230;
    }

    @Override
    public double getStrength() {
        return .3;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.forsaken;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class ForsakenHelmet extends ForsakenArmorSet {

    public ForsakenHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 205;
    }

    @Override
    public int getHealth() {
        return 155;
    }

    @Override
    public double getStrength() {
        return .25;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.undead;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class UndeadHelmet extends UndeadArmorSet {

    public UndeadHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public int getDefense() {
        return 25;
    }
}

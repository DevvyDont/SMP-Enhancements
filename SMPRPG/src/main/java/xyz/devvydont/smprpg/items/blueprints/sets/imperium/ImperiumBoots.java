package xyz.devvydont.smprpg.items.blueprints.sets.imperium;

import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class ImperiumBoots extends ImperiumArmorSet {

    public ImperiumBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 145;
    }

    @Override
    public int getHealth() {
        return 100;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.RIB;
    }
}

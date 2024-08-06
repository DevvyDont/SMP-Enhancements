package xyz.devvydont.smprpg.items.blueprints.sets.unstable;

import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class UnstableLeggings extends UnstableArmorSet {

    public UnstableLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public double getStatMultiplier() {
        return .85;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.HOST;
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.unstable;

import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class UnstableHelmet extends UnstableArmorSet {

    public UnstableHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public double getStatMultiplier() {
        return .7;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.HOST;
    }
}

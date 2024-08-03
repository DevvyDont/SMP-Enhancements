package xyz.devvydont.smprpg.items.blueprints.sets.radiant;

import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class RadiantHelmet extends RadiantArmorSet {


    public RadiantHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.RAISER;
    }
}

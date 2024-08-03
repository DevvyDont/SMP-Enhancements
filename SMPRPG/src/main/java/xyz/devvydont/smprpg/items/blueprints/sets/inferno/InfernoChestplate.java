package xyz.devvydont.smprpg.items.blueprints.sets.inferno;

import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class InfernoChestplate extends InfernoArmorSet {

    public InfernoChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }
}

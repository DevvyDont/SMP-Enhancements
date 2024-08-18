package xyz.devvydont.smprpg.items.blueprints.resources;

import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;

public class SellableResource extends CustomItemBlueprint implements Sellable {

    public SellableResource(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public int getWorth() {
        return getCustomItemType().defaultWorth;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }
}

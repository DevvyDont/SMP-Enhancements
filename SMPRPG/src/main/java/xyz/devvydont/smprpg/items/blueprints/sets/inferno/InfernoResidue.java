package xyz.devvydont.smprpg.items.blueprints.sets.inferno;

import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;

public class InfernoResidue extends CustomItemBlueprint implements Sellable {

    public InfernoResidue(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public int getWorth() {
        return 10_000;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }
}

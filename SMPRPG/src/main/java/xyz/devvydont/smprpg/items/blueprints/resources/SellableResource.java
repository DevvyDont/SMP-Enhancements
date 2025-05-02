package xyz.devvydont.smprpg.items.blueprints.resources;

import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;

public class SellableResource extends CustomItemBlueprint implements ISellable {

    public SellableResource(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return getCustomItemType().defaultWorth * itemStack.getAmount();
    }
}

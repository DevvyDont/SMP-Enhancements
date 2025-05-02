package xyz.devvydont.smprpg.items.blueprints.food;

import io.papermc.paper.datacomponent.item.Consumable;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IEdible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;

public class PotatoChip extends CustomItemBlueprint implements IEdible, ISellable {

    public PotatoChip(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return 24 * itemStack.getAmount();
    }

    @Override
    public int getNutrition() {
        return 4;
    }

    @Override
    public float getSaturation() {
        return 2;
    }

    @Override
    public boolean canAlwaysEat() {
        return false;
    }

    @Override
    public Consumable getConsumableComponent() {
        return Consumable.consumable()
                .consumeSeconds(.8f)
                .build();
    }
}

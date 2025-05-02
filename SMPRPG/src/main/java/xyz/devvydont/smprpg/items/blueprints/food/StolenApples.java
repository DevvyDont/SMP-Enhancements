package xyz.devvydont.smprpg.items.blueprints.food;

import io.papermc.paper.datacomponent.item.Consumable;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IEdible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;

public class StolenApples extends CustomItemBlueprint implements IEdible, ISellable {

    public StolenApples(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public int getNutrition() {
        return 4;
    }

    @Override
    public float getSaturation() {
        return 4;
    }

    @Override
    public boolean canAlwaysEat() {
        return false;
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return 30 * itemStack.getAmount();
    }

    @Override
    public Consumable getConsumableComponent() {
        return Consumable.consumable()
                .consumeSeconds(1.5f)
                .build();
    }
}

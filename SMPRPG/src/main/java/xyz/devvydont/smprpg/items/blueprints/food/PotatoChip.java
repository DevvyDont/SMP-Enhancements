package xyz.devvydont.smprpg.items.blueprints.food;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Edible;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.FoodUtil;

public class PotatoChip extends CustomItemBlueprint implements Edible, Sellable {

    public PotatoChip(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public int getWorth() {
        return 24;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
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
}

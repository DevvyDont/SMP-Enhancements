package xyz.devvydont.smprpg.items.blueprints.food;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Edible;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.FoodUtil;

public class CottonCandy extends CustomItemBlueprint implements Edible, Sellable {

    public CottonCandy(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getWorth() {
        return 25;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
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
}

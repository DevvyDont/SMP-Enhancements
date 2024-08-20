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

public class ChiliPepper extends CustomItemBlueprint implements Edible, Sellable {

    public ChiliPepper(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public FoodComponent getFoodComponent() {
        FoodComponent food = FoodUtil.getVanillaFoodComponent(Material.GOLDEN_CARROT);
        food.setEatSeconds(.8f);
        food.setNutrition(2);
        food.setSaturation(6);
        food.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*45, 0, true, true), .2f);
        return food;
    }

    @Override
    public int getWorth() {
        return 30;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }
}

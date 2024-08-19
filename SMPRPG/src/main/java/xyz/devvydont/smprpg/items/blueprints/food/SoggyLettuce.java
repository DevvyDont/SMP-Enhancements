package xyz.devvydont.smprpg.items.blueprints.food;

import org.bukkit.Material;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Edible;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.FoodUtil;

public class SoggyLettuce extends CustomItemBlueprint implements Edible {

    public SoggyLettuce(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public FoodComponent getFoodComponent() {
        FoodComponent food = FoodUtil.getVanillaFoodComponent(Material.COOKIE);
        food.setEatSeconds(.8f);
        food.setNutrition(3);
        food.setSaturation(5);
        food.addEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 30*20, 0, true, true), .1f);
        return food;
    }
}

package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Edible;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;
import xyz.devvydont.smprpg.util.items.FoodUtil;

import java.util.List;

public class FleshFamilyBlueprint extends CustomCompressableBlueprint implements Edible {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.ROTTEN_FLESH)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_FLESH)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_FLESH))
    );

    public FleshFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }

    public FoodComponent getFoodComponent() {
        FoodComponent food = FoodUtil.getVanillaFoodComponent(Material.ROTTEN_FLESH);
        food.setCanAlwaysEat(true);

        if (getCustomItemType().equals(CustomItemType.PREMIUM_FLESH)) {
            food.setNutrition(food.getNutrition()+4);
            food.setSaturation(food.getSaturation()+4);
            food.setEatSeconds(food.getEatSeconds()+1);
            food.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*60, 0), 1);
            return food;
        }

        if (getCustomItemType().equals(CustomItemType.ENCHANTED_FLESH)) {
            food.setNutrition(food.getNutrition()+8);
            food.setSaturation(food.getSaturation()+8);
            food.setEatSeconds(food.getEatSeconds()+2);
            food.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*600, 1), 1);
            food.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*600, 0), 1);
        }

        return food;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

}

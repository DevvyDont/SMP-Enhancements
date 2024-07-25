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

public class PorkchopFamilyBlueprint extends CustomCompressableBlueprint implements Edible {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.COOKED_PORKCHOP)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_PORKCHOP)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_PORKCHOP))
    );

    public PorkchopFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }

    public FoodComponent getFoodComponent() {
        FoodComponent food = FoodUtil.getVanillaFoodComponent(Material.COOKED_PORKCHOP);
        food.setCanAlwaysEat(true);

        if (getCustomItemType().equals(CustomItemType.PREMIUM_PORKCHOP)) {
            food.setNutrition(food.getNutrition()+6);
            food.setSaturation(food.getSaturation()+6);
            food.setEatSeconds(food.getEatSeconds()+1);
            food.addEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*60, 1), 1);
            food.addEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*60, 1), 1);
            return food;
        }

        if (getCustomItemType().equals(CustomItemType.ENCHANTED_PORKCHOP)) {
            food.setNutrition(food.getNutrition()+12);
            food.setSaturation(food.getSaturation()+12);
            food.setEatSeconds(food.getEatSeconds()+2);
            food.addEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*600, 2), 1);
            food.addEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*600, 2), 1);
            food.addEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20*600, 0), 1);
        }

        return food;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }
    
}

package xyz.devvydont.smprpg.util.crafting.builders;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;

public class LeggingsRecipe extends SingleMaterialRecipe {

    public LeggingsRecipe(ICraftable blueprint, ItemStack material, ItemStack result) {
        super(blueprint, material, result);
    }

    @Override
    public void shape(ShapedRecipe recipe) {
        recipe.shape(
                String.format("%s%s%s", MATERIAL_KEY, MATERIAL_KEY, MATERIAL_KEY),
                String.format("%s %s", MATERIAL_KEY, MATERIAL_KEY),
                String.format("%s %s", MATERIAL_KEY, MATERIAL_KEY)
        );
    }

}

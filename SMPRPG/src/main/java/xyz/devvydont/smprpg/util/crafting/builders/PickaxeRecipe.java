package xyz.devvydont.smprpg.util.crafting.builders;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;

public class PickaxeRecipe extends ToolRecipe {

    public PickaxeRecipe(ICraftable blueprint, ItemStack material, ItemStack handle, ItemStack result) {
        super(blueprint, material, handle, result);
    }

    @Override
    public void shape(ShapedRecipe recipe) {
        recipe.shape(
                String.format("%s%s%s", MATERIAL_KEY, MATERIAL_KEY, MATERIAL_KEY),
                String.format(" %s ", HANDLE_KEY),
                String.format(" %s ", HANDLE_KEY)
        );
    }
}

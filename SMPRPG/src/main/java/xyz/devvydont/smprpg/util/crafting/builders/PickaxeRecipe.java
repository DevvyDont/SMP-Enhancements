package xyz.devvydont.smprpg.util.crafting.builders;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.devvydont.smprpg.items.interfaces.Craftable;

public class PickaxeRecipe extends BaseRecipe {

    public PickaxeRecipe(Craftable blueprint, ItemStack material, ItemStack handle, ItemStack result) {
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

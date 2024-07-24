package xyz.devvydont.smprpg.util.crafting.builders;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.devvydont.smprpg.items.interfaces.Craftable;

public class BowRecipe extends ToolRecipe {

    public BowRecipe(Craftable blueprint, ItemStack material, ItemStack handle, ItemStack result) {
        super(blueprint, material, handle, result);
    }

    @Override
    public void shape(ShapedRecipe recipe) {
        recipe.shape(
                String.format(" %s%s", MATERIAL_KEY, HANDLE_KEY),
                String.format("%s %s", MATERIAL_KEY, HANDLE_KEY),
                String.format(" %s%s", MATERIAL_KEY, HANDLE_KEY)
        );
    }
}

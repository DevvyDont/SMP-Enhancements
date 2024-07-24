package xyz.devvydont.smprpg.util.crafting.builders;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.items.interfaces.Craftable;

public abstract class SingleMaterialRecipe {

    private final ItemStack material;
    private final ItemStack result;
    private final NamespacedKey key;

    protected final char MATERIAL_KEY = 'M';

    public SingleMaterialRecipe(Craftable blueprint, ItemStack material, ItemStack result) {
        this.material = material;
        this.result = result;
        this.key = blueprint.getRecipeKey();
    }

    public ItemStack getMaterial() {
        return material;
    }

    public ItemStack getResult() {
        return result;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public abstract void shape(ShapedRecipe recipe);

    public CraftingRecipe build() {
        ShapedRecipe recipe = new ShapedRecipe(getKey(), getResult());
        this.shape(recipe);
        recipe.setIngredient(MATERIAL_KEY, getMaterial());
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

}

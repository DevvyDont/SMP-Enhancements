package xyz.devvydont.smprpg.util.crafting.builders;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;

public abstract class ToolRecipe {

    private final ItemStack handle;
    private final ItemStack material;
    private final ItemStack result;
    private final NamespacedKey key;

    protected final char MATERIAL_KEY = 'M';
    protected final char HANDLE_KEY = 'H';

    public ToolRecipe(ICraftable blueprint, ItemStack material, ItemStack handle, ItemStack result) {
        this.handle = handle;
        this.material = material;
        this.result = result;
        this.key = blueprint.getRecipeKey();
    }

    public ItemStack getHandle() {
        return handle;
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
        recipe.setIngredient(HANDLE_KEY, getHandle());
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }
}

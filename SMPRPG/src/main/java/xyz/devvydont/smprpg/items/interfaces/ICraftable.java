package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

/**
 * An interface for descendants of SMPItemBlueprint to implement for our plugin to detect that this blueprint
 * can be custom crafted.
 */
public interface ICraftable {

    NamespacedKey getRecipeKey();
    CraftingRecipe getCustomRecipe();

    /**
     * A collection of items that will unlock the recipe for this item. Typically will be one of the components
     * of the recipe itself, but can be set to whatever is desired
     *
     * @return
     */
    Collection<ItemStack> unlockedBy();

    /**
     * A simplistic implementation to allow a recipe to be viewable in contexts where you are checking against this
     * interface. This is specifically only meant to call the getCustomRecipe() method.
     * @param recipe The recipe to use.
     * @return An ICraftable instance.
     */
    static ICraftable withOnlyRecipe(CraftingRecipe recipe) {
        return new ICraftable() {
            @Override
            public NamespacedKey getRecipeKey() {
                return new NamespacedKey("smprpg", "dummy");
            }

            @Override
            public CraftingRecipe getCustomRecipe() {
                return recipe;
            }

            @Override
            public Collection<ItemStack> unlockedBy() {
                return List.of();
            }
        };
    }
}

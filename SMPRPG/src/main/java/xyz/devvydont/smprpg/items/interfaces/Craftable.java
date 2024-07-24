package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Collection;

/**
 * An interface for descendants of SMPItemBlueprint to implement for our plugin to detect that this blueprint
 * can be custom crafted.
 */
public interface Craftable {

    NamespacedKey getRecipeKey();
    CraftingRecipe getCustomRecipe();

    /**
     * A collection of items that will unlock the recipe for this item. Typically will be one of the components
     * of the recipe itself, but can be set to whatever is desired
     *
     * @return
     */
    Collection<ItemStack> unlockedBy();
}

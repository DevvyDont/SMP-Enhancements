package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.Recipe;

/**
 * An interface for descendants of SMPItemBlueprint to implement for our plugin to detect that this blueprint
 * can be custom crafted.
 */
public interface Craftable {

    NamespacedKey getRecipeKey();
    CraftingRecipe getCustomRecipe();

}

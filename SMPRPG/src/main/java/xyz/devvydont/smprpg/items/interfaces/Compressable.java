package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;

import java.util.Collection;
import java.util.List;

/**
 * When something is "compressable", it means that it not only has a crafting recipe, but this recipe is simply just
 * either 4 or 9 of ingredients of its neighbor in the chain of compression.
 */
public interface Compressable {

    /**
     * Retrieve a list of compression recipe members.
     * The order of the members of this list determine which materials lead into what when performing
     * compression crafting operations.
     *
     * @return
     */
    List<CompressionRecipeMember> getCompressionFlow();

    Collection<CraftingRecipe> registerCompressionChain();
    Collection<CraftingRecipe> registerDecompressionChain();

    Collection<NamespacedKey> getAllRecipeKeys();

}

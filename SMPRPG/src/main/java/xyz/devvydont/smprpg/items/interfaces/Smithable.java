package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.SmithingRecipe;

public interface Smithable {

    public NamespacedKey getSmithingKey();

    /**
     * Gets the smithing recipe required to make this item
     *
     * @return
     */
    public SmithingRecipe getSmithingRecipe();

}

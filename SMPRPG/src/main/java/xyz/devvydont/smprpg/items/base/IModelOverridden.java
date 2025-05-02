package xyz.devvydont.smprpg.items.base;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface IModelOverridden {

    /**
     * Get the material that this item should display as, regardless of what it actually is internally.
     * This allows you to change how an item looks without affecting its behavior.
     * @return The material this item should render as.
     */
    Material getDisplayMaterial();

    /**
     * Retrieve the model data of a material. Can be used as a shortcut for filling out this interface.
     * @param material The vanilla material you want model data of.
     * @return The key pointing to the model data of a material.
     */
    static Key ofMaterial(Material material) {
        return ItemStack.of(material).getData(DataComponentTypes.ITEM_MODEL);
    }
}

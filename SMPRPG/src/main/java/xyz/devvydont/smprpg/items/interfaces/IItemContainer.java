package xyz.devvydont.smprpg.items.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IItemContainer {

    /**
     * Get the desired amount of slots this container should have.
     * NOTE: This should not be a number higher than 256, as that is the maximum
     * that Minecraft allows.
     * @return How many slots are allowed to be stored in this container.
     */
    int getSlots();

    /**
     * Get the desired max stack size of items for the inventory.
     * Note that this is ignored in most scenarios if this is higher than
     * the default limit of items that are being stored in the container.
     * It is meant to be more of a limiting factor.
     * @return The maximum stack size in the container.
     */
    int getStackSize();

    /**
     * Gets the items currently stored on the container.
     * @param container The item that is storing other items.
     * @param includeEmpty Whether or not to include the air slots, so you can see the exact configuration of the container.
     * @return A list of items that are currently stored in the container.
     */
    List<ItemStack> getStoredItems(ItemStack container, boolean includeEmpty);

    /**
     * Applies a collection of items to be stored on an item.
     * @param container The item that is storing other items.
     * @param items The list of items to store.
     */
    void setStoredItems(ItemStack container, List<ItemStack> items);

    /**
     * Get the title of the menu when interacting with the interface of this container.
     * @return The component to render on the interface.
     */
    Component getInterfaceTitleComponent();
}

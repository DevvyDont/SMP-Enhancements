package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an item blueprint that is sellable for money/coins.
 */
public interface ISellable {

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    int getWorth(ItemStack item);

}

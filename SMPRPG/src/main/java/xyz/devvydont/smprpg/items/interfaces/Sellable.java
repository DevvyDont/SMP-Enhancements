package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents an item blueprint that is sellable for money/coins.
 */
public interface Sellable {

    /**
     * Retrieve the expected value of a clean version of this item with no modifications.
     *
     * @return
     */
    int getWorth();

    /**
     * Retrieve the value of this item but also look at item metadata and determine an alternate price.
     * An example would be increasing the value of the item by a certain amount for having certain enchantments.
     *
     * @param meta
     * @return
     */
    int getWorth(ItemMeta meta);

}

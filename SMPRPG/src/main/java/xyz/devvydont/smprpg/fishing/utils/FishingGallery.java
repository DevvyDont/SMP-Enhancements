package xyz.devvydont.smprpg.fishing.utils;

import org.bukkit.NamespacedKey;
import xyz.devvydont.smprpg.fishing.loot.FishingLootBase;
import xyz.devvydont.smprpg.items.ItemRarity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A collection of fish loot that a PDC holder has caught.
 * Simply an interface for storing/retrieving collection data.
 */
public class FishingGallery {

    private final Map<NamespacedKey, Integer> keyToTimesCaught = new HashMap<>();

    /**
     * Increments a loot type by 1.
     * @param loot The loot to increment.
     */
    public void increment(FishingLootBase loot) {
        this.set(loot, get(loot) + 1);
    }

    /**
     * Increments a loot type by 1 with a given rarity.
     * @param loot The loot to increment.
     * @param rarity The rarity to specify.
     */
    public void increment(FishingLootBase loot, ItemRarity rarity) {
        this.set(loot, rarity, get(loot, rarity) + 1);
    }

    /**
     * Sets the loot amount using the raw key. Used for deserialization.
     * Use {@link FishingGallery#set(FishingLootBase, int)} if you have the loot instance.
     * @param key The key to use.
     */
    public void set(NamespacedKey key, int times) {
        keyToTimesCaught.put(key, times);
    }

    /**
     * Sets the loot amount.
     * @param loot The loot to set.
     */
    public void set(FishingLootBase loot, int times) {
        keyToTimesCaught.put(loot.getKey(), times);
    }

    /**
     * Sets the loot amount with the given rarity.
     * @param loot The loot to set.
     * @param rarity The rarity to specify.
     */
    public void set(FishingLootBase loot, ItemRarity rarity, int times) {
        keyToTimesCaught.put(loot.getKey(rarity), times);
    }

    /**
     * Gets the amount of times a certain loot was caught. Keep in mind, that this does not do any rarity checking.
     * This means that if you store a loot item based on rarity, this will not track it.
     * Use {@link FishingGallery#total(FishingLootBase)} if you wish to calculate a running total!
     * @param loot The loot to query.
     */
    public int get(FishingLootBase loot) {
        return keyToTimesCaught.getOrDefault(loot.getKey(), 0);
    }

    /**
     * Gets the amount of times a certain loot was caught with a specific rarity.
     * @param loot The loot to query.
     * @param rarity The rarity to query.
     *
     */
    public int get(FishingLootBase loot, ItemRarity rarity) {
        return keyToTimesCaught.getOrDefault(loot.getKey(rarity), 0);
    }

    /**
     * Calculates the total times caught regardless of rarity. This will work with any fishing loot item.
     * @param loot The loot to query.
     * @return The amount of times caught.
     */
    public int total(FishingLootBase loot) {
        var total = 0;
        total += get(loot);
        for (var rarity : ItemRarity.values())
            total += get(loot, rarity);
        return total;
    }

    /**
     * Returns the underlying entries of {@link Map} values. Useful for PDC serialization.
     * @return The raw entries in this gallery.
     */
    public Set<Map.Entry<NamespacedKey, Integer>> entries() {
        return keyToTimesCaught.entrySet();
    }


}

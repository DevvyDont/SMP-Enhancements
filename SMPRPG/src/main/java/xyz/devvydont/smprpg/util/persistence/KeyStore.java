package xyz.devvydont.smprpg.util.persistence;

import org.bukkit.NamespacedKey;

/**
 * Stores various common {@link org.bukkit.NamespacedKey} globals to make code less spaghetti.
 * There is no general pattern to what keys can be stored here, if it feels right to be global then put it here.
 * Keep in mind that changing key namespaces and values will break some things that used the prior key.
 */
public class KeyStore {

    /**
     * The namespace to use when generating keys. You should never change this or else you will break the plugin.
     */
    private static final String NAMESPACE = "smprpg";

    /**
     * Creates a new namespaced key using the given value. This key will ensure that data accessed/stored will be
     * consistent across plugin restarts, assuming the namespace (or key values) never change.
     * @param value The value of the key.
     * @return A {@link NamespacedKey} instance.
     */
    private static NamespacedKey key(String value) {
        return new NamespacedKey(NAMESPACE, value);
    }

    /**
     * Used for item rarity adjustments in item blueprints.
     */
    public static final NamespacedKey ITEM_RARITY_OVERRIDE = key("item_rarity_override");

    /**
     * Used for nerfing fishing attributes when dual wielding fishing rods.
     */
    public static final NamespacedKey FISHING_ATTRIBUTE_DUAL_WIELD_NERF = key("rod_dual_wield_nerf");

}

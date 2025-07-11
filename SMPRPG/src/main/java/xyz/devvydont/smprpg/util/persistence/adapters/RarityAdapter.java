package xyz.devvydont.smprpg.util.persistence.adapters;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.items.ItemRarity;

/**
 * Allows you to easily store {@link xyz.devvydont.smprpg.items.ItemRarity} on PDCs.
 */
public class RarityAdapter implements PersistentDataType<Integer, ItemRarity> {

    @Override
    public @NotNull Class<Integer> getPrimitiveType() {
        return Integer.class;
    }

    @Override
    public @NotNull Class<ItemRarity> getComplexType() {
        return ItemRarity.class;
    }

    @Override
    public @NotNull Integer toPrimitive(@NotNull ItemRarity complex, @NotNull PersistentDataAdapterContext context) {
        return complex.ordinal();
    }

    @Override
    public @NotNull ItemRarity fromPrimitive(@NotNull Integer primitive, @NotNull PersistentDataAdapterContext context) {
        if (primitive < 0 || primitive > ItemRarity.values().length)
            return ItemRarity.COMMON;
        return ItemRarity.values()[primitive];
    }
}

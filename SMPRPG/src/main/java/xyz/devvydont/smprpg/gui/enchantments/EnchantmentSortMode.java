package xyz.devvydont.smprpg.gui.enchantments;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * Helper enum for the EnchantmentMenu. Used to define different sorting modes for CustomEnchantment collections.
 */
public enum EnchantmentSortMode {

    DEFAULT,      // The order that enchantments render on items based on our curated order.
    REQUIREMENT,  // The magic skill requirement for the enchantment to be rolled.
    ALPHABETICAL  // Self-explanatory.

    ;

    /**
     * Gets a string to display to the user when this is the "selected mode".
     * @return A string representation of this enum.
     */
    public String display() {
        return MinecraftStringUtils.getTitledString(this.name());
    }

    /**
     * Performs an in place sort on a collection of enchantments depending on which mode this is if possible. (The DEFAULT sorting mode does NOT perform an in place sort.)
     * The sorted collection will also be returned, and is preferred to be used when calling this method.
     * @param enchantments A collection of enchantments to be sorted.
     * @return A sorted collection of enchantments.
     */
    public List<CustomEnchantment> sort(List<CustomEnchantment> enchantments) {
        switch (this) {
            case DEFAULT:
                return new ArrayList<>(SMPRPG.getService(EnchantmentService.class).getCustomEnchantments());

            case REQUIREMENT:
                enchantments.sort((e1, e2) -> Comparator.comparingInt(CustomEnchantment::getSkillRequirement).compare(e1, e2));
                return enchantments;

            case ALPHABETICAL:
                Comparator<CustomEnchantment> comparator = (e1, e2) -> {
                    String str1 = PlainTextComponentSerializer.plainText().serialize(e1.getDisplayName());
                    String str2 = PlainTextComponentSerializer.plainText().serialize(e2.getDisplayName());
                    int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                    if (res == 0)
                        res = str1.compareTo(str2);
                    return res;
                };
                enchantments.sort(comparator);
                return enchantments;

        }
        throw new IllegalStateException("Missing sort mode for " + this);
    }

    /**
     * Retrieve the next sort mode after this one. If this is the last, use the first one.
     * @return A new enchantment sort mode enum
     */
    public EnchantmentSortMode next() {
        int desiredModeOrdinal = this.ordinal() + 1;
        if (desiredModeOrdinal >= EnchantmentSortMode.values().length)
            desiredModeOrdinal = 0;
        return EnchantmentSortMode.values()[desiredModeOrdinal];
    }
}
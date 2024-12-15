package xyz.devvydont.treasureitems.util;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

public class PotentialEnchantmentWrapper {

    public static final float GUARANTEED_CHANCE = 1.0f;


    Enchantment enchantment;
    int minLevel;
    int maxLevel;
    float chance;

    public PotentialEnchantmentWrapper(Enchantment enchantment, int minLevel, int maxLevel, float chance) {
        this.enchantment = enchantment;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.chance = chance;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public float getChance() {
        return chance;
    }

    public String display() {
        if (enchantment.getStartLevel() == enchantment.getMaxLevel())
            return String.format("%s %s(%s)", FormatUtil.enchantFriendlyName(enchantment) , ChatColor.DARK_GRAY, (int)(chance*100) + "%");
        else
            return String.format("%s %s-%s %s(%s)", FormatUtil.enchantFriendlyName(enchantment) , minLevel, maxLevel, ChatColor.DARK_GRAY, (int)(chance*100) + "%");
    }
}

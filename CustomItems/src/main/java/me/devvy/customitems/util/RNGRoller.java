package me.devvy.customitems.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class RNGRoller {

    public static final float LUCK_BOOST = 1f;  // For every level of luck how much should we boost the chance?
    public static final float ENCHANTMENT_BOOST = 0.5f;  // For every level of enchant how much should we boost the chance?

    private Player player;
    private float chance;

    private Enchantment enchantToBoost;

    public RNGRoller(Player player, float chance, Enchantment enchantToBoost) {
        this.player = player;
        this.chance = chance;
        this.enchantToBoost = enchantToBoost;
    }

    public float getBaseChance() {
        return chance;
    }

    public float getChance() {
        return chance * getLuckMultiplier();
    }

    public float getLuckMultiplier() {
        return (float) (1 + (player.getAttribute(Attribute.GENERIC_LUCK).getValue() * LUCK_BOOST) + getEnchantLevels() * ENCHANTMENT_BOOST);
    }

    public boolean roll() {
        return Math.random() <= getChance();
    }

    public String getLuckMultiplierString() {
        return String.format("%.2fx", getLuckMultiplier());
    }

    public String getPercentOdds(boolean showLuck) {
        return String.format("%.3f", (showLuck ? getChance() : getBaseChance()) * 100) + "%";
    }

    public String getRatioOdds(boolean showLuck) {
        return "~1 in " + String.format("%d", Math.round(1 / (showLuck ? getChance() : getBaseChance())));
    }

    public int getEnchantLevels() {
        return player.getInventory().getItemInMainHand().getEnchantmentLevel(enchantToBoost) + player.getInventory().getItemInOffHand().getEnchantmentLevel(enchantToBoost);
    }
}

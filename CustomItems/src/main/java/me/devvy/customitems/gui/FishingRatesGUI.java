package me.devvy.customitems.gui;

import me.devvy.customitems.listeners.FishingItems;
import me.devvy.customitems.util.RNGRoller;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class FishingRatesGUI extends RatesViewerGUI {



    @Override
    public Inventory construct(Player player) {
        Inventory inventory = super.construct(player);

        RNGRoller rng = new RNGRoller(player, FishingItems.BASE_CHANCE, Enchantment.LUCK);

        inventory.setItem(2*9+4, createButton(ChatColor.GOLD + "Fishing",
                Material.FISHING_ROD,
                "",
                ChatColor.GRAY + "Every time you " + ChatColor.AQUA + "catch a fish " + ChatColor.GRAY + "there is",
                ChatColor.GRAY + "a chance it will be a " + ChatColor.LIGHT_PURPLE + "treasure item!",
                "",
                ChatColor.DARK_GRAY + "Player Luck Stat: " + ChatColor.DARK_PURPLE + player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_LUCK).getValue(),
                ChatColor.DARK_GRAY + "Luck of the Sea:  " + ChatColor.DARK_PURPLE + rng.getEnchantLevels(),
                ChatColor.GRAY + "Current fishing luck boost: " + ChatColor.GREEN + rng.getLuckMultiplierString(),
                "",

                ChatColor.GRAY + "Treasure item rates from fishing:",
                ChatColor.GRAY + "- Base chance: " + ChatColor.YELLOW + rng.getRatioOdds(false) + ChatColor.DARK_GRAY + " (" + rng.getPercentOdds(false) + ")",
                ChatColor.GRAY + "- Your chance: " + ChatColor.GREEN + rng.getRatioOdds(true) + ChatColor.DARK_GRAY + " (" + rng.getPercentOdds(true) + ")"
                ));

        return inventory;
    }


}

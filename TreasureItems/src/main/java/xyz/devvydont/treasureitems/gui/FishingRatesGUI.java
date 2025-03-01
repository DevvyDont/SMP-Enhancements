package xyz.devvydont.treasureitems.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import xyz.devvydont.treasureitems.listeners.FishingItems;
import xyz.devvydont.treasureitems.util.ComponentUtils;
import xyz.devvydont.treasureitems.util.RNGRoller;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class FishingRatesGUI extends RatesViewerGUI {



    @Override
    public Inventory construct(Player player) {
        Inventory inventory = super.construct(player);

        RNGRoller rng = new RNGRoller(player, FishingItems.BASE_CHANCE, Enchantment.LUCK_OF_THE_SEA);

        inventory.setItem(2*9+4, createButton(ChatColor.GOLD + "Fishing",
                Material.FISHING_ROD,
                Component.empty(),
                ComponentUtils.merge(Component.text("Every time you ", NamedTextColor.GRAY), Component.text("catch a fish ", NamedTextColor.AQUA), Component.text("there is", NamedTextColor.GRAY)),
                ComponentUtils.merge(Component.text("a chance it will be a ", NamedTextColor.GRAY), Component.text("treasure item!", NamedTextColor.LIGHT_PURPLE)),
                Component.empty(),
                ComponentUtils.merge(Component.text("Player Luck Stat: ", NamedTextColor.DARK_GRAY), Component.text(player.getAttribute(Attribute.LUCK).getValue(), NamedTextColor.DARK_PURPLE)),
                ComponentUtils.merge(Component.text("Luck of the Sea: ", NamedTextColor.DARK_GRAY), Component.text(rng.getEnchantLevels(), NamedTextColor.DARK_PURPLE)),
                ComponentUtils.merge(Component.text("Current fishing luck boost: ", NamedTextColor.DARK_GRAY), Component.text(rng.getLuckMultiplierString(), NamedTextColor.DARK_PURPLE)),
                Component.empty(),
                Component.text("Treasure item rates from fishing:", NamedTextColor.GRAY),
                ComponentUtils.merge(Component.text("- Base chance: ", NamedTextColor.GRAY), Component.text(rng.getRatioOdds(false), NamedTextColor.YELLOW), Component.text(" (" + rng.getPercentOdds(false) + ")", NamedTextColor.DARK_GRAY)),
                ComponentUtils.merge(Component.text("- Your chance: ", NamedTextColor.GRAY), Component.text(rng.getRatioOdds(true), NamedTextColor.GREEN), Component.text(" (" + rng.getPercentOdds(true) + ")", NamedTextColor.DARK_GRAY))
                ));

        return inventory;
    }


}

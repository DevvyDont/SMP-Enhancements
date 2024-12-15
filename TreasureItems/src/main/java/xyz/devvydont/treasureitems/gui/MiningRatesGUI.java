package xyz.devvydont.treasureitems.gui;

import org.bukkit.attribute.Attribute;
import xyz.devvydont.treasureitems.listeners.OreMineListeners;
import xyz.devvydont.treasureitems.util.FormatUtil;
import xyz.devvydont.treasureitems.util.RNGRoller;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MiningRatesGUI extends RatesViewerGUI {

    public ItemStack getOreDisplayItem(Player player, Material material) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        RNGRoller rng = new RNGRoller(player, OreMineListeners.getBaseDropChance(material), Enchantment.FORTUNE);

        meta.setDisplayName(ChatColor.GOLD + FormatUtil.getTitledString(material.name().toLowerCase().replace("_", " ")));
        meta.setLore(Arrays.asList(
                        "",
                        ChatColor.GRAY + "Every time you " + ChatColor.AQUA + "mine this ore " + ChatColor.GRAY + "there is",
                        ChatColor.GRAY + "a chance it will drop a " + ChatColor.LIGHT_PURPLE + "treasure item!",
                        "",
                        ChatColor.DARK_GRAY + "Luck Stat: " + ChatColor.DARK_PURPLE + player.getAttribute(Attribute.LUCK).getValue(),
                        ChatColor.DARK_GRAY + "Fortune:   " + ChatColor.DARK_PURPLE + rng.getEnchantLevels(),
                        ChatColor.GRAY + "Current mining luck boost: " + ChatColor.GREEN + rng.getLuckMultiplierString(),
                        "",

                        ChatColor.GRAY + "Treasure item rates from mining:",
                        ChatColor.GRAY + "- Base chance: " + ChatColor.YELLOW + rng.getRatioOdds(false) + ChatColor.DARK_GRAY + " (" + rng.getPercentOdds(false) + ")",
                        ChatColor.GRAY + "- Your chance: " + ChatColor.GREEN + rng.getRatioOdds(true) + ChatColor.DARK_GRAY + " (" + rng.getPercentOdds(true) + ")"
        ));

        item.setItemMeta(meta);
        return item;
    }


    @Override
    public Inventory construct(Player player) {
        Inventory inventory = super.construct(player);

        // Loop through every block in the game, and add a button if there is a chance for it to drop a treasure item
        for (Material material : Material.values()) {
            if (OreMineListeners.getBaseDropChance(material) <= 0 )
                continue;

            inventory.addItem(getOreDisplayItem(player, material));
        }

        return inventory;
    }


}

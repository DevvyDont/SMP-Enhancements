package me.devvy.smpenhancements.items;

import me.devvy.smpenhancements.SMPEnhancements;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ItemUtil {

    public static final NamespacedKey DEAD_DROP_KEY = new NamespacedKey(SMPEnhancements.getInstance(), "deaddrop");
    public static final NamespacedKey DEAD_DROP_NAME = new NamespacedKey(SMPEnhancements.getInstance(), "deaddropname");

    public static boolean isDeadDrop(ItemStack itemStack) {

        if (!itemStack.hasItemMeta())
            return false;

        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(DEAD_DROP_KEY, PersistentDataType.BOOLEAN, false);
    }

    public static void setItemStackFlag(ItemStack item, boolean flag, String playerName) {
        ItemMeta itemStackMeta = item.getItemMeta();

        if (flag) {
            assert playerName != null;
            itemStackMeta.getPersistentDataContainer().set(DEAD_DROP_KEY, PersistentDataType.BOOLEAN, flag);
            itemStackMeta.getPersistentDataContainer().set(DEAD_DROP_NAME, PersistentDataType.STRING, playerName);
        }
        else {
            itemStackMeta.getPersistentDataContainer().remove(DEAD_DROP_KEY);
            itemStackMeta.getPersistentDataContainer().remove(DEAD_DROP_NAME);
        }

        item.setItemMeta(itemStackMeta);
    }

    public static String getOwnerName(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(DEAD_DROP_NAME, PersistentDataType.STRING, "Unknown Player");
    }

    public static void addItemEntityAttributes(Item item) {
        // Infinite time, glow, name
        item.setUnlimitedLifetime(true);
        item.setGlowing(true);
        item.setCanMobPickup(false);
        item.setInvulnerable(true);

        String playerName = getOwnerName(item.getItemStack());

        // Set the display name of the item
        item.customName(
                Component.text(playerName + "'s ", TextColor.color(0, 200, 200))  // Name + 's
                        .append(item.getItemStack().displayName()) // Item name
                        .append(Component.text(" from death", TextColor.color( 180, 180, 180)))
        );
        item.setCustomNameVisible(true);
    }

    public static ItemStack generateDeathPaper(Player player) {

        String x, y, z;
        Location loc = player.getLastDeathLocation();
        assert loc != null;
        x = " " + loc.getBlockX();
        y = " " + loc.getBlockY();
        z = " " + loc.getBlockZ();

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta im = paper.getItemMeta();
        im.setDisplayName(ChatColor.RED + player.getName() + "'s Death Certificate");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        im.setLore(Arrays.asList("", ChatColor.GRAY + "Death recorded at:" + ChatColor.AQUA + x + y + z, "", ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Generated at " + formatter.format(date) + " EST"));
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        paper.setItemMeta(im);
        return paper;
    }

}

package me.devvy.deathdrops.items;

import me.devvy.deathdrops.DeathDrops;
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
import java.util.regex.Pattern;

public class ItemUtil {

    public static final NamespacedKey DEAD_DROP_KEY = new NamespacedKey(DeathDrops.getInstance(), "deaddrop");
    public static final NamespacedKey DEAD_DROP_NAME = new NamespacedKey(DeathDrops.getInstance(), "deaddropname");

    public static boolean isDeadDrop(ItemStack itemStack) {

        if (itemStack == null)
            return false;

        if (!itemStack.hasItemMeta())
            return false;

        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(DEAD_DROP_KEY, PersistentDataType.BOOLEAN, false);
    }

    public static void setItemStackFlag(ItemStack item, boolean flag, String playerName) {

        // Happens if we passed in air or some inventory slot that was null
        if (item == null)
            return;

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

    public static String upperCaseAllFirstCharacter(String text) {
        String regex = "\\b(.)(.*?)\\b";
        String result = Pattern.compile(regex).matcher(text).replaceAll(
                matche -> matche.group(1).toUpperCase() + matche.group(2)
        );

    return result;
    }

    public static void addItemEntityAttributes(Item item) {
        // Infinite time, glow, name
        item.setUnlimitedLifetime(true);
        item.setGlowing(true);
        item.setInvulnerable(true);

        String playerName = getOwnerName(item.getItemStack());

        // Set the display name of the item
        String itemname = item.getItemStack().getItemMeta().hasDisplayName() ? item.getItemStack().getItemMeta().getDisplayName() : ChatColor.GRAY + upperCaseAllFirstCharacter(item.getItemStack().getType().name().replace("_", " ").toLowerCase());
        item.setCustomName(ChatColor.AQUA + playerName + "'s " + itemname + ChatColor.DARK_GRAY + ChatColor.ITALIC + " from death");
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

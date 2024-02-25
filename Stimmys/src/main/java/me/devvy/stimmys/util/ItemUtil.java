package me.devvy.stimmys.util;

import me.devvy.stimmys.Stimmys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Map;

public class ItemUtil {

    private static NamespacedKey STIMMY_ITEM_KEY = new NamespacedKey(Stimmys.getInstance(), "stimmy-item-flag");

    private static final Material STIMMY_MATERIAL = Material.ECHO_SHARD;

    public static ItemStack getStimmyItem(int amount) {

        ItemStack is = new ItemStack(STIMMY_MATERIAL, amount);
        ItemMeta meta = is.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.displayName(Component.text("Stimmies", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        meta.lore(Arrays.asList(
                Component.empty(),
                Component.text("Currency for the stimmy shop!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("Access using ", NamedTextColor.GRAY).append(Component.text("/stimmy shop", NamedTextColor.GREEN, TextDecoration.BOLD)).decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Obtained by participating in events!", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC)
        ));
        meta.getPersistentDataContainer().set(STIMMY_ITEM_KEY, PersistentDataType.INTEGER, 1);

        is.setItemMeta(meta);
        return is;
    }

    public static boolean isStimmyItem(ItemStack itemStack) {
        return (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(STIMMY_ITEM_KEY));
    }

    public static int getNumStimmies(PlayerInventory playerInventory) {
        int total = 0;
        for (ItemStack is : playerInventory.getContents())
            if (is != null && isStimmyItem(is))
                total += is.getAmount();

        return total;
    }

    public static int removeNumStimmies(PlayerInventory playerInventory, int amount) throws IllegalStateException {

        if (getNumStimmies(playerInventory) < amount)
            throw new IllegalStateException("You do not have enough stimmies!");

        int toRemove = amount;
        int numRemoved = 0;

        for (ItemStack is : playerInventory.getContents()) {

            if (toRemove <= 0)
                break;

            if (is == null)
                continue;

            if (!isStimmyItem(is))
                continue;

            // If the amount we have to remove is less than the stack, set the stack to the difference
            // Otherwise empty the stack and go to the next one
            int newStackAmount;
            if (toRemove < is.getAmount()) {
                newStackAmount = is.getAmount() - toRemove;
                toRemove = 0;
            }
            else {
                newStackAmount = 0;
                toRemove -= is.getAmount();
            }

            int oldAmount = is.getAmount();
            is.setAmount(newStackAmount);
            numRemoved += oldAmount - is.getAmount();
        }

        if (numRemoved != amount)
            throw new IllegalStateException("You do not have enough stimmies!");

        return numRemoved;
    }

    public static void addItemToInventoryOverflowSafe(Player player, ItemStack is) {
        Map<Integer, ItemStack> overflow = player.getInventory().addItem(is);
        for (ItemStack overflowItem : overflow.values())
            player.getWorld().dropItemNaturally(player.getEyeLocation(), overflowItem).setGlowing(true);
    }

}

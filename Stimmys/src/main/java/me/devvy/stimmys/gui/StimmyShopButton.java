package me.devvy.stimmys.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class StimmyShopButton {

    private ItemStack itemToBuy;
    int cost;

    int x;
    int y;

    public StimmyShopButton(ItemStack itemToBuy, int cost, int x, int y) {
        this.itemToBuy = itemToBuy;
        this.cost = cost;
        this.x = x;
        this.y = y;
    }

    public ItemStack getItemToBuy() {
        return itemToBuy.clone();
    }

    public int getCost() {
        return cost;
    }

    public ItemStack displayItem() {
        ItemStack newItem = itemToBuy.clone();
        ItemMeta meta = newItem.getItemMeta();

        meta.displayName(
                Component.text(cost + "© ", NamedTextColor.DARK_AQUA).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false)
                        .append(getItemToBuy().displayName())
        );

        meta.lore(Arrays.asList(
                Component.empty(),
                Component.text("Click to buy!", NamedTextColor.GREEN)
        ));

        newItem.setItemMeta(meta);
        return newItem;
    }

    public int slot() {
        return x + (y * 9);
    }
}

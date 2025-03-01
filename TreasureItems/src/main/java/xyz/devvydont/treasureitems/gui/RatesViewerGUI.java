package xyz.devvydont.treasureitems.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.treasureitems.TreasureItems;
import xyz.devvydont.treasureitems.util.ComponentUtils;

import java.util.*;

public abstract class RatesViewerGUI implements Listener {

    public static final int BACK_SLOT = 4*9 + 7;
    public static final int CANCEL_SLOT = 4*9 + 8;

    public static String TITLE = ChatColor.GOLD + "Treasure Items";

    Map<UUID, Inventory> playerToGUI;

    public RatesViewerGUI() {

        playerToGUI = new HashMap<>();

        // Register for events
        TreasureItems.getInstance().getServer().getPluginManager().registerEvents(this, TreasureItems.getInstance());
    }

    public void open(Player player) {
        Inventory inv = construct(player);
        player.openInventory(inv);
        playerToGUI.put(player.getUniqueId(), inv);
    }

    public ItemStack createButton(String name, org.bukkit.Material material, Component... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.lore(ComponentUtils.removeItalics(lore));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ARMOR_TRIM);
        item.setItemMeta(meta);
        return item;
    }

    public Inventory construct(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 5*9, TITLE);
        inventory.setItem(BACK_SLOT, createButton(ChatColor.YELLOW + "Go back", Material.ARROW, Component.text("Click to go back to the main menu!", NamedTextColor.GRAY)));
        inventory.setItem(CANCEL_SLOT, createButton(ChatColor.RED + "Close", Material.BARRIER, Component.text("Click to close this menu!", NamedTextColor.GRAY)));
        return inventory;
    }

    public void handleClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player))
            return;

        switch (event.getSlot()) {
            case BACK_SLOT:
                TreasureItems.getInstance().getGuiManager().getRatesMainMenuGUI().open((Player) event.getWhoClicked());
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK, 1f, .85f);
                break;

            case CANCEL_SLOT:
                event.getWhoClicked().closeInventory();
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK, 1f, .65f);
                break;

            default:
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.25f);

        }
    }

    public void handleClose(InventoryCloseEvent event) {

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;

        if (!event.getInventory().equals(playerToGUI.get(event.getWhoClicked().getUniqueId())))
            return;

        event.setCancelled(true);
        handleClick(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (!event.getInventory().equals(playerToGUI.get(event.getPlayer().getUniqueId())))
            return;

        handleClose(event);
        playerToGUI.remove(event.getPlayer().getUniqueId());
    }
}

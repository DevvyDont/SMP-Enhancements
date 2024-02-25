package me.devvy.stimmys.gui;

import me.devvy.stimmys.Stimmys;
import me.devvy.stimmys.events.AttemptOpenStimmyShop;
import me.devvy.stimmys.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StimmyShopGUI implements Listener {

    private Inventory inventory;

    private List<StimmyShopButton> buttons = new ArrayList<>();

    public StimmyShopGUI() {
        Stimmys.getInstance().getServer().getPluginManager().registerEvents(this, Stimmys.getInstance());
        inventory = Bukkit.createInventory(null, 54, Component.text("Stimmy Shop", NamedTextColor.GOLD, TextDecoration.BOLD));
        constructButtons();
        updateInterface();
    }

    public void constructButtons() {
        buttons.clear();
        buttons.add(new StimmyShopButton(new ItemStack(Material.IRON_INGOT, 1), 2, 1, 1));
        buttons.add(new StimmyShopButton(new ItemStack(Material.GOLD_INGOT, 1), 2, 2, 1));
        buttons.add(new StimmyShopButton(new ItemStack(Material.LAPIS_LAZULI, 1), 1, 3, 1));
        buttons.add(new StimmyShopButton(new ItemStack(Material.EMERALD, 1), 3, 4, 1));
        buttons.add(new StimmyShopButton(new ItemStack(Material.DIAMOND, 1), 5, 5, 1));
        buttons.add(new StimmyShopButton(new ItemStack(Material.NETHERITE_INGOT, 1), 30, 6, 1));
        buttons.add(new StimmyShopButton(new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1), 50, 7, 1));

        buttons.add(new StimmyShopButton(new ItemStack(Material.COAL, 1), 1, 1, 2));
        buttons.add(new StimmyShopButton(new ItemStack(Material.FLINT, 1), 1, 2, 2));
        buttons.add(new StimmyShopButton(new ItemStack(Material.COPPER_INGOT, 1), 1, 3, 2));

        buttons.add(new StimmyShopButton(new ItemStack(Material.SHULKER_SHELL, 1), 7, 5, 2));
        buttons.add(new StimmyShopButton(new ItemStack(Material.SPECTRAL_ARROW, 64), 3, 6, 2));
        buttons.add(new StimmyShopButton(new ItemStack(Material.ARROW, 64), 3, 7, 2));

        buttons.add(new StimmyShopButton(new ItemStack(Material.BREAD, 64), 2, 1, 3));
        buttons.add(new StimmyShopButton(new ItemStack(Material.COOKED_BEEF, 64), 4, 2, 3));
        buttons.add(new StimmyShopButton(new ItemStack(Material.EXPERIENCE_BOTTLE, 64), 10, 3, 3));

        buttons.add(new StimmyShopButton(new ItemStack(Material.ENDER_PEARL, 16), 5, 5, 3));
        buttons.add(new StimmyShopButton(new ItemStack(Material.HEART_OF_THE_SEA, 1), 15, 6, 3));
        buttons.add(new StimmyShopButton(new ItemStack(Material.NAUTILUS_SHELL, 1), 7, 7, 3));

        buttons.add(new StimmyShopButton(new ItemStack(Material.GOLDEN_APPLE, 3), 15, 1, 4));
        buttons.add(new StimmyShopButton(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1), 50, 2, 4));
        buttons.add(new StimmyShopButton(new ItemStack(Material.GOLDEN_CARROT, 64), 20, 3, 4));

        buttons.add(new StimmyShopButton(new ItemStack(Material.NETHER_STAR, 1), 100, 5, 4));
        buttons.add(new StimmyShopButton(new ItemStack(Material.ELYTRA, 1), 70, 6, 4));
        buttons.add(new StimmyShopButton(new ItemStack(Material.TOTEM_OF_UNDYING, 1), 40, 7, 4));
    }

    public void updateInterface() {
        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        for (StimmyShopButton button : buttons)
            inventory.setItem(button.slot(), button.displayItem());
    }

    public StimmyShopButton getClickedButton(int slot) {
        for (StimmyShopButton button : buttons)
            if (button.slot() == slot)
                return button;

        return null;
    }

    public void open(Player p) {

        AttemptOpenStimmyShop openShopEvent = new AttemptOpenStimmyShop(p);
        openShopEvent.callEvent();
        if (openShopEvent.isCancelled()) {
            p.sendMessage(Component.text(openShopEvent.getReason(), NamedTextColor.RED));
            return;
        }

        p.openInventory(inventory);
    }

    @EventHandler
    public void onOpenShop(InventoryOpenEvent event) {
        if (event.getInventory().equals(inventory))
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_BELL_USE, 1, 3f);
    }

    @EventHandler
    public void onCloseShop(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_BELL_USE, 1, 1.8f);
    }

    @EventHandler
    public void onClickedInShop(InventoryClickEvent event) {

        // If a click happened while this inventory was open, cancel whatever the action was, otherwise ignore this event
        if (inventory.getViewers().contains(event.getWhoClicked()))
            event.setCancelled(true);
        else
            return;

        // Only listen to normal left clicks
        if (!event.getAction().equals(InventoryAction.PICKUP_ALL))
            return;

        if (event.getClickedInventory() == null)
            return;

        if (!event.getClickedInventory().equals(inventory))
            return;

        StimmyShopButton clickedButton = getClickedButton(event.getSlot());
        if (clickedButton == null)
            return;

        int balance = ItemUtil.getNumStimmies(event.getWhoClicked().getInventory());
        if (balance < clickedButton.getCost()) {
            event.getWhoClicked().sendMessage(Component.text("You cannot afford that!", NamedTextColor.RED));
            event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        try {
            ItemUtil.removeNumStimmies(event.getWhoClicked().getInventory(), clickedButton.getCost());
        } catch (IllegalStateException e) {
            event.getWhoClicked().sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
            event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        ItemUtil.addItemToInventoryOverflowSafe((Player) event.getWhoClicked(), clickedButton.getItemToBuy());

        event.getWhoClicked().sendMessage(Component.text("Purchase successful!", NamedTextColor.GREEN));
        event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
        event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
    }

    public void cleanup() {
        for (HumanEntity human : inventory.getViewers())
            human.closeInventory();
    }
}

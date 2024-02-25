package me.devvy.customitems.gui;

import me.devvy.customitems.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class RatesMainMenuGUI extends RatesViewerGUI {

    public static final int FISHING_GUI_SLOT = 1*9 + 2;
    public static final int MINING_GUI_SLOT = 1*9 + 4;
    public static final int MOBS_GUI_SLOT = 1*9 + 6;
    public static final int ITEMS_GUI_SLOT = 3*9 + 3;

    public static final int TRADEUP_GUI_SLOT = 3*9 + 5;

    public static String TITLE = ChatColor.GOLD + "Treasure Items";


    @Override
    public Inventory construct(Player player) {

        Inventory inventory = super.construct(player);
        // This is the main menu we don't need to show the back button
        inventory.setItem(BACK_SLOT, null);

        inventory.setItem(MINING_GUI_SLOT, createButton(ChatColor.GOLD + "Mining", Material.IRON_PICKAXE, ChatColor.GRAY + "Click to view mining rates!"));
        inventory.setItem(MOBS_GUI_SLOT, createButton(ChatColor.GOLD + "Mobs", Material.ZOMBIE_HEAD, ChatColor.GRAY + "Click to view mob rates!"));
        inventory.setItem(FISHING_GUI_SLOT, createButton(ChatColor.GOLD + "Fishing", Material.FISHING_ROD, ChatColor.GRAY + "Click to view fishing rates!"));
        inventory.setItem(ITEMS_GUI_SLOT, createButton(ChatColor.LIGHT_PURPLE + "Treasure", Material.TOTEM_OF_UNDYING, ChatColor.GRAY + "Click to view treasure items!"));
        inventory.setItem(TRADEUP_GUI_SLOT, createButton(ChatColor.AQUA + "Tradeup", Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, ChatColor.GRAY + "Click to view re-roll treasure items!"));
        inventory.getItem(ITEMS_GUI_SLOT).addUnsafeEnchantment(Enchantment.MENDING, 1);
        inventory.getItem(TRADEUP_GUI_SLOT).addUnsafeEnchantment(Enchantment.MENDING, 1);

        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event) {

        super.handleClick(event);

        switch (event.getSlot()) {

            case MINING_GUI_SLOT:
                CustomItems.getInstance().getGuiManager().getMiningRatesGUI().open((Player) event.getWhoClicked());
                break;
            case MOBS_GUI_SLOT:
                CustomItems.getInstance().getGuiManager().getMobRatesGUI().open((Player) event.getWhoClicked());
                break;
            case FISHING_GUI_SLOT:
                CustomItems.getInstance().getGuiManager().getFishingRatesGUI().open((Player) event.getWhoClicked());
                break;

            case ITEMS_GUI_SLOT:
                CustomItems.getInstance().getGuiManager().getCustomItemViewer().open((Player) event.getWhoClicked());
                break;

            case TRADEUP_GUI_SLOT:
                CustomItems.getInstance().getGuiManager().getTradeupGUI().open((Player) event.getWhoClicked());
                break;


        }

    }
}

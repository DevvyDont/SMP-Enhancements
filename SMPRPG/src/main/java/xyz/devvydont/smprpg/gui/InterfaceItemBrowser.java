package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class InterfaceItemBrowser extends PrivateInterface {

    private final String query;
    private final List<CustomItemType> results;
    private int page = 0;

    private static final int PREV_BUTTON = 45;
    private static final int NEXT_BUTTON = 53;
    private static final int CLOSE_BUTTON = 49;


    public InterfaceItemBrowser(SMPRPG plugin, Player owner, String query) {
        super(plugin, owner);
        this.query = query;
        this.results = new ArrayList<>();
        queryCustomItemTypes();
    }

    public boolean isDisplaySlot(int i) {
        if (i <= 9 || i >= inventory.getSize() - 9)
            return false;

        return i % 9 != 0 && (i + 1) % 9 != 0;
    }

    public int getNumDisplaySlots() {
        int n = 0;
        for (int i = 0; i < inventory.getSize(); i++)
            if (isDisplaySlot(i))
                n++;
        return n;
    }

    public int getNumPages() {
        return results.size() / getNumDisplaySlots() + 1;
    }

    public List<CustomItemType> queryCustomItemTypes() {
        results.clear();
        for (CustomItemType customItemType : CustomItemType.values())
            if (customItemType.name.toLowerCase().contains(query.toLowerCase()))
                results.add(customItemType);
        return results;
    }

    public void clearDisplayItems() {
        for (int i = 0; i < inventory.getSize(); i++)
            if (isDisplaySlot(i))
                inventory.setItem(i, null);
    }

    public void fillDisplayItems() {

        clearDisplayItems();

        if (page < 0)
            page = getNumPages()-1;
        if (page >= getNumPages())
            page = 0;

        List<CustomItemType> toDisplay = queryCustomItemTypes();
        if (toDisplay.isEmpty()) {
            inventoryView.setTitle("Nothing to display!");
            return;
        }

        int itemIndex = getNumDisplaySlots() * page;
        int invIndex = 0;

        while (itemIndex < toDisplay.size() && invIndex < inventory.getSize()) {

            if (!isDisplaySlot(invIndex)) {
                invIndex++;
                continue;
            }

            SMPItemBlueprint blueprint = plugin.getItemService().getBlueprint(toDisplay.get(itemIndex));
            ItemStack item = blueprint.generate();
            if (blueprint instanceof Craftable)
                item.editMeta(meta -> {
                    List<Component> lore = meta.lore();
                    lore.addFirst(ComponentUtils.create("Click to view recipe!", NamedTextColor.YELLOW));
                    lore.addFirst(ComponentUtils.EMPTY);
                    meta.lore(lore);
                });
            inventory.setItem(invIndex, item);
            invIndex++;
            itemIndex++;
        }

        inventoryView.setTitle("Item Directory | Page " + (page + 1) + "/" + getNumPages());
    }

    public void addPageButtons() {
        inventory.setItem(PREV_BUTTON, InterfaceUtil.getNamedItem(Material.ARROW, ComponentUtils.create("Previous", NamedTextColor.GOLD)));
        inventory.setItem(NEXT_BUTTON, InterfaceUtil.getNamedItem(Material.ARROW, ComponentUtils.create("Next", NamedTextColor.GOLD)));
    }

    public void addCloseButton() {
        inventory.setItem(CLOSE_BUTTON, InterfaceUtil.getNamedItem(Material.BARRIER, ComponentUtils.create("Close", NamedTextColor.RED)));
    }

    @Override
    public void initializeDefaultState() {
        inventoryView.setTitle(ChatColor.BLACK + "Item Directory");
        inventory.clear();
        border(InterfaceUtil.getInterfaceBorder());
        fillDisplayItems();
        addPageButtons();
        addCloseButton();
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {

        // Since this is a pure button clicking GUI, never accept any inventory modification events
        event.setCancelled(true);

        // If our custom inventory wasn't clicked, don't do anything
        if (!inventory.equals(event.getClickedInventory()))
            return;

        if (event.getCurrentItem() == null)
            return;

        // Handle button events
        if (event.getSlot() == PREV_BUTTON) {
            page--;
            fillDisplayItems();
            owner.playSound(owner.getEyeLocation(), Sound.UI_BUTTON_CLICK, 1, 1.5f);
            return;
        }

        if (event.getSlot() == NEXT_BUTTON) {
            page++;
            fillDisplayItems();
            owner.playSound(owner.getEyeLocation(), Sound.UI_BUTTON_CLICK, 1, 1.5f);
            return;
        }

        if (event.getSlot() == CLOSE_BUTTON) {
            inventory.close();
            owner.playSound(owner.getEyeLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1f);
            return;
        }

        if (!isDisplaySlot(event.getSlot()))
            return;

        // Handle the item click, for things that can be crafted we should show the recipe
        SMPItemBlueprint potentialItemClick = plugin.getItemService().getBlueprint(event.getCurrentItem());

        // If the player is in creative mode give it to them, otherwise attempt to find the recipe and display it
        if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))
            event.getWhoClicked().getInventory().addItem(potentialItemClick.generate());
        else if (potentialItemClick instanceof Craftable craftable) {
            InterfaceRecipe recipeViewer = new InterfaceRecipe(plugin, owner);
            recipeViewer.open();
            recipeViewer.showRecipe(craftable);
        }

        owner.playSound(owner.getEyeLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1f);
    }


}

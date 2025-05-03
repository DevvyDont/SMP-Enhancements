package xyz.devvydont.smprpg.gui.items;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.items.interfaces.IItemContainer;
import xyz.devvydont.smprpg.services.ItemService;

public class MenuContainer extends MenuBase {

    private final IItemContainer blueprint;
    private final ItemStack backpack;

    private int currentPage;

    // The inventory slots that can be used to interact with items. Other slots are reserved for buttons and decoration.
    private static final int[] INVENTORY_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };

    private static final int PAGE_CAPACITY = INVENTORY_SLOTS.length;

    public MenuContainer(@NotNull Player player, IItemContainer blueprint, ItemStack backpack) {
        super(player, 6);
        this.blueprint = blueprint;
        this.backpack = backpack;
        this.currentPage = 0;
        this.sounds.setMenuOpen(Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, .5f);
        this.sounds.setMenuClose(Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, .5f);
        this.sounds.setPageNext(Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, .8f);
        this.sounds.setPageNext(Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1.3f);
        render();
    }

    public void render() {

        this.setBorderFull();

        // Clone all the items stored in the meta to this inventory.
        // Once we do this, this inventory becomes the source of truth!
        var items = blueprint.getStoredItems(backpack, true);

        // Check if this page is out of bounds.
        var lastPage = getLastPage();
        if (currentPage > lastPage)
            currentPage = 0;
        if (currentPage < 0)
            currentPage = lastPage;

        // We only need to query for items that fall in the range of the subarray that we are actually looking at.
        // There's a lot of iterating variables going on here, but we have:
        // - itemIndex: the index of the item stored on the container that is offset from i.
        // - i: the index of the item in the item stored.
        var itemIndex = 0;
        for (var i = currentPage * PAGE_CAPACITY; i < blueprint.getSlots(); i++) {

            // If we ran out of space for this page, stop.
            if (i >= (currentPage + 1) * PAGE_CAPACITY)
                break;

            // If we ran out of items, stop.
            if (i >= items.size())
                break;

            // Place the item.
            this.setSlot(INVENTORY_SLOTS[itemIndex], items.get(i));
            itemIndex++;
        }

        // Now add some buttons. It is important these don't interfere with the storage.
        this.setBackButton(49);
        var page = "(" + (currentPage+1) + "/" + (lastPage+1) + ")";
        this.setButton(53, createNamedItem(Material.ARROW, "Next Page " + page), e -> changePage(1));
        this.setButton(45, createNamedItem(Material.ARROW, "Previous Page " + page), e -> changePage(-1));
    }

    public void savePage() {

        // Query the items so we can replace them.
        var items = blueprint.getStoredItems(backpack, true);

        // Whatever is present in our interface is going to replace the items we just retrieved.
        var containerIndex = currentPage * PAGE_CAPACITY;
        for (var interfaceSlotIndex : INVENTORY_SLOTS) {

            // If we are out of bounds of the container, we should stop.
            if (containerIndex >= blueprint.getSlots())
                break;

            // Whatever is stored in the interface should replace the item in the list.
            var itemInInterface = getItem(interfaceSlotIndex);
            if (itemInInterface == null)
                itemInInterface = ItemStack.of(Material.AIR);

            items.set(containerIndex, itemInInterface);
            containerIndex++;
        }

        // Items have been replaced for the subsection we care about. We can update the items.
        blueprint.setStoredItems(backpack, items);
    }

    public void changePage(int pageDelta) {

        // Save the page before changing it.
        savePage();

        // Change the page and re-render the menu.
        currentPage += pageDelta;
        render();
    }

    public int getLastPage() {
        return (blueprint.getSlots() - 1) / PAGE_CAPACITY;
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {

        // Under any circumstances, NEVER let any other backpacks (or gui elements) be clicked or modified.
        var clicked = event.getCurrentItem();
        if (clicked == null)
            return;

        if (ItemService.blueprint(clicked) instanceof IItemContainer
                || clicked.getType().equals(Material.BLACK_STAINED_GLASS_PANE)
                || clicked.getType().equals(Material.RED_STAINED_GLASS_PANE)
                || clicked.getType().equals(Material.GREEN_STAINED_GLASS_PANE)) {
            event.setCancelled(true);
            this.playInvalidAnimation();
        }
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(blueprint.getInterfaceTitleComponent());
        event.getInventory().setMaxStackSize(blueprint.getStackSize());
    }

    @Override
    protected void handleInventoryClosed(InventoryCloseEvent event) {
        // When this inventory closes, our inventory is the source of truth, so we should copy everything we have over.
        this.savePage();
    }
}

package xyz.devvydont.smprpg.gui.items;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * Functions as a server side version of J/NEI. Players can view this interface to view custom items and their recipes.
 */
public class MenuItemBrowser extends MenuBase {

    public static final int ROWS = 6;

    private String query;
    private final List<ItemStack> queriedItems;
    private int page = 0;

    /**
     * Default constructor. Used for general queries when we want to just display every single item in the game.
     *
     * @param player The player who wants to view items
     */
    public MenuItemBrowser(@NotNull Player player) {
        this(player, "");
    }

    /**
     * Alternative constructor if the user is querying for a specific item. We only want to display items that have
     * some sort of matching string pattern with their query.
     *
     * @param player The player who wants to view items
     * @param query The string query the player provided within the command
     */
    public MenuItemBrowser(@NotNull Player player, String query) {
        super(player, ROWS);
        this.query = query;
        this.queriedItems = new ArrayList<>();
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        super.handleInventoryOpened(event);
        this.queryItems();
        this.render();
        event.titleOverride(ComponentUtils.create("Item Directory: " + (query.isEmpty() ? "All Items" : query), NamedTextColor.BLACK));
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        super.handleInventoryClicked(event);
        event.setCancelled(true);
        this.playInvalidAnimation();
    }

    /**
     * Retrieve the query that is currently being used for this display.
     *
     * @return A string representing what the user input via command.
     */
    public String getQuery() {
        return query;
    }

    /**
     * A query is empty if the user didn't define something to search for. Determine if we are actually querying
     * for something specific or not
     *
     * @return true if a query is set, false if we are just viewing everything
     */
    public boolean hasQuery() {
        return query != null && !query.isEmpty();
    }

    /**
     * Use our currently defined query to return a fresh new list of items that we should render on the interface.
     * This only needs to be called when the query is either first set, or when we update the query.
     * After calling this method, the items can be viewed either via state or return value, as they will be the same list.
     *
     * @return A list of item stacks representing items that we should render on the display.
     */
    private List<ItemStack> queryItems() {

        // First, we can throw out our old item query since we are overwriting it anyway.
        this.queriedItems.clear();

        // Do we not have a query and just want to show everything? If that is the case we can return one of everything.
        if (!hasQuery()) {
            for (CustomItemType type : CustomItemType.values())
                this.queriedItems.add(ItemService.getItem(type));
            return queriedItems;
        }

        // When querying for items, we want to make the process as painless as possible, ignore spaces underscores etc.
        // todo use regex (i dont know it >_<) also capture more character patterns that may be present in item names
        String simpleQuery = query.toLowerCase().replace(" ", "").replace("_", "").replace("-", "");

        // Loop through every custom item in the game and see if the query makes this something of interest.
        for (CustomItemType itemType : CustomItemType.values()) {

            String simpleName = itemType.getName().toLowerCase();
            ItemStack item = ItemService.getItem(itemType);

            // Blacklisting process, does this item's name not contain any similar character patterns as the query?
            if (!simpleName.contains(simpleQuery))
                continue;

            // Valid!
            this.queriedItems.add(item);
        }

        return queriedItems;
    }

    /**
     * Determine what to do when a certain item stack is clicked. For example, if a craftable item was clicked, we
     * should display a sub menu containing the recipe of the item.
     *
     * @param itemStack
     */
    private void handleClick(InventoryClickEvent event, ItemStack itemStack) {

        this.sounds.playActionConfirm();

        // If the player is in creative mode and this is a shift click, give it to them.
        if (this.player.getGameMode() == GameMode.CREATIVE && event.isShiftClick()) {
            this.playSound(Sound.ENTITY_ITEM_PICKUP, 1, .5f);
            var item = itemStack.clone();
            SMPRPG.getInstance().getItemService().ensureItemStackUpdated(item);
            this.player.getInventory().addItem(item);
            return;
        }

        // Currently, we don't do anything unless the item is craftable, but that is subject to change.
        // When it does change, that logic goes here.
        SMPItemBlueprint blueprint = SMPRPG.getInstance().getItemService().getBlueprint(itemStack);
        if (!(blueprint instanceof Craftable craftable)) {
            this.playInvalidAnimation();
            return;
        }

        // Open up a submenu for the crafting recipe.
        this.openSubMenu(new MenuRecipeViewer(player, this, craftable, itemStack));
    }

    /**
     * Renders the inventory based on the state of this instance. Factors in current page and the current query
     * to decide what to display on the page.
     */
    public void render() {

        this.clear();
        this.setBorderEdge();

        // Pagination logic, do a bounds check on the current page and allow page wrapping.
        int totalItems = queriedItems.size();
        int area = (ROWS-2) * 7;  // Exclude top and bottom rows, 7 slots in each row
        int lastPage = totalItems / area;
        int itemIndexOffset = page * area;
        if (itemIndexOffset >= totalItems) {
            itemIndexOffset = 0;
            page = 0;
        }
        if (itemIndexOffset < 0) {
            page = lastPage;
            itemIndexOffset = area * page;
        }

        // Display items!
        for (int slot = 0; slot < getInventorySize(); slot++) {

            // Is the item index out of bounds? This can happen on the last page.
            if (itemIndexOffset >= totalItems)
                break;

            // Is this slot already occupied? Skip
            if (this.getItem(slot) != null)
                continue;

            // Add the button
            ItemStack item = queriedItems.get(itemIndexOffset);

            // Re-render the lore on the item. This needs to be done so we don't duplicate injected lore.
            var blueprint = SMPRPG.getInstance().getItemService().getBlueprint(item);
            var lore = SMPRPG.getInstance().getItemService().renderItemStackLore(item);
            lore.addFirst(ComponentUtils.EMPTY);
            lore.addFirst(ComponentUtils.create("Click to view recipe!", NamedTextColor.YELLOW));
            lore.addFirst(ComponentUtils.EMPTY);

            // If this ingredient can be crafted, insert the craftable tooltip.
            if (blueprint instanceof Craftable)
                item.editMeta(meta -> meta.lore(lore));

            this.setButton(slot, item, event -> this.handleClick(event, item));
            itemIndexOffset++;
        }

        // Extra utility buttons
        // Now set the slots for a next/prev page that increment/decrement the page and re-render
        int displayPage = page + 1;
        int displayPageMax = lastPage + 1;
        this.setButton((ROWS-1)*9, createNamedItem(Material.ARROW, ComponentUtils.create("Previous Page (" + displayPage + "/" + displayPageMax + ")", NamedTextColor.GOLD)), (e) -> {
            page--;
            this.render();
            this.sounds.playPagePrevious();
        });

        this.setButton((ROWS-1)*9+8, createNamedItem(Material.ARROW, ComponentUtils.create("Next Page (" + displayPage + "/" + displayPageMax + ")", NamedTextColor.GOLD)), (e) -> {
            page++;
            this.render();
            this.sounds.playPageNext();
        });

        // Close button
        this.setButton((ROWS-1)*9+4, BUTTON_EXIT, (e) -> {
            this.closeMenu();
            this.sounds.playMenuClose();
        });

    }
}

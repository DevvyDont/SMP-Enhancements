package xyz.devvydont.smprpg.gui.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.animations.blockers.WaitFor;
import xyz.devvydont.smprpg.util.animations.playback.AnimationHandle;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * A menu that is displayed to a player.
 */
public abstract class MenuBase implements Listener {
    // -----------
    //   Presets
    // -----------
    protected static final ItemStack BORDER_NORMAL = createNamedItem(Material.BLACK_STAINED_GLASS_PANE, Component.text(""));
    protected static final ItemStack BUTTON_PAGE_NEXT = createNamedItem(Material.ARROW, Component.text("Next Page ->", NamedTextColor.BLUE));
    protected static final ItemStack BUTTON_PAGE_PREVIOUS = createNamedItem(Material.ARROW, Component.text("<- Previous Page", NamedTextColor.BLUE));
    protected static final ItemStack BUTTON_BACK = createNamedItem(Material.ARROW, Component.text("Back", NamedTextColor.BLUE));
    protected static final ItemStack BUTTON_EXIT = createNamedItem(Material.BARRIER, Component.text("Exit", NamedTextColor.RED));

    // ---------
    //   State
    // ---------
    protected final Player player;
    protected final MenuBase parentMenu;
    protected final Inventory inventory;
    protected final MenuSoundManager sounds;

    private boolean shouldPlayOpeningSound;
    private boolean shouldPlayClosingSound;
    private AnimationHandle activeAnimation;
    private final Map<Integer, MenuButtonClickHandler> buttonSlots = new HashMap<>();


    // ----------------
    //   Constructors
    // ----------------

    public MenuBase(@NotNull Player player, int rows) {
        this(player, rows, null);
    }

    public MenuBase(@NotNull Player player, int rows, MenuBase parentMenu) {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 9 * rows);
        this.sounds = new MenuSoundManager(player);
        this.parentMenu = parentMenu;
    }


    // --------------
    //   Visibility
    // --------------

    /**
     * Opens/updates the inventory UI to this menu.
     */
    public final void openMenu() {
        this.openMenu(true);
    }

    /**
     * Opens/updates the inventory UI to this menu.
     * Used internally to manage the sounds that are played.
     *
     * @param playOpeningSound True if the opening sound should be played, otherwise false.
     */
    private void openMenu(boolean playOpeningSound) {
        // The opening sound should only play if this is the initial menu.
        this.shouldPlayOpeningSound = playOpeningSound;
        this.shouldPlayClosingSound = true;

        // Open the UI
        Bukkit.getPluginManager().registerEvents(this, SMPRPG.getInstance());
        this.player.openInventory(this.inventory);
    }

    /**
     * Updates the inventory UI to the specified sub menu, closing this menu.
     */
    protected final void openSubMenu(MenuBase subMenu) {
        // Play the transition sound
        this.sounds.playMenuOpenSub();

        // Block our closing sound and the sub menus opening sound.
        this.shouldPlayClosingSound = false;
        subMenu.openMenu(false);
    }

    /**
     * Updates the inventory UI to the parent menu, closing this menu.
     */
    protected final void openParentMenu() {
        // Report if there's no parent menu.
        if (this.parentMenu == null) {
            throw new IllegalStateException("No parent menu provided");
        }

        // Play the transition sound
        this.sounds.playMenuOpenParent();

        // Block our closing sound and the sub menus opening sound.
        this.shouldPlayClosingSound = false;
        this.parentMenu.openMenu(false);
    }

    /**
     * Closes the inventory UI completely.
     */
    public final void closeMenu() {
        this.player.closeInventory();
    }


    // ----------
    //   Events
    // ----------

    @EventHandler
    public final void __handleInventoryOpened(InventoryOpenEvent event) {
        var eventForOtherInventory = !event.getInventory().equals(this.inventory);
        if (eventForOtherInventory) {
            return;
        }

        if (this.shouldPlayOpeningSound) {
            this.sounds.playMenuOpen();
        }

        this.handleInventoryOpened(event);
    }

    @EventHandler
    public final void __handleInventoryClicked(InventoryClickEvent event) {
        var eventForOtherInventory = !event.getInventory().equals(this.inventory);
        if (eventForOtherInventory) {
            return;
        }

        // Explicitly disable number key modifications.
        if (event.getClick().equals(ClickType.NUMBER_KEY)) {
            event.setCancelled(true);
            this.playInvalidAnimation();
            return;
        }

        // Here we use the raw slot index instead of the slot index.
        // This is because buttons should only be inside the menu inventory.
        // The index returned by event.getSlot() is relative to the clicked inventory.
        // As the menu is slot index 0 to something, we can use the raw index and skip inventory checks.
        var clickHandler = this.buttonSlots.getOrDefault(event.getRawSlot(), null);
        if (clickHandler != null) {
            event.setCancelled(true);
            clickHandler.handleClick(event);
            return;
        }

        this.handleInventoryClicked(event);
    }

    @EventHandler
    public final void __handleInventoryClosed(InventoryCloseEvent event) {
        var eventForOtherInventory = !event.getInventory().equals(this.inventory);
        if (eventForOtherInventory) {
            return;
        }

        if (this.shouldPlayClosingSound) {
            this.sounds.playMenuClose();
        }

        this.stopAnimation();
        this.handleInventoryClosed(event);
        HandlerList.unregisterAll(this);
    }


    // -------------
    //   Overrides
    // -------------

    /**
     * Called when the menu is displayed.
     */
    protected void handleInventoryOpened(InventoryOpenEvent event) {
    }

    ;

    /**
     * Called when a non button slot is clicked on the menu.
     */
    protected void handleInventoryClicked(InventoryClickEvent event) {
    }

    ;

    /**
     * Called when the menu is closed.
     */
    protected void handleInventoryClosed(InventoryCloseEvent event) {
    }

    ;


    // ------------------------
    //   Inventory Operations
    // ------------------------

    /**
     * Gets the item stored in one of menu inventory slots.
     *
     * @param slotIndex The index of the inventory slot.
     * @return The item stored in the inventory slot or null if the slot is empty.
     */
    protected final @Nullable ItemStack getItem(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= this.inventory.getSize()) {
            return null;
        }
        return this.inventory.getItem(slotIndex);
    }

    /**
     * Gets all the items stored in the menus inventory.
     *
     * @return An array containing all the item stacks stored in the menu.
     */
    protected final @Nullable ItemStack @NotNull [] getItems() {
        return this.inventory.getContents();
    }

    /**
     * Returns the size of the underlying inventory.
     *
     * @return The size of menu inventory.
     */
    protected final int getInventorySize() {
        return this.inventory.getSize();
    }

    /**
     * Copies the item from the menu inventory to the players inventory.
     * Warning: This method does not delete the item from the menu inventory.
     *
     * @param slotIndex          The index of the item slot the item is in.
     * @param shouldDropOnGround True if any leftover items should be dropped onto the ground, otherwise false.
     */
    protected final void giveItemToPlayer(int slotIndex, boolean shouldDropOnGround) {
        var itemStack = this.inventory.getItem(slotIndex);
        if (itemStack == null) {
            return;
        }

        // Give the maximum amount of items to the player.
        var overflowItems = this.player.getInventory().addItem(itemStack).values();
        if (!shouldDropOnGround) {
            return;
        }

        // Throw any remaining items onto the ground.
        for (var item : overflowItems) {
            this.player.getWorld().dropItemNaturally(this.player.getLocation(), item);
        }
    }

    /**
     * Sets the maximum size a stack can be.
     *
     * @param maxStackSize The maximum stack size.
     */
    protected final void setMaxStackSize(int maxStackSize) {
        this.inventory.setMaxStackSize(maxStackSize);
    }

    /**
     * Sets one of the menu inventory slots to the specified item.
     *
     * @param slotIndex The inventory slot to update.
     * @param material  The material to create an item stack out of.
     */
    protected final void setSlot(int slotIndex, @NotNull Material material) {
        this.setSlot(slotIndex, new ItemStack(material));
    }

    /**
     * Sets one of the menu inventory slots to the specified item.
     *
     * @param slotIndex The inventory slot to update.
     * @param itemStack The item to insert.
     */
    protected final void setSlot(int slotIndex, @NotNull ItemStack itemStack) {
        if (slotIndexOutsideMenuBounds(slotIndex)) {
            throw new IllegalStateException("Provided slot index is outside the bounds of the menu inventory.");
        }

        this.inventory.setItem(slotIndex, itemStack);
    }

    /**
     * Sets all the menu inventory slots to the specified item.
     *
     * @param material The material to create an item stack out of.
     */
    protected final void setSlots(Material material) {
        this.setSlots(new ItemStack(material));
    }

    /**
     * Sets all the menu inventory slots to the specified item.
     *
     * @param itemStack The item to insert.
     */
    protected final void setSlots(ItemStack itemStack) {
        for (var slotIndex = 0; slotIndex < this.inventory.getSize(); slotIndex++) {
            this.setSlot(slotIndex, itemStack);
        }
    }

    /**
     * Converts one of the menu inventory slots to be a button.
     *
     * @param slotIndex The inventory slot to convert.
     * @param itemStack The item to represent the button.
     * @param handler   The function to invoke when the button is pressed.
     */
    protected final void setButton(int slotIndex, @NotNull ItemStack itemStack, @NotNull MenuButtonClickHandler handler) {
        if (slotIndexOutsideMenuBounds(slotIndex)) {
            throw new IllegalStateException("Provided slot index is outside the bounds of the menu inventory.");
        }

        this.setSlot(slotIndex, itemStack);
        this.buttonSlots.put(slotIndex, handler);
    }

    /**
     * Replaces instances of an item in the menus inventory with another one.
     *
     * @param oldItem The item to replace.
     * @param newItem The item to replace the old item with.
     */
    protected final void replaceSlots(ItemStack oldItem, ItemStack newItem) {
        for (var slotIndex = 0; slotIndex < this.inventory.getSize(); slotIndex++) {
            var shouldReplace = oldItem.equals(this.getItem(slotIndex));
            if (shouldReplace) {
                this.setSlot(slotIndex, newItem);
            }
        }
    }

    /**
     * Removes all items from the menus inventory.
     */
    public final void clear() {
        this.inventory.clear();
        this.buttonSlots.clear();
    }

    /**
     * Removes an item from the menus inventory.
     *
     * @param slotIndex The index of the inventory slot to clear.
     */
    protected final void clearSlot(int slotIndex) {
        if (slotIndexOutsideMenuBounds(slotIndex)) {
            throw new IllegalStateException("Provided slot index is outside the bounds of the menu inventory.");
        }

        this.inventory.setItem(slotIndex, null);
        this.buttonSlots.remove(slotIndex);
    }


    // -----------
    //   Borders
    // -----------

    /**
     * Creates a border around the perimeter of the menus inventory.
     */
    public final void setBorderEdge() {
        var canApplyBorder = this.inventory.getSize() >= (3 * 9);
        if (!canApplyBorder) {
            throw new IllegalArgumentException("Edge borders can only be applied to menus with 3 or more rows");
        }

        for (var slotIndex = 0; slotIndex < this.inventory.getSize(); slotIndex++) {
            var isTopSlot = slotIndex <= 8;
            var isBottomSlot = this.inventory.getSize() - slotIndex <= 9;
            var isSideSlot = slotIndex % 9 == 0 || slotIndex % 9 == 8;
            if (isTopSlot || isBottomSlot || isSideSlot) {
                this.setSlot(slotIndex, BORDER_NORMAL);
            }
        }
    }

    /**
     * Creates a border which covers every slot in the menus inventory.
     */
    protected final void setBorderFull() {
        this.setSlots(BORDER_NORMAL);
    }


    // --------------
    //   Animations
    // --------------

    /**
     * Plays an animation which signifies the user performed a valid operation.
     */
    protected final void playSuccessAnimation() {
        stopAnimation();
        var successBorder = createNamedItem(Material.LIME_STAINED_GLASS_PANE, Component.text(""));
        this.activeAnimation = SMPRPG.getInstance().getAnimationService().playOnce(
            () -> {
                this.sounds.playActionConfirm();
                this.replaceSlots(BORDER_NORMAL, successBorder);
                return WaitFor.milliseconds(200);
            },
            () -> {
                this.replaceSlots(successBorder, BORDER_NORMAL);
                return WaitFor.nothing();
            }
        );
    }

    /**
     * Plays an animation which signifies the user performed an invalid operation.
     */
    protected final void playInvalidAnimation() {
        stopAnimation();
        var errorBorder = createNamedItem(Material.RED_STAINED_GLASS_PANE, Component.text(""));
        this.activeAnimation = SMPRPG.getInstance().getAnimationService().playOnce(
            () -> {
                this.sounds.playActionError();
                this.replaceSlots(BORDER_NORMAL, errorBorder);
                return WaitFor.milliseconds(200);
            },
            () -> {
                this.replaceSlots(errorBorder, BORDER_NORMAL);
                return WaitFor.nothing();
            }
        );
    }

    /**
     * Stops and cleans up any animation that is currently active.
     */
    protected final void stopAnimation() {
        if (this.activeAnimation != null) {
            this.activeAnimation.stop();
            this.activeAnimation = null;
        }
    }


    // -----------
    //   Helpers
    // -----------

    /**
     * Plays a one-shot sound on the player's client.
     *
     * @param sound The sound effect to play.
     */
    protected final void playSound(Sound sound) {
        this.playSound(sound, 1.0f, 1.0f);
    }

    /**
     * Plays a one-shot sound on the player's client.
     *
     * @param sound  The sound effect to play.
     * @param volume How loud the sound should be.
     * @param pitch  The pitch of the sound effect.
     */
    protected final void playSound(Sound sound, float volume, float pitch) {
        this.player.playSound(this.player.getLocation(), sound, volume, pitch);
    }

    /**
     * Attempts to add the specified item to the players inventory.
     *
     * @param item The item to give to the player.
     */
    protected final void giveItemToPlayer(ItemStack item) {
        var overflow = this.player.getInventory().addItem(item);
        for (var overflowItem : overflow.entrySet()) {
            this.player.getWorld().dropItemNaturally(this.player.getEyeLocation(), overflowItem.getValue());
        }
    }

    /**
     * Returns if the provided slot index is outside the bounds of the menu inventory.
     *
     * @param slotIndex The relative or raw slot index to check.
     * @return True if it's outside the bounds, otherwise false.
     */
    protected final boolean slotIndexOutsideMenuBounds(int slotIndex) {
        return slotIndex < 0 || this.inventory.getSize() <= slotIndex;
    }

    /**
     * Creates an item stack with a custom name.
     *
     * @param material The type of the item to create an item stack of.
     * @param name     The name to apply to the item stack.
     * @return The named item stack.
     */
    protected static ItemStack createNamedItem(Material material, String name) {
        var item = new ItemStack(material);
        var meta = item.getItemMeta();
        meta.displayName(ComponentUtils.create(name, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates an item stack with a custom name.
     *
     * @param material The type of the item to create an item stack of.
     * @param name     The name to apply to the item stack.
     * @return The named item stack.
     */
    protected static ItemStack createNamedItem(Material material, Component name) {
        var item = new ItemStack(material);
        var meta = item.getItemMeta();
        meta.displayName(name.decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
        return item;
    }
}

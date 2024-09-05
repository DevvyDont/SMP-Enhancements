package xyz.devvydont.smprpg.gui.base;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Delegate that handles a menu button click.
 */
public interface MenuButtonClickHandler {
    /**
     * Handles a button being clicked.
     *
     * @param event The client event that triggered the invocation.
     */
    void handleClick(InventoryClickEvent event);
}

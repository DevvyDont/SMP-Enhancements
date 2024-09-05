package xyz.devvydont.smprpg.gui.items;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public final class MenuTrashItems extends MenuBase {
    public MenuTrashItems(@NotNull Player player) {
        super(player, 6);
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        // Prepare the inventory
        event.titleOverride(ComponentUtils.create("Trash Items", NamedTextColor.BLACK));
        this.setMaxStackSize(100);

        // Render the UI
        this.clear();
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        event.setCancelled(false);
    }

    @Override
    protected void handleInventoryClosed(InventoryCloseEvent event) {
        var itemCount = 0;
        for (var itemStack : this.getItems()) {
            if (itemStack == null) {
                continue;
            }

            itemCount += itemStack.getAmount();
            itemStack.setAmount(0);
        }

        this.player.sendMessage(ComponentUtils.success(ComponentUtils.merge(
            ComponentUtils.create("You trashed ", NamedTextColor.GREEN),
            ComponentUtils.create(String.valueOf(itemCount), NamedTextColor.AQUA),
            ComponentUtils.create(" items!", NamedTextColor.GREEN)
        )));
    }
}

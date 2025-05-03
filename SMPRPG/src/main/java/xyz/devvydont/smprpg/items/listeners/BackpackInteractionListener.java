package xyz.devvydont.smprpg.items.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.devvydont.smprpg.gui.items.MenuContainer;
import xyz.devvydont.smprpg.items.interfaces.IItemContainer;
import xyz.devvydont.smprpg.services.ItemService;

public class BackpackInteractionListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    private void __onInteractWithBackpack(final PlayerInteractEvent event) {

        var item = event.getItem();
        if (item == null)
            return;

        if (!event.getAction().isRightClick())
            return;

        if (event.getPlayer().isSneaking())
            return;

        var blueprint = ItemService.blueprint(item);
        if (!(blueprint instanceof IItemContainer container))
            return;

        event.setCancelled(true);
        new MenuContainer(event.getPlayer(), container, item).openMenu();
    }

}

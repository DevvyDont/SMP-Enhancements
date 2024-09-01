package xyz.devvydont.smprpg.items.base;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class CustomFakeTotem extends CustomAttributeItem implements Listener {

    public CustomFakeTotem(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @EventHandler
    public void onDeathWithThisTotem(EntityResurrectEvent event) {

        // No totem is involved? don't care
        if (event.getHand() == null)
            return;

        // Equipment is null? shouldn't be possible
        if (event.getEntity().getEquipment() == null)
            return;

        ItemStack totem = event.getEntity().getEquipment().getItem(event.getHand());

        if (!isItemOfType(totem))
            return;

        // This is our fake totem, don't make it act like a normal totem
        event.setCancelled(true);
    }


}

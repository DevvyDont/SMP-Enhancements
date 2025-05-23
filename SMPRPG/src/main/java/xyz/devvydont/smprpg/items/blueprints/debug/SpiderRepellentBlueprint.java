package xyz.devvydont.smprpg.items.blueprints.debug;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class SpiderRepellentBlueprint extends CustomItemBlueprint implements Listener {

    public SpiderRepellentBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent event) {

        if (!isItemOfType(event.getItem()))
            return;

        int n = 0;
        for (Entity e : event.getPlayer().getWorld().getEntitiesByClass(Spider.class)) {
            e.remove();
            n++;
        }
        event.getPlayer().sendMessage(ComponentUtils.alert("Killed " + n + " spiders!"));
    }
}

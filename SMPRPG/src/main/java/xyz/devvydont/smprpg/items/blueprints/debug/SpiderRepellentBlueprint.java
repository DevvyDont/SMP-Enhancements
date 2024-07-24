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

public class SpiderRepellentBlueprint extends CustomItemBlueprint implements Listener {


    public SpiderRepellentBlueprint(ItemService itemService) {
        super(itemService);
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.SPIDER_REPELLENT;
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
        event.getPlayer().sendMessage("Killed " + n + " spiders!!!!");
    }
}

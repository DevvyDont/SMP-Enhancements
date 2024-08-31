package xyz.devvydont.smprpg.items.blueprints.debug;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawner;
import xyz.devvydont.smprpg.gui.spawner.InterfaceSpawnerMainMenu;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpawnerEditorBlueprint extends CustomItemBlueprint implements Listener {

    public SpawnerEditorBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getFooterComponent(ItemMeta meta) {
        return List.of(
                ComponentUtils.create("Used to interact with"),
                ComponentUtils.create("and edit custom spawner"),
                ComponentUtils.create("entities in the world")
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteractWhileHoldingEditor(PlayerInteractEvent event) {

        ItemStack item = event.getItem();
        if (!isItemOfType(item))
            return;

        event.setCancelled(true);

        if (!event.getPlayer().isOp()){
            event.getPlayer().sendMessage(ComponentUtils.getErrorMessage("You must be a server operator to utilize this item as it is a dangerous admin item!"));
            return;
        }

        Collection<Entity> nearbyDisplays = event.getPlayer().getWorld().getNearbyEntitiesByType(CustomEntityType.SPAWNER.entityType.getEntityClass(), event.getPlayer().getEyeLocation(), 2.5);
        List<EntitySpawner> nearbySpawners = new ArrayList<>();
        for (Entity display : nearbyDisplays)
            if (SMPRPG.getInstance().getEntityService().getEntityInstance(display) instanceof EntitySpawner spawner)
                nearbySpawners.add(spawner);

        if (nearbySpawners.isEmpty()) {
            event.getPlayer().sendMessage(ComponentUtils.getErrorMessage("Did not detect any spawners near you! Get closer to one and try again :3"));
            return;
        }

        if (nearbySpawners.size() > 1) {
            event.getPlayer().sendMessage(ComponentUtils.getErrorMessage("Detected too many spawners near you! Try to limit the spawners you are close to!"));
            return;
        }

        EntitySpawner spawner = nearbySpawners.getFirst();

        new InterfaceSpawnerMainMenu(SMPRPG.getInstance(), event.getPlayer(), spawner).open();
        event.getPlayer().sendMessage(ComponentUtils.getSuccessMessage("Now editing the spawner you were looking at!"));
    }
}

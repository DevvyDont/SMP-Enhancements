package xyz.devvydont.smprpg.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.generator.structure.GeneratedStructure;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

/*
 * Listener in charge of managing PVP events. Since this is mostly a PVE plugin, we should restrict PVP in certain
 * scenarios.
 */
public class PvPListener extends ToggleableListener {

    /**
     * Checks the structures in an entity's chunk and checks if any of them are overlapping with the entity.
     *
     * @param entity An entity to check for.
     * @return true if the entity is currently in a structure, false if they are not.
     */
    private boolean entityIsInStructure(Entity entity) {

        for (GeneratedStructure structure : entity.getChunk().getStructures())
            if (structure.getBoundingBox().overlaps(entity.getBoundingBox()))
                return true;

        return false;
    }

    /*
     * Prevent players from dealing damage to each other if they are both inside of a structure.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPVPWithinStructure(CustomEntityDamageByEntityEvent event) {

        // Are both of these entities players and contained in a structure? Cancel the event if so
        if (event.getDealer() instanceof Player && event.getDamaged() instanceof Player) {
            if (entityIsInStructure(event.getDealer()) && entityIsInStructure(event.getDamaged()))
                event.setCancelled(true);
        }

    }
}

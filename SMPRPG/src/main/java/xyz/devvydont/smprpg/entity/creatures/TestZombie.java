package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;

public class TestZombie extends CustomEntityInstance implements Listener {


    public TestZombie(SMPRPG plugin, Entity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    @EventHandler
    public void onAttackPlayer(EntityDamageByEntityEvent event) {

        if (!isEntity(event.getDamager()))
            return;

        event.getEntity().sendMessage("hit by custom zombie");
    }

}

package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.entity.CustomEntityType;

public class TestZombie extends CustomEntityInstance implements Listener {


    public TestZombie(SMPRPG plugin, LivingEntity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    @EventHandler
    public void onAttackPlayer(EntityDamageByEntityEvent event) {

        if (!isEntity(event.getDamager()))
            return;

        event.getEntity().sendMessage("hit by custom zombie");
    }

}

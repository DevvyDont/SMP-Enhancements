package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;

public class TestZombie<T extends LivingEntity> extends CustomEntityInstance<T> implements Listener {

    public TestZombie(Entity entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    public TestZombie(T entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    @EventHandler
    public void onAttackPlayer(EntityDamageByEntityEvent event) {

        if (!isEntity(event.getDamager()))
            return;

        event.getEntity().sendMessage("hit by custom zombie");
    }

}

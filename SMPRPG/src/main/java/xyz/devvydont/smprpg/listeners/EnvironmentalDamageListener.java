package xyz.devvydont.smprpg.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;

public class EnvironmentalDamageListener implements Listener {

    final SMPRPG plugin;

    /**
     * Environmental damage causes % damage to health since health can get out of control
     * This multiplier is a multiplier on top of the half heart percentage
     *
     * @param cause
     * @return
     */
    public double getEnvironmentalDamagePercentage(EntityDamageEvent.DamageCause cause) {

        return switch (cause) {

            case FIRE, FIRE_TICK, DROWNING, CAMPFIRE, HOT_FLOOR, STARVATION, FREEZE -> 1.0;
            case LAVA, DRAGON_BREATH -> 3.0;
            case VOID -> 5.0;
            case POISON, WORLD_BORDER, SUFFOCATION, CRAMMING -> 2.0;
            case WITHER -> 2.5;

            default -> -1;
        };

    }

    /**
     * Determines if a specific damage cause uses % of max HP instead of flat damage
     *
     * @param cause
     * @return
     */
    public boolean causeIsPercentage(EntityDamageEvent.DamageCause cause) {
        return getEnvironmentalDamagePercentage(cause) > 0;
    }

    public EnvironmentalDamageListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPercentageBasedEnvironmentalDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (!causeIsPercentage(event.getCause()))
            return;

        LeveledEntity entity = plugin.getEntityService().getEntityInstance((LivingEntity) event.getEntity());
        event.setDamage(entity.getHalfHeartValue() * getEnvironmentalDamagePercentage(event.getCause()));
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            return;

        float distance = event.getEntity().getFallDistance();
        LeveledEntity entity = plugin.getEntityService().getEntityInstance((LivingEntity) event.getEntity());
        double damage = entity.getMaxHp() * (distance - 3) / 40;
        event.setDamage(damage);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExplosiveDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION))
            return;

        // Take the vanilla damage and 5x it
        event.setDamage(event.getFinalDamage() * 5);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPotionDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.MAGIC))
            return;

        // Take the vanilla damage and 5x it
        event.setDamage(event.getFinalDamage() * 5);
    }
}

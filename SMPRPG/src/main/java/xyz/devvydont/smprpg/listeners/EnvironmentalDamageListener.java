package xyz.devvydont.smprpg.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;

public class EnvironmentalDamageListener implements Listener {

    final SMPRPG plugin;

    public static boolean shouldGiveIFrames(EntityDamageEvent.DamageCause cause) {

        return switch (cause) {
            case FIRE, LAVA, MELTING, DRAGON_BREATH, HOT_FLOOR, FIRE_TICK, CRAMMING, SUFFOCATION, CONTACT, CAMPFIRE, WORLD_BORDER, STARVATION, VOID, DRYOUT -> true;
            default -> false;
        };

    }

    /**
     * Environmental damage causes % damage to health since health can get out of control
     * This multiplier is a multiplier on top of the half heart percentage
     *
     * @param cause
     * @return
     */
    public static double getEnvironmentalDamagePercentage(EntityDamageEvent.DamageCause cause) {

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

    /**
     * Hopefully this becomes fully deprecated out of the API soon, very annoying to deal with.
     * We call this to completely ignore any sort of vanilla damage interaction. We are in charge of everything
     */
    public static void clearVanillaDamageModifiers(EntityDamageEvent event) {

        // Attempt to set the all vanilla modifications to 0, if this fails then the entity couldn't have had armor anyway
        for (EntityDamageEvent.DamageModifier mod : EntityDamageEvent.DamageModifier.values()) {

            if (mod.equals(EntityDamageEvent.DamageModifier.BASE))
                continue;

            if (!event.isApplicable(mod))
                continue;

            event.setDamage(mod, 0);
        }
    }

    /*
     * Only used to remove vanilla damage modifiers from the game. We are in full control of how damage is calculated.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityTakeDamage(EntityDamageEvent event) {
        clearVanillaDamageModifiers(event);
    }

    /*
     * Do it again after all calculations are done to reset any modifiers that were applied.
     */
    @EventHandler
    public void onEntityTakeDamageFinal(EntityDamageEvent event) {
        clearVanillaDamageModifiers(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExplosiveDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION))
            return;

        // Take the vanilla damage and 5x it
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, event.getDamage() * 5);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPercentageBasedEnvironmentalDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (!causeIsPercentage(event.getCause()))
            return;

        LeveledEntity entity = plugin.getEntityService().getEntityInstance(event.getEntity());

        // Bosses don't take env damage as long as it isn't explosive
        boolean isExplosive = event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
        if (entity instanceof BossInstance && !isExplosive) {
            event.setCancelled(true);
            return;
        }

        event.setDamage(EntityDamageEvent.DamageModifier.BASE, entity.getHalfHeartValue() * getEnvironmentalDamagePercentage(event.getCause()));
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            return;

        float distance = event.getEntity().getFallDistance();
        LeveledEntity entity = plugin.getEntityService().getEntityInstance(event.getEntity());

        double safeFall = 3;
        if (entity.getEntity() instanceof Attributable attributable) {
            AttributeInstance safeFallAttribute = attributable.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE);
            if (safeFallAttribute != null)
                safeFall = safeFallAttribute.getValue();
        }

        // Add a half heart of fall damage per block fallen past the safe fall distance
        double damage = entity.getHalfHeartValue() * (distance - safeFall) / 2;
        if (damage <= 0) {
            event.setCancelled(true);
            return;
        }
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPoisonDeath(EntityDamageEvent event) {

        // Save players from getting poisoned to death
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.POISON))
            return;

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        if (event.getDamage() > living.getHealth())
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, Math.max(0, living.getHealth() - 1));
    }

    /*
     * Since entities can take lots of damage very rapidly, we need to add some iframes to certain damage events so
     * they don't take an absurd amount of damage very quickly.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTakeRapidEnvironmentalDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        // If this is a boss, don't do it
        if (plugin.getEntityService().getEntityInstance(living) instanceof BossInstance)
            return;

        if (shouldGiveIFrames(event.getCause()))
            Bukkit.getScheduler().runTaskLater(plugin, () -> living.setNoDamageTicks(10), 0);
    }
}

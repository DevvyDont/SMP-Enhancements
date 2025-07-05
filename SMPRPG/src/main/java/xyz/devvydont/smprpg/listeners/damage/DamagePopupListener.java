package xyz.devvydont.smprpg.listeners.damage;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.events.damage.AbsorptionDamageDealtEvent;
import xyz.devvydont.smprpg.util.formatting.ComponentDecorator;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.text.DecimalFormat;

/**
 * This listener is in charge of the "damage popups" you see in the world when entities take damage.
 * All these listeners are responsible for listening to any relevant events to spawn in damage popups.
 */
public class DamagePopupListener extends ToggleableListener {

    // Number formatter to make popups prettier.
    private static final DecimalFormat NUMBER_FORMATTER = new DecimalFormat("#,###,###");

    /**
     * The sole purpose of the popup type is to assign behavior to specific popups.
     */
    public enum PopupType {
        DAMAGE_ARMOR(ComponentDecorator.color(NamedTextColor.GOLD)),
        GENERIC(ComponentDecorator.color(NamedTextColor.GRAY)),
        DAMAGE(ComponentDecorator.color(TextColor.color(180, 100, 100))),
        CRITICAL(ComponentDecorator.symbolizedGradient(Symbols.POWER, TextColor.color(255, 0, 60), TextColor.color(255, 138, 0))),
        GAIN_ARMOR(ComponentDecorator.color(NamedTextColor.YELLOW)),
        HEAL(ComponentDecorator.color(NamedTextColor.GREEN));

        public final ComponentDecorator Decorator;
        PopupType(ComponentDecorator decorator) {
            this.Decorator = decorator;
        }
    }

    /**
     * This is a helper method to spawn a simple text popup that cleans itself up later.
     * @param location The location to spawn the text popup.
     * @param amount The 'amount' to display within the popup. It will automatically be formatted.
     * @param type The popup type. This affects how to display it.
     */
    public static void spawnTextPopup(Location location, double amount, PopupType type) {

        // It's pointless to display nothing...
        var rounded = Math.round(amount);
        if (rounded == 0)
            return;

        // Format the amount. We want to make sure we are at least displaying 1. We also want to make this more readable.
        double finalAmount = Math.max(1, rounded);
        var text = NUMBER_FORMATTER.format(finalAmount);

        // Retrieve the component based on the popup type.
        var component = type.Decorator.decorate(text);

        // Now actually spawn the text display entity.
        var spawnLoc = location.add(Math.random()-.5, Math.random()+.3, Math.random()-.5);
        var display = location.getWorld().spawn(spawnLoc, TextDisplay.class, e -> {
            e.setPersistent(false);
            e.text(component);
            e.setBillboard(Display.Billboard.CENTER);
            e.setShadowed(true);
            e.setSeeThrough(false);
            e.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        });
        new BukkitRunnable() {
            @Override
            public void run() {
                display.remove();
            }
        }.runTaskLater(SMPRPG.getInstance(), TickTime.seconds(2));
    }

    /**
     * Hook into the custom damage event. We need to use this one to determine if it was a critical or not.
     * It must be noted however that this event will not hook into self-inflicted damage, such as fall damage etc.
     * This is explicitly entity vs. entity damage.
     * @param event The {@link CustomEntityDamageByEntityEvent} that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onEntityTakeDamage(CustomEntityDamageByEntityEvent event) {

        // If the underlying damage event was cancelled, we shouldn't care.
        if (event.getOriginalEvent().isCancelled())
            return;

        // Only worry about living entities.
        if (!(event.getDamaged() instanceof LivingEntity living))
            return;

        // First, determine the type. We only care about two here, with that being damage and critical.
        var type = event.isCritical() ? PopupType.CRITICAL : PopupType.DAMAGE;

        // We can spawn the popup. The final damage will essentially be what displays.
        spawnTextPopup(living.getEyeLocation(), event.getFinalDamage(), type);
    }

    /**
     * Hook into damage events only where the entity wasn't involved with another entity. This will include things such
     * as drowning, fall damage, cactus, etc. This paired with the handler above should handle all cases.
     * @param event The {@link EntityDamageEvent} that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onEntityTakeGenericDamage(EntityDamageEvent event) {

        // Only worry about living entities.
        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        // Analyze the cause. This needs to be harm that is not from an entity.
        if (event.getDamageSource().getCausingEntity() != null)
            return;

        // Should be good to spawn a popup!
        spawnTextPopup(living.getEyeLocation(), event.getFinalDamage(), PopupType.GENERIC);
    }

    /**
     * Hook into when entities take absorption damage. Our plugin handles absorption in a strange way, by setting the
     * actual event damage to 0 then subtracting absorption. This means we have to spawn absorption popups differently.
     * @param event The {@link AbsorptionDamageDealtEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onEntityTakeAbsorptionDamage(AbsorptionDamageDealtEvent event) {

        // This event already handles all the relevant checking. Simply spawn the popup.
        spawnTextPopup(event.getVictim().getEyeLocation(), event.getDamage(), PopupType.DAMAGE_ARMOR);
    }

    /**
     * Let's display popups for when entities heal. This is really simple, when an entity gains health, show it.
     * @param event The {@link EntityRegainHealthEvent} that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onRegenerate(EntityRegainHealthEvent event) {

        // We only care about living entities.
        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        spawnTextPopup(living.getEyeLocation(), event.getAmount(), PopupType.HEAL);
    }

    /**
     * Let's display a popup when a user gains absorption hearts. Our plugin treats this as a "temporary armor" mechanic,
     * which scales with their health which contributes to their health pool.
     * @param event The {@link EntityPotionEffectEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onPotionEffectUpdate(EntityPotionEffectEvent event) {

        // We are only concerned with the absorption potion effect.
        if (event.getNewEffect() == null || !event.getNewEffect().getType().equals(PotionEffectType.ABSORPTION))
            return;

        // Only worry about living entities. We need information about their max health.
        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        // Retrieve the wrapper so we can easily extract health information.
        var leveled = SMPRPG.getInstance().getEntityService().getEntityInstance(living);

        // We need to calculate how much absorption health they gained. This may need to be standardized later...
        // The idea is that their absorption hearts are equal in health as their normal hearts.
        var amount = (event.getNewEffect().getAmplifier()+1.0) * leveled.getHalfHeartValue() * 4;

        // Spawn the popup!
        spawnTextPopup(living.getEyeLocation(), amount, PopupType.GAIN_ARMOR);
    }
}

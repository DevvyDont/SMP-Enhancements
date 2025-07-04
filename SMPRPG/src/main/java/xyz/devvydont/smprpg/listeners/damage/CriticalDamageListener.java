package xyz.devvydont.smprpg.listeners.damage;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

/**
 * When this listener is initialized, the plugin will listen for "critical" damage events, and set the event to be
 * critical as a result. Whether this listener is enabled basically decides if critical hits are enabled or not.
 * This listener is in charge of the following mechanics:
 * - Apply the critical flag when manual melee crits occur by jumping and attacking.
 * - Apply the critical flag when manual bow/arrow crits occur by shooting fully charged shots.
 * - Apply the critical flag when auto crit chance procs for all melee hits. (Crit Chance % attribute).
 * - Apply the critical flag when auto crit chance procs for all bow shots (Crit Chance % attribute).
 * - Apply critical damage when critical flag is present by viewing an entity's Crit Rating attribute.
 * - Make the visuals known that the event was critical via particles and sounds.
 */
public class CriticalDamageListener extends ToggleableListener {

    /**
     * The force requirement for an arrow to be considered critical.
     */
    public static final float CRITICAL_ARROW_FORCE_THRESHOLD = .95f;

    /**
     * The default "critical rating" to use for an entity if they don't have the Critical Rating attribute.
     */
    public static final float DEFAULT_CRITICAL_RATING = .5f;

    /**
     * Implements the logic that causes bow shots to shoot critical arrows.
     * All this event does is set the critical flag on the arrow to true if a fully charged shot occurred and that's it.
     * @param event The {@link EntityShootBowEvent} that contains the necessary context for us.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void __onCriticalArrowConditionMeet(EntityShootBowEvent event) {

        // Skip non-arrows
        if (!(event.getEntity() instanceof AbstractArrow arrow))
            return;

        // All arrow shot at full force are critical.
        if (event.getForce() >= CRITICAL_ARROW_FORCE_THRESHOLD)
            arrow.setCritical(true);
    }

    /**
     * Implements the logic that causes bow shots to automatically shoot critical arrows.
     * All this event does is set the critical flag on the arrow to true if we pass a stat check and that's it.
     * @param event The {@link EntityShootBowEvent} that contains the necessary context for us.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void __onAutoCriticalArrowConditionMeet(EntityShootBowEvent event) {

        // Skip non-arrows
        if (!(event.getEntity() instanceof AbstractArrow arrow))
            return;

        // Do a crit chance check.
        var crit = AttributeService.getInstance().getAttribute(event.getEntity(), AttributeWrapper.CRITICAL_CHANCE);
        if (crit == null)
            return;

        // Roll for auto crit.
        var chance = crit.getValue() / 100.0;
        var success = Math.random() < chance;

        // All arrow shot at full force are critical.
        if (success)
            arrow.setCritical(true);
    }

    /**
     * Implements the logic that causes melee critical hits to occur.
     * All this event does is set the critical flag on the damage event to true, and that's it.
     * @param event The {@link CustomEntityDamageByEntityEvent} that contains the necessary context for us.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void __onMeleeCriticalDamageConditionMeet(CustomEntityDamageByEntityEvent event) {

        // Criticals can only occur for melee hits.
        if (!event.getVanillaCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            return;

        // If the entity is not airborne, this can't be a crit.
        if (event.getDealer().isOnGround())
            return;

        // If the entity doesn't have negative Y velocity, this can't be a crit.
        if (event.getDealer().getVelocity().getY() >= 0)
            return;

        // We have met all the conditions for a crit.
        event.setCritical(true);
    }

    /**
     * Implements the logic that causes auto melee critical hits to occur.
     * All this event does is set the critical flag on the damage event to true, and that's it.
     * @param event The {@link CustomEntityDamageByEntityEvent} that contains the necessary context for us.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void __onMeleeAutoCriticalDamageConditionMeet(CustomEntityDamageByEntityEvent event) {

        // Criticals can only occur for melee hits.
        if (!event.getVanillaCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            return;

        // No point on continuing unless the dealer can have attributes.
        if (!(event.getDealer() instanceof LivingEntity living))
            return;

        // Check the entity for their auto crit chance.
        var chance = 0d;
        var crit = AttributeService.getInstance().getAttribute(living, AttributeWrapper.CRITICAL_CHANCE);
        if (crit != null)
            chance = crit.getValue() / 100;  // Keep in mind, unformatted percentage.

        // Roll!
        var success = Math.random() < chance;
        if (success)
            event.setCritical(true);
    }

    /**
     * Implements the logic that causes bow shots to be critical.
     * All this event does is set the critical flag on the arrow damage event to true, and that's it.
     * @param event The {@link CustomEntityDamageByEntityEvent} that contains the necessary context for us.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void __onArrowCriticalDamageConditionMeet(CustomEntityDamageByEntityEvent event) {

        // This one is actually pretty simple, as the critical flag is actually stored on the arrow.
        if (event.getProjectile() instanceof AbstractArrow arrow)
            if (arrow.isCritical())
                event.setCritical(true);
    }

    /**
     * Implements the damage increase that is added when a critical happens. This is one of the last steps in damage
     * calculation, as critical damage just multiplies the end resulting damage.
     * @param event The {@link CustomEntityDamageByEntityEvent} that contains the necessary context for us.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void __onCriticalDamage(CustomEntityDamageByEntityEvent event) {

        if (!event.isCritical())
            return;

        // Extract the critical damage multiplier from the dealer. Their critical damage attribute is the multiplier.
        var multiplier = 1.0 + DEFAULT_CRITICAL_RATING;

        // Only living entities can be attribute checked.
        if (event.getDealer() instanceof LivingEntity living) {
            var crit = AttributeService.getInstance().getAttribute(living, AttributeWrapper.CRITICAL_DAMAGE);

            // Only update if they have the attribute, and remember it is an unformatted percentage **boost**.
            if (crit != null)
                multiplier = 1.0 + crit.getValue() / 100;
        }

        event.multiplyDamage(multiplier);
    }

    /**
     * Implements the visuals for when a critical occurs. When an entity is hit by a critical, the critical noise and
     * particles should play out to signal that the hit was critical. We also do not care to run this if the event is
     * cancelled for whatever reason, so we can ignoreCancelled.
     * @param event The {@link CustomEntityDamageByEntityEvent} that contains the necessary context for us.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onCriticalDisplay(CustomEntityDamageByEntityEvent event) {

        if (!event.isCritical())
            return;

        // Play sound and spawn particles.
        event.getDamaged().getWorld().playSound(event.getDamaged().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, .5f, 1f);
        event.getDamaged().getWorld().spawnParticle(Particle.CRIT, event.getDamaged().getLocation().add(0, 1, 0), 10, 0.25, 0.1, 0.25, 0.25);
    }

}

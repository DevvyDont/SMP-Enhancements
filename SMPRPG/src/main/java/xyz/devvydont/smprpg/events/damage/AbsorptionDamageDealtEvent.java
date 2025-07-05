package xyz.devvydont.smprpg.events.damage;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A simple event that is broadcasted when damage is dealt to an entity's Absorption heart pool.
 * This event represents the "post" state of the damage being dealt, so the damage event has already happened.
 * This is mostly meant for monitoring.
 */
public class AbsorptionDamageDealtEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private final LivingEntity victim;
    private final double damage;
    private final boolean cracked;

    public AbsorptionDamageDealtEvent(LivingEntity victim, double damage, boolean cracked) {
        this.damage = damage;
        this.cracked = cracked;
        this.victim = victim;
    }

    /**
     * Get the entity that took damage in this event.
     * @return A {@link LivingEntity} instance.
     */
    public LivingEntity getVictim() {
        return victim;
    }

    /**
     * Get the damage that the entity withstood from the event.
     * @return A number representing damage.
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Check if the entity has completely exhausted their absorption as a result of this damage.
     * The term "cracked" essentially means they ran out of "shields" or absorption in Minecraft terms.
     * @return True if the entity no longer has absorption, false if they still have some left over.
     */
    public boolean isCracked() {
        return cracked;
    }
}

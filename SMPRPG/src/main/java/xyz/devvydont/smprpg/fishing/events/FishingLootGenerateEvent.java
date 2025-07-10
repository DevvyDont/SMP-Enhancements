package xyz.devvydont.smprpg.fishing.events;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.fishing.calculator.FishLootCalculator;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;

/**
 * An event that fires when our plugin generates fishing loot. Can be used to override the entity that gets hooked,
 * or evaluate the probabilities of a catch occurring. Note, that if you override the entity that is caught in this event,
 * the announcement will be turned off by default. If you still want to announce the event, you should update that afterward.
 * The reason we use a custom event for fishing is due to just how much more complex loot rolling is, and *everything*
 * has a low drop rate. This event provides a way to fine-tune what and how you want to announce loot generation, as well
 * as modify fishing rewards.
 */
public class FishingLootGenerateEvent extends Event implements Cancellable {

    /**
     * Helper for tri-state announcement mode. Don't announce, only whisper player, or tell whole server.
     */
    public enum AnnounceMode {
        DONT,
        PLAYER,
        SERVER
    }

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private boolean cancelled;
    private AnnounceMode announceMode = AnnounceMode.DONT;
    private final FishingContext fishingContext;
    private Entity entity;
    private final FishLootCalculator.CalculationResult calculationResult;
    private @Nullable Component componentOverride = null;

    public FishingLootGenerateEvent(FishingContext fishingContext, Entity entity, FishLootCalculator.CalculationResult calculationResult) {
        this.fishingContext = fishingContext;
        this.entity = entity;
        this.calculationResult = calculationResult;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    /**
     * Checks if this event is cancelled. If it is, no loot will generate.
     * @return True if cancelled, False otherwise.
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancels the event.
     * @param cancel {@code true} if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public FishingContext getFishingContext() {
        return fishingContext;
    }

    /**
     * Get the announcement mode for this loot event.
     * @return The announcement mode to use.
     */
    public AnnounceMode getAnnounceMode() {
        return announceMode;
    }

    /**
     * Set the announcement mode for this event. Keep in mind this is implicitly called if entity is changed.
     * @param announceMode The new announcement mode to use.
     */
    public void setAnnounceMode(AnnounceMode announceMode) {
        this.announceMode = announceMode;
    }

    /**
     * Gets the calculation result that caused this event to occur. Keep in mind that if the entity gets overridden,
     * then this information is basically meaningless.
     * @return The calculation result that led this event to occur.
     */
    public FishLootCalculator.CalculationResult getCalculationResult() {
        return calculationResult;
    }

    /**
     * Get the entity that was caught. This represents the raw Minecraft entity, so it is either an Item or an Entity.
     * @return The Entity that was caught.
     */
    public Entity getCaughtEntity() {
        return entity;
    }

    /**
     * Override the entity that was caught by attaching it to the hook and launching it towards the player.
     * Implicitly disables the announcement as well. If you want to announce something still, you need to set a new mode.
     * @param entity The new entity to attach to the hook.
     */
    public void setCaughtEntity(Entity entity) {
        this.entity = entity;
        this.setAnnounceMode(AnnounceMode.DONT);
    }

    /**
     * Set a complete component override for the announcement of this event. If this is left null, that means a default
     * one will be generated.
     * @return The Component instance to announce. Can be null.
     */
    public @Nullable Component getComponentOverride() {
        return componentOverride;
    }

    /**
     * Set or clear a component override for this event. If one is present, that will be used instead of a default one.
     * @param componentOverride The new component to announce, if an announcement is desired.
     */
    public void setComponentOverride(@Nullable Component componentOverride) {
        this.componentOverride = componentOverride;
    }
}

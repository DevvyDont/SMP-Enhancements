package xyz.devvydont.smprpg.fishing.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import xyz.devvydont.smprpg.fishing.calculator.FishLootCalculator;
import xyz.devvydont.smprpg.fishing.events.FishingLootGenerateEvent;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

public class FishingBehaviorListeners extends ToggleableListener {

    /**
     * Handle for when a player hooks up something. We need to completely override the entity that is attached since
     * our usage of the luck stat completely breaks vanilla fishing drops. We are going to accomplish this by using
     * our fishing calculator and supplying it with a fishing context to do all the work for us.
     * @param event The {@link PlayerFishEvent} that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void __onFish(PlayerFishEvent event) {

        // If the player didn't catch a fish, we don't care. Vanilla is the same up until this point.
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;

        // We caught a fish! This is where our plugin completely takes over. Generate context and calculate.
        var ctx = new FishingContext(event);
        var calculator = new FishLootCalculator();
        var loot = calculator.roll(ctx);

        // This is where things get fucky... We don't have a simple event.setCaught() method.
        // Delete the entity on the fishing hook, and attach a newly generated one.
        if (event.getCaught() != null)
            event.getCaught().remove();

        // Let the plugin know and/or make modifications.
        var _new = loot.Reward().Element().generate(ctx);
        if (_new == null)
            return;
        var fishLootEvent = new FishingLootGenerateEvent(ctx, _new, loot);
        if (!fishLootEvent.callEvent()){
            _new.remove();
            return;
        }

        // The event did not cancel. Did we override an entity? If so, remove the old one and add the new one.
        if (!fishLootEvent.getCaughtEntity().equals(_new)) {
            _new.remove();
            _new = fishLootEvent.getCaughtEntity();
        }

        event.getHook().setHookedEntity(_new);
        event.getHook().pullHookedEntity();  // Also, we need to manually pull the entity to simulate normal fishing physics...
    }

}

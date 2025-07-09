package xyz.devvydont.smprpg.fishing;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

public class FishingBehaviorListeners extends ToggleableListener {

    /**
     * Handle for when a player hooks up something. We need to completely override the entity that is attached since
     * our usage of the luck stat completely breaks vanilla fishing drops. We are going to accomplish this by using
     * our fishing calculator and supplying it with a fishing context to do all the work for us.
     * @param event The {@link PlayerFishEvent} that provides us with relevant context.
     */
    @EventHandler
    private void __onFish(PlayerFishEvent event) {
    }

}

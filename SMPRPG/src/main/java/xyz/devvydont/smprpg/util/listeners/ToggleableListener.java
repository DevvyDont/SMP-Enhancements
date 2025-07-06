package xyz.devvydont.smprpg.util.listeners;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.SMPRPG;

/**
 * A simple extendable class to use for listeners that provide a simple .start() and .stop() method that
 * enables/disables the listener behavior. This makes it much more obvious what your listener is doing at a glance.
 */
public abstract class ToggleableListener implements Listener {

    private boolean enabled;

    /**
     * Start this listener so that events start firing to any EventHandlers.
     * Calling start() multiple times in a row does nothing.
     */
    public void start() {

        if (enabled)
            return;

        SMPRPG.getInstance().getServer().getPluginManager().registerEvents(this, SMPRPG.getInstance());
        enabled = true;
    }

    /**
     * Stop this listener so that it will no longer handle any EventHandlers.
     * Calling stop() multiple times in a row does nothing.
     */
    public void stop() {
        if (!enabled)
            return;

        HandlerList.unregisterAll(this);
        enabled = false;
    }

    /**
     * Check if this listener is handling any EventHandlers that it defines.
     * @return true if this listener is handling events, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }
}

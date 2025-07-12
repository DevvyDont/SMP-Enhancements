package xyz.devvydont.smprpg.services;

import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.listeners.crafting.CraftingTransmuteUpgradeFix;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all recipe logic on the server. This includes crafting, smelting, etc.
 */
public class RecipeService implements IService, Listener {

    private final List<ToggleableListener> listeners = new ArrayList<>();

    @Override
    public void setup() throws RuntimeException {

        // Start listeners.
        listeners.add(new CraftingTransmuteUpgradeFix());
        for (var listener : listeners)
            listener.start();
    }

    @Override
    public void cleanup() {
        for (var listener : listeners)
            listener.stop();
    }
}

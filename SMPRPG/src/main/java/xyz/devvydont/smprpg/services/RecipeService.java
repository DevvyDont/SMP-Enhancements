package xyz.devvydont.smprpg.services;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ShapedRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.listeners.crafting.CraftingTransmuteUpgradeFix;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all recipe logic on the server. This includes crafting, smelting, etc.
 */
public class RecipeService implements IService, Listener {

    public static final NamespacedKey KEY = new NamespacedKey("smprpg", "test_upgrade_recipe");
    public static ShapedRecipe RECIPE;

    private final List<ToggleableListener> listeners = new ArrayList<>();

    @Override
    public void setup() throws RuntimeException {
        RECIPE = new ShapedRecipe(KEY, ItemService.generate(CustomItemType.NEBULA_ROD));
        RECIPE.shape(" a ", "ara");
        RECIPE.setIngredient('a', ItemService.generate(CustomItemType.STOLEN_APPLE));
        RECIPE.setIngredient('r', ItemService.generate(CustomItemType.COMET_ROD));
        Bukkit.addRecipe(RECIPE);

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

    @EventHandler
    public void onBreakDirt(BlockBreakEvent event) {
        event.getPlayer().discoverRecipe(KEY);
    }

}

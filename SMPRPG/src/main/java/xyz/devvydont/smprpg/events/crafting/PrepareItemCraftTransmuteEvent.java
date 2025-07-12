package xyz.devvydont.smprpg.events.crafting;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An extension off of the {@link org.bukkit.event.inventory.PrepareItemCraftEvent} event.
 * This event only fires when the above event is fired and a potential candidate for an item being transmuted
 * into another one is possible. The logic had to be hacked in since the Recipe API sucks, but if you need to listen
 * to the event and modify it before it occurs you can use this event.
 */
public class PrepareItemCraftTransmuteEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private boolean cancelled;
    private @NotNull final Recipe recipe;
    private @NotNull final CraftingInventory inventory;
    private @NotNull final InventoryView view;
    private @NotNull final ItemStack transmuteIngredient;
    private @Nullable ItemStack recipeResult;

    public PrepareItemCraftTransmuteEvent(@NotNull Recipe recipe, @NotNull CraftingInventory inventory,
                                          @NotNull InventoryView view, @NotNull ItemStack transmuteIngredient,
                                          @NotNull ItemStack result) {
        this.recipe = recipe;
        this.inventory = inventory;
        this.view = view;
        this.transmuteIngredient = transmuteIngredient;
        this.recipeResult = result;
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

    /**
     * Get the recipe that is causing this transmute event.
     * @return The recipe that is responsible for the transmute event.
     */
    public @NotNull Recipe getRecipe() {
        return recipe;
    }

    /**
     * Get the inventory that is relative to this event.
     * @return A crafting inventory.
     */
    public @NotNull CraftingInventory getInventory() {
        return inventory;
    }

    /**
     * Get the view that is relative to this event.
     * @return The inventory view.
     */
    public @NotNull InventoryView getView() {
        return view;
    }

    /**
     * Get the ingredient that is being transmuted. This item *should* have its data copied to the new item.
     * @return The item that's being transmuted.
     */
    public @NotNull ItemStack getTransmuteIngredient() {
        return transmuteIngredient;
    }

    /**
     * The result of the recipe. If it's null, that means the item will not force update itself in the original event.
     * @return The item to force set as a result in the crafting grid.
     */
    public @Nullable ItemStack getRecipeResult() {
        return recipeResult;
    }

    /**
     * Set the result to force as a result of the craft. Can be set to null to cancel the craft.
     * @param recipeResult The new item to make the recipe craft.
     */
    public void setRecipeResult(@Nullable ItemStack recipeResult) {
        this.recipeResult = recipeResult;
    }
}

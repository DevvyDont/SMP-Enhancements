package xyz.devvydont.smprpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemQuantityRollDropEvent extends Event implements Cancellable {


    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull private final Player player;
    @Nullable private final ItemStack tool;
    @Nullable private final ItemStack offhand;
    private final ItemStack drop;
    private final int min;
    private final int max;
    private final int initialAmount;
    private int amount;
    private boolean isCancelled = false;

    public CustomItemQuantityRollDropEvent(@NotNull Player player, @Nullable ItemStack tool, int min, int max, int initialAmount, ItemStack drop) {
        this.player = player;
        this.tool = tool;
        this.offhand = player.getInventory().getItemInOffHand();
        this.min = min;
        this.max = max;
        this.initialAmount = initialAmount;
        this.amount = initialAmount;
        this.drop = drop;
    }

    /**
     * The player involved with this drop roll
     *
     * @return
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * The tool used to trigger this rolling event
     *
     * @return
     */
    public @Nullable ItemStack getTool() {
        return tool;
    }

    /**
     * Returns the item currently held in the offhand
     *
     * @return
     */
    public @Nullable ItemStack getOffhand() {
        return offhand;
    }

    /**
     * The drop to be potentially dropped
     *
     * @return
     */
    public ItemStack getDrop() {
        return drop.clone();
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * The initial unchanged drop amount
     *
     * @return
     */
    public double getInitialAmount() {
        return initialAmount;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}

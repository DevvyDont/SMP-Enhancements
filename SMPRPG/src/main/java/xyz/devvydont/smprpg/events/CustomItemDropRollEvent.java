package xyz.devvydont.smprpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemDropRollEvent extends Event {


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
    private final double initialChance;
    private double chance;

    public CustomItemDropRollEvent(@NotNull Player player, @Nullable ItemStack tool, double initialChance, ItemStack drop) {
        this.player = player;
        this.tool = tool;
        this.offhand = player.getInventory().getItemInOffHand();
        this.initialChance = initialChance;
        this.chance = initialChance;
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

    /**
     * The initial unchanged drop chance
     *
     * @return
     */
    public double getInitialChance() {
        return initialChance;
    }

    /**
     * The chance so far that this roll is gonna use
     *
     * @return
     */
    public double getChance() {
        return chance;
    }

    /**
     * Update the drop chance of this event
     *
     * @param chance
     */
    public void setChance(double chance) {
        this.chance = chance;
    }

}

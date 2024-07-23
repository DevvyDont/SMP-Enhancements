package xyz.devvydont.smprpg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.util.items.LootSource;

// Called when a player successfully rolls for an item from a ChancedItemDrop roll
public class CustomChancedItemDropSuccessEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private final Player player;
    private final double chance;
    private final ItemStack item;
    private final LootSource source;

    public CustomChancedItemDropSuccessEvent(@NotNull Player player, double chance, ItemStack drop, LootSource source) {
        this.player = player;
        this.chance = chance;
        this.item = drop;
        this.source = source;
    }

    public Player getPlayer() {
        return player;
    }

    public double getChance() {
        return chance;
    }

    public LootSource getSource() {
        return source;
    }

    public String getFormattedChance() {
        return String.format("%.4f%%", chance*100);
    }

    public ItemStack getItem() {
        return item;
    }
}

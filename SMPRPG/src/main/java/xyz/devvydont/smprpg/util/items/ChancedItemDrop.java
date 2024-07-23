package xyz.devvydont.smprpg.util.items;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.events.CustomChancedItemDropSuccessEvent;
import xyz.devvydont.smprpg.events.CustomItemDropRollEvent;

import java.util.Collection;
import java.util.List;

public class ChancedItemDrop implements LootDrop {

    // 1/x chance, higher number means lower chance
    private final int rarity;
    private final ItemStack item;
    private final LootSource source;

    public ChancedItemDrop(ItemStack item, int rarity, LootSource source) {
        this.rarity = rarity;
        this.item = item;
        this.source = source;
    }

    public ItemStack getItemCopy() {
        return item.clone();
    }

    public int getRarity() {
        return rarity;
    }

    public double getChance() {
        return 1.0 / rarity;
    }

    public LootSource getSource() {
        return source;
    }

    public Component getReason() {
        return source.getAsComponent();
    }

    /**
     * Call to roll this drop for a player and use a tool that was used, tool can be null to not consider
     * enchants or any other stat modifications
     * returns a list of items stacks from this roll. Null if none
     *
     * @param player
     * @param tool
     * @return
     */
    @Override
    @Nullable
    public Collection<ItemStack> roll(Player player, @Nullable ItemStack tool) {

        // Make an event for this drop chance to allow the plugin to modify it in other places
        CustomItemDropRollEvent event = new CustomItemDropRollEvent(player, tool, getChance(), item);
        event.callEvent();

        // RNG roll
        if (Math.random() < event.getChance()) {
            ItemStack reward = getItemCopy();
            new CustomChancedItemDropSuccessEvent(player, event.getChance(), reward, getSource()).callEvent();
            return List.of(reward);
        }

        // Any other sort of checks needed
        //...

        // Failed
        return null;
    }

}

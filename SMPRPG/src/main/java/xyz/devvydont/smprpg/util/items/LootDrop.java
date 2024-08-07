package xyz.devvydont.smprpg.util.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface LootDrop {

    /**
     * Call to roll this drop for a player and use a tool that was used, tool can be null to not consider
     * enchants or any other stat modifications
     * returns a list of items stacks from this roll. Null if none
     *
     * @param player
     * @param tool
     * @return
     */
    @Nullable
    Collection<ItemStack> roll(Player player, @Nullable ItemStack tool, double chanceDecay);

}

package xyz.devvydont.smprpg.util.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.events.CustomItemQuantityRollDropEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuantityLootDrop implements LootDrop {

    private ItemStack item;
    private int min;
    private int max;
    private LootSource source;

    public QuantityLootDrop(ItemStack item, int min, int max, LootSource source) {
    }

    @Override
    public @Nullable Collection<ItemStack> roll(Player player, @Nullable ItemStack tool) {
        int count = min;
        int possibleExtra = max - min;
        count += (int) (Math.random() * (possibleExtra+1));

        CustomItemQuantityRollDropEvent event = new CustomItemQuantityRollDropEvent(player, tool, min, max, count, item);
        event.callEvent();
        if (event.isCancelled())
            return null;
        count = event.getAmount();

        List<ItemStack> drops = new ArrayList<>();
        for (int i = 0; i < count; i++)
            drops.add(item.clone());
        return drops;
    }

}

package xyz.devvydont.smprpg.loot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomLootTable {

    private final Collection<LootTableMember> table;

    public CustomLootTable(LootTableMember...elements) {

        if (elements.length == 0)
            throw new IllegalArgumentException("You cannot create an empty table!");

        this.table = List.of(elements);
    }

    public Collection<LootTableMember> getTable() {
        return table;
    }

    public Collection<ItemStack> rollItems(Player player, int limit) {

        List<ItemStack> results = new ArrayList<>();

        // Loop through every possible drop in this table
        for (LootTableMember member : table) {

            // Perform a roll for every drop we have
            for (int rollNumber = 0; rollNumber < member.getRolls(); rollNumber++) {
                ItemStack roll = member.roll(player);

                // If we failed the roll go to the next iteration
                if (roll == null)
                    continue;

                results.add(roll);

                // Have we hit the limit?
                if (results.size() >= limit)
                    return results;
            }
        }

        return results;
    }
}

package xyz.devvydont.smprpg.gui.enchantments;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents the menu that is displayed when specific enchantment details are queried using the EnchantmentMenu.
 * This sub menu displays data for a specific enchantment.
 */
public class EnchantmentSubMenu extends MenuBase {

    public static final int ROWS = 5;
    public static final int[] ALL_SLOTS = {10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34};

    // The enchantment we are expanding details on
    private final CustomEnchantment enchantment;

    public EnchantmentSubMenu(@NotNull Player player, MenuBase parentMenu, CustomEnchantment enchantment) {
        super(player, ROWS, parentMenu);
        this.enchantment = enchantment;
    }

    public ItemStack createEnchantmentButton(int level) {
        ItemStack item = createNamedItem(Material.ENCHANTED_BOOK, enchantment.getEnchantment().displayName(level).color(enchantment.getEnchantColor()));
        item.editMeta(meta -> {
            meta.lore(ComponentUtils.cleanItalics(List.of(
                    enchantment.build(level).getDescription()
            )));
        });
        return item;
    }

    /**
     * Generates a list of inventory indexes to populate that aligns with the max level of the enchantment.
     * Handles the ugly logic on which slots to use based on what the max level of the enchantment is.
     * @return A list of integers representing inventory index slots.
     */
    private List<Integer> getIndexesToPopulate() {
        List<Integer> indexes = new ArrayList<>();

        // Sanity checks, is there no "levels" to display?
        if (enchantment.getMaxLevel() <= 0)
            return indexes;

        // Is the max level too big for us to show? Our UI only supports 21 levels
        if (enchantment.getMaxLevel() > 21) {
            for (Integer slot : ALL_SLOTS)
                indexes.add(slot);
            return indexes;
        }

        // todo figure out big brain centering logic so it looks pretty, for now i am gonna be a moron and just give numbers in order
        for (int level = 1; level <= enchantment.getMaxLevel(); level++)
            indexes.add(ALL_SLOTS[level-1]);

        return indexes;
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        super.handleInventoryClicked(event);
        event.setCancelled(true);
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(enchantment.getDisplayName());
        this.setBorderFull();

        // Populate slots with the levels of the enchantment.
        List<Integer> indexes = getIndexesToPopulate();
        for (int level = 1; level <= enchantment.getMaxLevel(); level++)
            this.setSlot(indexes.get(level-1), createEnchantmentButton(level));

        this.setButton((ROWS-1)*9+4, BUTTON_BACK, (e) -> this.openParentMenu());
    }

}

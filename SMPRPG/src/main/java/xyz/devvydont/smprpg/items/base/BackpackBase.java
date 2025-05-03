package xyz.devvydont.smprpg.items.base;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemContainerContents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.IFooterDescribable;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.IItemContainer;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BackpackBase extends CustomItemBlueprint implements IHeaderDescribable, IFooterDescribable, IItemContainer {

    public BackpackBase(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    private static NamedTextColor determineColor(int count, int max) {
        var ratio = (double) count / max;
        if (ratio <= .5)
            return NamedTextColor.GREEN;
        if (ratio <= .75)
            return NamedTextColor.YELLOW;
        if (ratio <= .9)
            return NamedTextColor.GOLD;
        if (ratio < 1)
            return NamedTextColor.RED;
        return NamedTextColor.DARK_RED;
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                ComponentUtils.merge(
                        ComponentUtils.create("Stack size: "),
                        ComponentUtils.create("" + this.getStackSize(), NamedTextColor.GREEN)
                ),
                ComponentUtils.merge(
                        ComponentUtils.create("Slots: "),
                        ComponentUtils.create("" + this.getSlots(), NamedTextColor.GREEN)
                )
        );
    }

    @Override
    public List<Component> getFooter(ItemStack itemStack) {
        var items = getStoredItems(itemStack, false);
        var count = 0;
        for (var item : items)
            count += item.getAmount();
        var maxItems = getSlots() * getStackSize();
        return List.of(
                ComponentUtils.create("Currently storing:", NamedTextColor.GOLD),
                ComponentUtils.merge(
                        ComponentUtils.create("* Types: "),
                        ComponentUtils.create("" + items.size(), determineColor(items.size(), getSlots())),
                        ComponentUtils.create("/" + getSlots(), NamedTextColor.DARK_GRAY)
                ),
                ComponentUtils.merge(
                        ComponentUtils.create("* Total: "),
                        ComponentUtils.create("" + count, determineColor(count, maxItems)),
                        ComponentUtils.create("/" + maxItems, NamedTextColor.DARK_GRAY)
                ),
                ComponentUtils.create("Right click while holding to view!", NamedTextColor.DARK_GRAY)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.STORAGE;
    }

    public List<ItemStack> initialize(ItemStack backpack) {

        // Create enough air items to fill all the slots.
        var items = new ArrayList<ItemStack>();
        for (var i = 0; i < getSlots(); i++)
            items.add(ItemStack.of(Material.AIR));

        // Set the stored contents.
        backpack.setData(DataComponentTypes.CONTAINER, ItemContainerContents.containerContents(items));
        return items;
    }

    @Override
    public List<ItemStack> getStoredItems(ItemStack container, boolean includeEmpty) {

        var data = container.getData(DataComponentTypes.CONTAINER);

        // If this item has no stored items, initialize it.
        List<ItemStack> items;
        if (data == null || data.contents().isEmpty())
            items = initialize(container);
        else
            items = new ArrayList<>(data.contents());

        // Fill empty spaces with air.
        while (items.size() < this.getSlots())
            items.add(ItemStack.of(Material.AIR));

        // Looks like there's items, we can return them. If we want the items as is, we can return it now.
        if (includeEmpty)
            return items;

        // Filter out air.
        return items.stream().filter(item -> !item.getType().equals(Material.AIR)).toList();
    }

    @Override
    public void setStoredItems(ItemStack container, List<ItemStack> items) {
        container.setData(DataComponentTypes.CONTAINER, ItemContainerContents.containerContents(items));
        updateItemData(container);
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        super.updateItemData(itemStack);
        itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, 1);  // Do not stack these items.
    }
}

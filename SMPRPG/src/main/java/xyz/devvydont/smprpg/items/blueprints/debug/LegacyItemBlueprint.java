package xyz.devvydont.smprpg.items.blueprints.debug;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.List;

/*
 * In the event that an item key gets changed, define a fallback for items so that in an already estabilished server
 * setting, an item can keep its metadata safely to be swapped for an "updated" one, or just remain as a relic of
 * the past.
 */
public class LegacyItemBlueprint extends CustomItemBlueprint implements IHeaderDescribable {

    public LegacyItemBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    private String resolveItemName(ItemMeta meta) {
        String key = itemService.getItemKey(meta);

        if (key == null)
            return "???";
        return key;
    }

    /**
     * Override the updateMeta method to not actually modify any item properties except the lore to show our
     * message.
     */
    public void updateMeta(ItemStack item) {
        item.editMeta(meta -> meta.displayName(getNameComponent(item)));
    }

    @Override
    public void updateMeta(ItemMeta meta) {

    }

    @Override
    public String getItemName(ItemStack item) {
        return resolveItemName(item.getItemMeta()) + " (Legacy)";
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                ComponentUtils.create("This item is considered legacy."),
                ComponentUtils.EMPTY,
                ComponentUtils.create("The item type tag key has either"),
                ComponentUtils.create("been removed or changed, meaning"),
                ComponentUtils.create("this item does not know what it is"),
                ComponentUtils.create("anymore. If you believe this is an"),
                ComponentUtils.create("error, contact a server admin."),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Item Key: ").append(ComponentUtils.create(resolveItemName(itemStack.getItemMeta()), NamedTextColor.GREEN))
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }
}

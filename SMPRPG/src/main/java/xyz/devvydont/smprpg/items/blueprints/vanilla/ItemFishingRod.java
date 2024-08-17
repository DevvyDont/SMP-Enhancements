package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class ItemFishingRod extends VanillaAttributeItem implements ToolBreakable {

    public ItemFishingRod(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public int getMaxDurability() {
        return 250;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of();
    }

    @Override
    public int getPowerRating() {
        return 3;
    }
}

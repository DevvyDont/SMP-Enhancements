package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class ItemShears extends VanillaAttributeItem implements ToolBreakable {

    public ItemShears(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 5),
                new MultiplicativeAttributeEntry(Attribute.GENERIC_ATTACK_SPEED, -.1)
        );
    }

    @Override
    public int getPowerRating() {
        return 2;
    }

    @Override
    public int getMaxDurability() {
        return 1_000;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOOL;
    }
}

package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.Shieldable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class ItemShield extends VanillaAttributeItem implements Shieldable, ToolBreakable {

    public ItemShield(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, .25)
        );
    }

    @Override
    public int getPowerRating() {
        return 7;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.EQUIPMENT;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public double getDamageBlockingPercent() {
        return .80;
    }

    @Override
    public int getShieldDelay() {
        return 10;
    }

    @Override
    public int getMaxDurability() {
        return 1000;
    }
}

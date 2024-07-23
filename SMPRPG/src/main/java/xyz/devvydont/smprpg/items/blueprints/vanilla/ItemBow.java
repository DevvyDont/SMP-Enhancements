package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class ItemBow extends VanillaAttributeItem implements ToolBreakable {

    public ItemBow(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOW;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ATTACK_DAMAGE, 10.0)
        );
    }

    @Override
    public int getPowerRating() {
        return 5;
    }


    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getMaxDurability() {
        return 25_000;
    }
}

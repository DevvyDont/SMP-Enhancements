package xyz.devvydont.smprpg.items.blueprints.armor.singularity;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Wearable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SingularityHelmet extends CustomAttributeItem implements Wearable, ToolBreakable {


    public SingularityHelmet(ItemService itemService) {
        super(itemService);
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.SINGULARITY_HELMET;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ARMOR;
    }

    @Override
    public int getPowerRating() {
        return 100;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR, 6),
                new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR_TOUGHNESS, 250),
                new AdditiveAttributeEntry(Attribute.GENERIC_MAX_HEALTH, 200),
                new AdditiveAttributeEntry(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 2),
                new ScalarAttributeEntry(Attribute.GENERIC_MOVEMENT_SPEED, .25)
        );
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getMaxDurability() {
        return 100_000_000;
    }
}

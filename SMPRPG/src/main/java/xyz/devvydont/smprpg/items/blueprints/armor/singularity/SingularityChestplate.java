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
import xyz.devvydont.smprpg.items.interfaces.Wearable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SingularityChestplate extends CustomAttributeItem implements Wearable {
    
    
    public SingularityChestplate(ItemService itemService) {
        super(itemService);
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.SINGULARITY_CHESTPLATE;
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
                new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR_TOUGHNESS, 4),
                new AdditiveAttributeEntry(Attribute.GENERIC_MAX_HEALTH, 400),
                new AdditiveAttributeEntry(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 2),
                new ScalarAttributeEntry(Attribute.GENERIC_MOVEMENT_SPEED, .25)
        );
    }

    @Override
    public int getDefense() {
        return 310;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }
}

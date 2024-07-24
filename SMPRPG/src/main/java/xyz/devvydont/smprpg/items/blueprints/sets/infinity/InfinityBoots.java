package xyz.devvydont.smprpg.items.blueprints.sets.infinity;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.Wearable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class InfinityBoots extends CustomAttributeItem implements Wearable {


    public InfinityBoots(ItemService itemService) {
        super(itemService);
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
        meta.setUnbreakable(true);
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.INFINITY_BOOTS;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ARMOR;
    }

    @Override
    public int getPowerRating() {
        return 1000;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR, 30),
                new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR_TOUGHNESS, 10000),
                new AdditiveAttributeEntry(Attribute.GENERIC_MAX_HEALTH, 1000),
                new AdditiveAttributeEntry(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 2)
        );
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }
}

package xyz.devvydont.smprpg.items.blueprints.armor.infinity;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
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
import java.util.Map;

public class InfinityChestplate extends CustomAttributeItem implements Wearable {


    public InfinityChestplate(ItemService itemService) {
        super(itemService);
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
        meta.setUnbreakable(true);
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.INFINITY_CHESTPLATE;
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
                new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR_TOUGHNESS, 20),
                new AdditiveAttributeEntry(Attribute.GENERIC_MAX_HEALTH, 2500),
                new AdditiveAttributeEntry(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 2)
        );
    }

    @Override
    public int getDefense() {
        return 25000;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.infinity;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class InfinityChestplate extends CustomAttributeItem {


    public InfinityChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
        meta.setUnbreakable(true);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public int getPowerRating() {
        return 1000;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(Attribute.ARMOR, 30),
                new AdditiveAttributeEntry(Attribute.ARMOR_TOUGHNESS, 20000),
                new AdditiveAttributeEntry(Attribute.MAX_HEALTH, 20000),
                new ScalarAttributeEntry(Attribute.KNOCKBACK_RESISTANCE, .2)
        );
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }
}

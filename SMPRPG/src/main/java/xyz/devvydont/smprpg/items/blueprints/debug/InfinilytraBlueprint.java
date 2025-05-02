package xyz.devvydont.smprpg.items.blueprints.debug;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeTotem;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class InfinilytraBlueprint extends CustomFakeTotem {

    public InfinilytraBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public void updateMeta(ItemStack itemStack) {
        super.updateMeta(itemStack);
        itemStack.addUnsafeEnchantment(EnchantmentService.KEEPING_BLESSING.getEnchantment(), 1);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of();
    }

    @Override
    public int getPowerRating() {
        return 1;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.CHEST;
    }
}

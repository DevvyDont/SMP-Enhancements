package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.IWaterRod;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class ItemFishingRod extends VanillaAttributeItem implements IBreakableEquipment, IWaterRod {

    public ItemFishingRod(ItemService itemService, Material material) {
        super(itemService, material);
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
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of();
    }

    @Override
    public int getPowerRating() {
        return 3;
    }
}

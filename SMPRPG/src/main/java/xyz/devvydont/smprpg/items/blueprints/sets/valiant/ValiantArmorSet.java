package xyz.devvydont.smprpg.items.blueprints.sets.valiant;

import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class ValiantArmorSet extends CustomAttributeItem implements IBreakableEquipment {

    public ValiantArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getPowerRating() {
        return 80;
    }

    @Override
    public int getMaxDurability() {
        return 80_000;
    }
}

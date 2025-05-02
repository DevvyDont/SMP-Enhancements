package xyz.devvydont.smprpg.items.blueprints.sets.prelude;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ITrimmable;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class PreludeArmorSet extends CustomAttributeItem implements ITrimmable, IBreakableEquipment {

    public PreludeArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getPowerRating() {
        return 100;
    }

    @Override
    public int getMaxDurability() {
        return 120_000;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SNOUT;
    }
}

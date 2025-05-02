package xyz.devvydont.smprpg.items.blueprints.sets.prelude;

import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class PreludeArmorSet extends CustomArmorBlueprint implements Trimmable, IBreakableEquipment {

    public PreludeArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
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

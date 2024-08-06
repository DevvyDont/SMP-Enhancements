package xyz.devvydont.smprpg.items.blueprints.sets.radiant;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class RadiantArmorSet extends CustomArmorBlueprint implements ToolBreakable, Trimmable {


    public RadiantArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.GOLD;
    }

    @Override
    public int getPowerRating() {
        return 35;
    }

    @Override
    public int getMaxDurability() {
        return 15_000;
    }
}

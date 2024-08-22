package xyz.devvydont.smprpg.items.blueprints.sets.snowfall;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class SnowfallArmorSet extends CustomArmorBlueprint implements ToolBreakable, Trimmable {

    public SnowfallArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getPowerRating() {
        return 70;
    }

    @Override
    public int getMaxDurability() {
        return 75_000;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.DIAMOND;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.FLOW;
    }
}

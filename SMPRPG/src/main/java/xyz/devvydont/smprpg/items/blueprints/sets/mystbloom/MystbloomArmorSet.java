package xyz.devvydont.smprpg.items.blueprints.sets.mystbloom;

import org.bukkit.Color;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class MystbloomArmorSet extends CustomArmorBlueprint implements Dyeable, Trimmable, ToolBreakable {

    public MystbloomArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getPowerRating() {
        return 20;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0xf38baa);
    }

    @Override
    public int getMaxDurability() {
        return 20_000;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.AMETHYST;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }
}

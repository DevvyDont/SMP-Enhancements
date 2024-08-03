package xyz.devvydont.smprpg.items.blueprints.sets.quartz;

import org.bukkit.Color;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.services.ItemService;

public class QuartzChestplate extends QuartzArmorSet implements Dyeable {

    public QuartzChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.QUARTZ;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.RAISER;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0xf9fff3);
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.neptune;

import org.bukkit.Color;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class NeptuneHelmet extends NeptuneArmorSet {

    public NeptuneHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 150;
    }

    @Override
    public int getHealth() {
        return 50;
    }

    @Override
    public int getStrength() {
        return 5;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x3ab3da);
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.QUARTZ;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }
}

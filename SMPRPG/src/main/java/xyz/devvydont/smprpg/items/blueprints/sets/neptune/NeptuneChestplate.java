package xyz.devvydont.smprpg.items.blueprints.sets.neptune;

import org.bukkit.Color;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class NeptuneChestplate extends NeptuneArmorSet {

    public NeptuneChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 275;
    }

    @Override
    public int getHealth() {
        return 80;
    }

    @Override
    public int getStrength() {
        return 25;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x99d9ec);
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.EMERALD;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }
}

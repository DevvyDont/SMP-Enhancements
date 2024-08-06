package xyz.devvydont.smprpg.items.blueprints.sets.undead;

import org.bukkit.Color;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;

public class UndeadChestplate extends UndeadArmorSet implements Trimmable {

    public UndeadChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x835432);
    }

    @Override
    public int getDefense() {
        return 40;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.EMERALD;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.TIDE;
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.sakura;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;

public class SakuraLeggings extends SakuraArmorSet implements Trimmable {

    public SakuraLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.COPPER;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.RAISER;
    }
}

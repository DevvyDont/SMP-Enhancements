package xyz.devvydont.smprpg.items.blueprints.sets.valiant;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;

public class ValiantChestplate extends ValiantArmorSet implements Trimmable {

    public ValiantChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.GOLD;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SENTRY;
    }
}

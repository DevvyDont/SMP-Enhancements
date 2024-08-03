package xyz.devvydont.smprpg.items.blueprints.sets.prelude;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class PreludeHelmet extends PreludeArmorSet {

    public PreludeHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.REDSTONE;
    }
}

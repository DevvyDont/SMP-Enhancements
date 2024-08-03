package xyz.devvydont.smprpg.items.blueprints.sets.prelude;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class PreludeBoots extends PreludeArmorSet {

    public PreludeBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.AMETHYST;
    }
}

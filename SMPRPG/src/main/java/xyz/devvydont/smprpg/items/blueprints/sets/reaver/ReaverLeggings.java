package xyz.devvydont.smprpg.items.blueprints.sets.reaver;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class ReaverLeggings extends ReaverArmorSet {


    public ReaverLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.NETHERITE;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.quartz;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class QuartzHelmet extends QuartzArmorSet {

    public QuartzHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 110;
    }

    @Override
    public int getHealth() {
        return 35;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.QUARTZ;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.RAISER;
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.smite;

import org.bukkit.Material;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.services.ItemService;

public class SmiteBoots extends SmiteArmorSet {

    public SmiteBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.DIAMOND;
    }

    @Override
    public int getDefense() {
        return ItemArmor.getDefenseFromMaterial(Material.DIAMOND_BOOTS) - 10;
    }
}

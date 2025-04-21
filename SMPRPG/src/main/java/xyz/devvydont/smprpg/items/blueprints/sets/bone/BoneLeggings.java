package xyz.devvydont.smprpg.items.blueprints.sets.bone;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

public class BoneLeggings extends BoneArmorSet {

    public BoneLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return ItemArmor.getDefenseFromMaterial(Material.GOLDEN_LEGGINGS);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, itemService.getCustomItem(Material.BONE), generate()).build();
    }
}

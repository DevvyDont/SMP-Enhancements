package xyz.devvydont.smprpg.items.blueprints.sets.emerald;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

public class EmeraldLeggings extends EmeraldArmorSet {

    public EmeraldLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, itemService.getCustomItem(EmeraldArmorSet.INGREDIENT), generate()).build();
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public double getDefense() {
        return ItemArmor.getDefenseFromMaterial(Material.DIAMOND_LEGGINGS)+5;
    }

    @Override
    public double getHealth() {
        return (int) (ItemArmor.getHealthFromMaterial(Material.DIAMOND_LEGGINGS)+5);
    }

    @Override
    public double getStrength() {
        return ItemArmor.getDamageFromMaterial(Material.NETHERITE_LEGGINGS);
    }
}

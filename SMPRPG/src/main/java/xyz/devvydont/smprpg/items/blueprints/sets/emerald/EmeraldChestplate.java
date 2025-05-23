package xyz.devvydont.smprpg.items.blueprints.sets.emerald;

import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

public class EmeraldChestplate extends EmeraldArmorSet {

    public EmeraldChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ChestplateRecipe(this, itemService.getCustomItem(EmeraldArmorSet.INGREDIENT), generate()).build();
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public int getDefense() {
        return 55;
    }

    @Override
    public int getHealth() {
        return 20;
    }

    @Override
    public int getStrength() {
        return 20;
    }
}

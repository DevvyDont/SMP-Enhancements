package xyz.devvydont.smprpg.items.blueprints.sets.emerald;

import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

public class EmeraldHelmet extends EmeraldArmorSet {

    public EmeraldHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HelmetRecipe(this, itemService.getCustomItem(EmeraldArmorSet.INGREDIENT), generate()).build();
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public int getDefense() {
        return 100;
    }

    @Override
    public int getHealth() {
        return 15;
    }

    @Override
    public int getStrength() {
        return 10;
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.forsaken;

import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

public class ForsakenLeggings extends ForsakenArmorSet {

    public ForsakenLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 65;
    }

    @Override
    public int getHealth() {
        return 15;
    }

    @Override
    public double getStrength() {
        return .45;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, ItemService.getItem(ForsakenHelmet.CRAFTING_COMPONENT), generate()).build();
    }
}

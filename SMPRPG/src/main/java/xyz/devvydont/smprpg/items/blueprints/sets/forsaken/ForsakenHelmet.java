package xyz.devvydont.smprpg.items.blueprints.sets.forsaken;

import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

public class ForsakenHelmet extends ForsakenArmorSet {

    public ForsakenHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 60;
    }

    @Override
    public int getHealth() {
        return 15;
    }

    @Override
    public double getStrength() {
        return .4;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HelmetRecipe(this, ItemService.generate(ForsakenHelmet.CRAFTING_COMPONENT), generate()).build();
    }
}

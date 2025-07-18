package xyz.devvydont.smprpg.items.blueprints.sets.forsaken;

import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

public class ForsakenLeggings extends ForsakenArmorSet {

    public static final int DEFENSE = 140;
    public static final int HEALTH = 20;
    public static final double STRENGTH = .35;

    public ForsakenLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return DEFENSE;
    }

    @Override
    public int getHealth() {
        return HEALTH;
    }

    @Override
    public double getStrength() {
        return STRENGTH;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, ItemService.generate(ForsakenHelmet.CRAFTING_COMPONENT), generate()).build();
    }
}

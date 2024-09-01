package xyz.devvydont.smprpg.items.blueprints.sets.mystic;

import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

public class LuxeLeggings extends LuxeArmorSet {

    public LuxeLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 15;
    }

    @Override
    public int getHealth() {
        return 20;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, itemService.getCustomItem(LuxeArmorSet.ingredient), generate()).build();
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }
}

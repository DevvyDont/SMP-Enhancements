package xyz.devvydont.smprpg.items.blueprints.sets.quartz;

import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

public class QuartzLeggings extends QuartzArmorSet {

    public QuartzLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 165;
    }

    @Override
    public int getHealth() {
        return 50;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.QUARTZ;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.EYE;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, itemService.getCustomItem(CustomItemType.ENCHANTED_QUARTZ), generate()).build();
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.slimy;

import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

public class SlimyLeggings extends SlimyArmorSet {

    public SlimyLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 10;
    }

    @Override
    public int getHealth() {
        return 40;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, itemService.getCustomItem(CustomItemType.PREMIUM_SLIME), generate()).build();
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.EMERALD;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }
}

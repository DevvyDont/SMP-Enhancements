package xyz.devvydont.smprpg.items.blueprints.sets.amethyst;

import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

public class AmethystChestplate extends AmethystArmorSet {


    public AmethystChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getHealth() {
        return 40;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.ENCHANTED_AMETHYST), generate()).build();
    }
}

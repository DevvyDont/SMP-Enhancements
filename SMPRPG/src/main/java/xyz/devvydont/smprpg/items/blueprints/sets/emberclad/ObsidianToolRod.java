package xyz.devvydont.smprpg.items.blueprints.sets.emberclad;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class ObsidianToolRod extends CustomItemBlueprint implements ICraftable, ISellable {


    public ObsidianToolRod(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape("o", "o");
        recipe.setIngredient('o', itemService.getCustomItem(CustomItemType.ENCHANTED_OBSIDIAN));
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.OBSIDIAN)
        );
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return 5000 * itemStack.getAmount();
    }
}

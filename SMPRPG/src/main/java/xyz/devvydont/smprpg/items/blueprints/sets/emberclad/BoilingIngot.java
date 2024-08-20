package xyz.devvydont.smprpg.items.blueprints.sets.emberclad;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class BoilingIngot extends CustomItemBlueprint implements Craftable, Sellable {


    public BoilingIngot(ItemService itemService, CustomItemType type) {
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
        recipe.shape("ppp", "pip", "ppp");
        recipe.setIngredient('p', itemService.getCustomItem(CustomItemType.ENCHANTED_BLAZE_ROD));
        recipe.setIngredient('i', itemService.getCustomItem(CustomItemType.ENCHANTED_IRON));
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.BLAZE_ROD),
                itemService.getCustomItem(CustomItemType.ENCHANTED_IRON)
        );
    }

    @Override
    public int getWorth() {
        return 12000;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }
}

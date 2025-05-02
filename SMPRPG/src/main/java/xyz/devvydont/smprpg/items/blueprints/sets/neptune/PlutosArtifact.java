package xyz.devvydont.smprpg.items.blueprints.sets.neptune;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomHeadBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class PlutosArtifact extends CustomHeadBlueprint implements ICraftable, ISellable {

    public PlutosArtifact(ItemService itemService, CustomItemType type) {
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
        recipe.shape(
                "fff",
                "fdf",
                "fff"
        );
        recipe.setIngredient('f', itemService.getCustomItem(CustomItemType.PLUTO_FRAGMENT));
        recipe.setIngredient('d', itemService.getCustomItem(Material.DIAMOND));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.PLUTO_FRAGMENT)
        );
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return 20000 * itemStack.getAmount();
    }

}

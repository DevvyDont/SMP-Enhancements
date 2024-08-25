package xyz.devvydont.smprpg.items.blueprints.equipment;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class DesolatedStone extends ReforgeStone implements Craftable, Sellable {

    public DesolatedStone(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.WITHERED;
    }

    @Override
    public int getExperienceCost() {
        return 30;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(
                "ono",
                "nsn",
                "ono"
        );
        recipe.setIngredient('o', ItemService.getItem(CustomItemType.ENCHANTED_OBSIDIAN));
        recipe.setIngredient('n', ItemService.getItem(Material.NETHERITE_INGOT));
        recipe.setIngredient('s', ItemService.getItem(CustomItemType.PREMIUM_NETHER_STAR));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                ItemService.getItem(Material.NETHER_STAR)
        );
    }

    @Override
    public int getWorth() {
        return 50_000;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }
}

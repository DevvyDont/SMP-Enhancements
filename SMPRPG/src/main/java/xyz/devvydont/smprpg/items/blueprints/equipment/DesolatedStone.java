package xyz.devvydont.smprpg.items.blueprints.equipment;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.ICustomTextured;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class DesolatedStone extends ReforgeStone implements ICraftable, ISellable, ICustomTextured {

    public DesolatedStone(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public String getTextureUrl() {
        return "895a05992afa37b3806b81f0003ca617b3c1cbb9170a2309115b9c6a03eb73af";
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
        recipe.setIngredient('o', ItemService.generate(CustomItemType.ENCHANTED_OBSIDIAN));
        recipe.setIngredient('n', ItemService.generate(Material.NETHERITE_INGOT));
        recipe.setIngredient('s', ItemService.generate(CustomItemType.PREMIUM_NETHER_STAR));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                ItemService.generate(Material.NETHER_STAR)
        );
    }

    @Override
    public int getWorth(ItemStack item) {
        return 50_000 * item.getAmount();
    }
}

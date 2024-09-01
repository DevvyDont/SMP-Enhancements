package xyz.devvydont.smprpg.items.blueprints.potion;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.ExperienceThrowable;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class ExperienceBottle extends CustomItemBlueprint implements ExperienceThrowable, Craftable, Sellable {

    @Nullable
    public static ItemStack getCraftingComponent(CustomItemType expBottleType) {
        return switch (expBottleType) {
            case EXPERIENCE_BOTTLE -> ItemService.getItem(Material.LAPIS_LAZULI);
            case LARGE_EXPERIENCE_BOTTLE -> ItemService.getItem(Material.LAPIS_BLOCK);
            case HEFTY_EXPERIENCE_BOTTLE -> ItemService.getItem(CustomItemType.ENCHANTED_LAPIS);
            case GIGANTIC_EXPERIENCE_BOTTLE -> ItemService.getItem(CustomItemType.ENCHANTED_LAPIS_BLOCK);
            case COLOSSAL_EXPERIENCE_BOTTLE -> ItemService.getItem(CustomItemType.LAPIS_SINGULARITY);
            default -> null;
        };
    }

    public static int getExperienceForBottle(CustomItemType bottleType) {
        return switch (bottleType) {
            case EXPERIENCE_BOTTLE -> 20;
            case LARGE_EXPERIENCE_BOTTLE -> 200;
            case HEFTY_EXPERIENCE_BOTTLE -> 2_000;
            case GIGANTIC_EXPERIENCE_BOTTLE -> 20_000;
            case COLOSSAL_EXPERIENCE_BOTTLE -> 200_000;
            default -> 0;
        };
    }

    public ExperienceBottle(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public int getExperience() {
        return getExperienceForBottle(getCustomItemType());
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe =  new ShapedRecipe(getRecipeKey(), generate());

        ItemStack lapis = getCraftingComponent(getCustomItemType());
        if (lapis == null)
            throw new IllegalStateException("You must define an item type for this experience bottle!");

        recipe.shape(
                "l",
                "b"
        );
        recipe.setIngredient('l', lapis);
        recipe.setIngredient('b', itemService.getCustomItem(Material.GLASS_BOTTLE));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.LAPIS_LAZULI)
        );
    }

    @Override
    public int getWorth() {
        return getExperienceForBottle(getCustomItemType());
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }
}

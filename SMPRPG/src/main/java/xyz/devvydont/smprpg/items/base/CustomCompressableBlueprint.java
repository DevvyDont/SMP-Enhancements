package xyz.devvydont.smprpg.items.base;


import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.blueprints.resources.VanillaResource;
import xyz.devvydont.smprpg.items.interfaces.Compressable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CustomCompressableBlueprint extends CustomItemBlueprint implements Compressable, ISellable {

    public CustomCompressableBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    /**
     * Generates a namespaced key to register a recipe name given a member of the compression chain
     *
     * @param member
     * @param compress
     * @return
     */
    private NamespacedKey generateRecipeKey(CompressionRecipeMember member, boolean compress) {
        return new NamespacedKey(SMPRPG.getInstance(), member.generateRecipeName(compress));
    }

    /**
     * Call to register every single compression recipe contained in this blueprint.
     * If a recipe already happens to be registered, then that means it is safe to skip as another item
     * with the same blueprint has already registered for us.
     */
    public Collection<CraftingRecipe> registerCompressionChain() {

        List<CraftingRecipe> recipes = new ArrayList<>();

        // Loop forward through the compression chain. Start at the first element, but don't query the last element.
        List<CompressionRecipeMember> chain = getCompressionFlow();
        for (int i = 0; i < chain.size()-1; i++) {

            CompressionRecipeMember current = chain.get(i);
            CompressionRecipeMember next = chain.get(i+1);

            // Is this recipe already registered?
            NamespacedKey recipeKey = generateRecipeKey(current, true);
            if (SMPRPG.getInstance().getServer().getRecipe(recipeKey) != null)
                continue;

            // Register the current item to craft into the next item by making a shapeless recipe of the defined amount.
            ShapedRecipe recipe = new ShapedRecipe(recipeKey, next.getMaterial().get(itemService));
            if (current.getAmount() == 4)
                recipe.shape("mm", "mm");
            else
                recipe.shape("mmm", "mmm", "mmm");
            recipe.setIngredient('m', current.getMaterial().get(itemService));
            recipe.setCategory(CraftingBookCategory.MISC);
            recipe.setGroup(getCompressionFlow().get(0).getMaterial().key());
            if (SMPRPG.getInstance().getServer().addRecipe(recipe))
                recipes.add(recipe);
        }

        return recipes;
    }

    /**
     * If desired, call this to allow the decompression of items that were compressed. Simply just registers the
     * chain backwards as well so the recipe goes both ways.
     */
    public Collection<CraftingRecipe> registerDecompressionChain() {

        List<CraftingRecipe> recipes = new ArrayList<>();

        // Loop backward through the compression chain. Start at the last element, but don't query the first element.
        List<CompressionRecipeMember> chain = getCompressionFlow();
        for (int i = chain.size()-1; i > 0; i--) {

            CompressionRecipeMember current = chain.get(i);
            CompressionRecipeMember prev = chain.get(i-1);

            // Is this recipe already registered?
            NamespacedKey recipeKey = generateRecipeKey(current, false);
            if (SMPRPG.getInstance().getServer().getRecipe(recipeKey) != null)
                continue;

            // Register the current item to craft into the next item by making a shapeless recipe of the defined amount.
            ItemStack result = prev.getMaterial().get(itemService);
            result.setAmount(prev.getAmount());
            ShapedRecipe recipe = new ShapedRecipe(recipeKey, result);
            recipe.shape("m");
            recipe.setIngredient('m', current.getMaterial().get(itemService));
            recipe.setCategory(CraftingBookCategory.MISC);
            recipe.setGroup(getCompressionFlow().get(0).getMaterial().key());
            if (SMPRPG.getInstance().getServer().addRecipe(recipe))
                recipes.add(recipe);
        }

        return recipes;
    }

    @Override
    public Collection<NamespacedKey> getAllRecipeKeys() {
        List<NamespacedKey> keys = new ArrayList<>();
        for (CompressionRecipeMember member : getCompressionFlow()) {
            keys.add(generateRecipeKey(member, true));
            keys.add(generateRecipeKey(member, false));
        }
        return keys;
    }

    /**
     * Calculate just how compressed this stage of the item is.
     *
     * @return
     */
    public int getCompressedAmount() {

        int amount = 1;

        for (CompressionRecipeMember compressionRecipeMember : getCompressionFlow()) {

            // Did we hit our material?
            if (getCustomItemType().equals(compressionRecipeMember.getMaterial().getCustom()))
                break;

            // Compress further
            amount *= compressionRecipeMember.getAmount();
        }
        return amount;
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        // Get the worth of the worst item in the crafting chain, and multiply it by the compressed amount.
        int amount = getCompressedAmount();
        int worth = 0;
        MaterialWrapper firstMaterial = getCompressionFlow().getFirst().getMaterial();

        // Prevent recursive worth checking.
        if (firstMaterial.isCustom() && isItemOfType(firstMaterial.get(itemService))) {
            SMPRPG.broadcastToOperators(ComponentUtils.create("Missing worth for compressive material " + firstMaterial.name() + "! Defaulting to 1. This needs to be fixed in code and is considered a severe error.", NamedTextColor.RED));
            return itemStack.getAmount();
        }

        // If the item is vanilla, then the worth is just the value of the vanilla material.
        if (firstMaterial.isVanilla())
            worth = VanillaResource.getMaterialValue(firstMaterial.material());
        // If the item is custom check if the blueprint is sellable and use that.
        else if (firstMaterial.isCustom() && itemService.getBlueprint(firstMaterial.getCustom()) instanceof ISellable sellable)
            worth = sellable.getWorth(firstMaterial.get(itemService));

        return amount * worth * itemStack.getAmount();
    }
}

package xyz.devvydont.smprpg.items.base;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Compressable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CustomCompressableBlueprint extends CustomItemBlueprint implements Compressable {

    public CustomCompressableBlueprint(ItemService itemService) {
        super(itemService);
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

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        Component material = getCompressionFlow().get(0).getMaterial().component().decoration(TextDecoration.BOLD, true);
        return List.of(
                Component.text("An ultra compressed").color(NamedTextColor.GRAY),
                Component.text("collection of ").color(NamedTextColor.GRAY).append(material),
                Component.empty(),
                Component.text("(1x)  Uncompressed amount: ").color(NamedTextColor.DARK_GRAY).append(Component.text(MinecraftStringUtils.formatNumber(getCompressedAmount())).color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true)),
                Component.text("(64x) Uncompressed amount: ").color(NamedTextColor.DARK_GRAY).append(Component.text(MinecraftStringUtils.formatNumber(getCompressedAmount() * 64L)).color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true))
        );
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
}

package xyz.devvydont.smprpg.items.blueprints.sets.inferno;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomShortbow;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemAxe;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class InfernoShortbow extends CustomShortbow implements ICraftable, ISellable {

    public InfernoShortbow(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemAxe.getAxeDamage(Material.NETHERITE_AXE)-10),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.4)
        );
    }

    @Override
    public int getPowerRating() {
        return InfernoArmorSet.POWER;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(
                " rs",
                "r s",
                " rs");
        recipe.setIngredient('r', ItemService.generate(InfernoArmorSet.CRAFTING_COMPONENT));
        recipe.setIngredient('s', ItemService.generate(CustomItemType.SCORCHING_STRING));
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                ItemService.generate(InfernoArmorSet.CRAFTING_COMPONENT)
        );
    }
}

package xyz.devvydont.smprpg.items.blueprints.bow;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolsUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class IronBow extends CustomAttributeItem implements Craftable, ToolBreakable {

    public IronBow(ItemService itemService) {
        super(itemService);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ATTACK_DAMAGE, 25.0)
        );
    }

    @Override
    public int getPowerRating() {
        return 10;
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.IRON_BOW;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOW;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(
                " IS",
                "I S",
                " IS"
        );
        recipe.setIngredient('I', Material.DIAMOND);
        recipe.setIngredient('S', Material.STRING);
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getMaxDurability() {
        return ToolsUtil.IRON_TOOL_DURABILITY;
    }
}

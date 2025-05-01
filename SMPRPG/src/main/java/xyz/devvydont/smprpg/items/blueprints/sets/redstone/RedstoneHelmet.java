package xyz.devvydont.smprpg.items.blueprints.sets.redstone;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeHelmetBlueprint;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

import java.util.Collection;
import java.util.List;

import static xyz.devvydont.smprpg.items.blueprints.sets.redstone.RedstoneArmorSet.*;

public class RedstoneHelmet extends CustomFakeHelmetBlueprint implements ToolBreakable, Craftable {

    public RedstoneHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, ItemArmor.getDefenseFromMaterial(Material.IRON_HELMET)),
                new ScalarAttributeEntry(Attribute.MOVEMENT_SPEED, MOVEMENT_BUFF),
                new MultiplicativeAttributeEntry(Attribute.ATTACK_SPEED, ATTACK_BUFF),
                new ScalarAttributeEntry(Attribute.MINING_EFFICIENCY, MINING_BUFF)
        );
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HelmetRecipe(this, itemService.getCustomItem(RedstoneArmorSet.INGREDIENT), generate()).build();
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(Material.REDSTONE));
    }

    @Override
    public int getPowerRating() {
        return RedstoneArmorSet.POWER;
    }

    @Override
    public int getMaxDurability() {
        return RedstoneArmorSet.DURABILITY;
    }
}

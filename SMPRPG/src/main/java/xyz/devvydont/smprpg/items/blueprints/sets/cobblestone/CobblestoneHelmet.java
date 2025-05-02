package xyz.devvydont.smprpg.items.blueprints.sets.cobblestone;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeHelmetBlueprint;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

import java.util.Collection;
import java.util.List;

public class CobblestoneHelmet extends CustomFakeHelmetBlueprint implements ICraftable, IBreakableEquipment {

    public CobblestoneHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, ItemArmor.getDefenseFromMaterial(Material.DIAMOND_HELMET)),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, -0.1)
        );
    }

    @Override
    public int getPowerRating() {
        return 5;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HelmetRecipe(this, itemService.getCustomItem(CustomItemType.COMPRESSED_COBBLESTONE), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(Material.COBBLESTONE));
    }

    @Override
    public int getMaxDurability() {
        return 600;
    }
}

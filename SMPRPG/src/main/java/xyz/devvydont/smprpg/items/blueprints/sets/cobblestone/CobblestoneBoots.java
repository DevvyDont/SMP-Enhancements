package xyz.devvydont.smprpg.items.blueprints.sets.cobblestone;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;

import java.util.Collection;
import java.util.List;

public class CobblestoneBoots extends CustomArmorBlueprint implements ToolBreakable, Dyeable, Craftable, Trimmable {


    public CobblestoneBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 85),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, -0.15)
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
        return new BootsRecipe(this, itemService.getCustomItem(CustomItemType.COMPRESSED_COBBLESTONE), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(Material.COBBLESTONE));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x474f52);
    }

    @Override
    public int getMaxDurability() {
        return 600;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.NETHERITE;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.RIB;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }
}

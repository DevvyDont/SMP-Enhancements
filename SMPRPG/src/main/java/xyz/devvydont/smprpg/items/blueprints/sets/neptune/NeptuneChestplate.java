package xyz.devvydont.smprpg.items.blueprints.sets.neptune;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

import java.util.Collection;
import java.util.List;

public class NeptuneChestplate extends NeptuneArmorSet {

    public NeptuneChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 125;
    }

    @Override
    public int getHealth() {
        return 80;
    }

    @Override
    public int getStrength() {
        return 25;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x99d9ec);
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.EMERALD;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.JUPITERS_ARTIFACT), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.JUPITER_CRYSTAL)
        );
    }
}

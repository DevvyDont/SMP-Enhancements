package xyz.devvydont.smprpg.items.blueprints.sets.sakura;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

import java.util.Collection;
import java.util.List;

public class SakuraChestplate extends SakuraArmorSet implements Dyeable, Trimmable {

    public SakuraChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ChestplateRecipe(this, itemService.getCustomItem(Material.CHERRY_LOG), generate()).build();
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 30));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0xf38baa);
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.IRON;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }

}

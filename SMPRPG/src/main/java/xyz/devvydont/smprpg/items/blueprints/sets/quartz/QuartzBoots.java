package xyz.devvydont.smprpg.items.blueprints.sets.quartz;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.items.interfaces.IDyeable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;

public class QuartzBoots extends QuartzArmorSet implements IDyeable {

    public QuartzBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return ItemArmor.getDefenseFromMaterial(Material.DIAMOND_BOOTS) / 2;
    }

    @Override
    public double getStrength() {
        return ItemArmor.getDamageFromMaterial(Material.NETHERITE_BOOTS) * 2;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.IRON;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.EYE;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0xf9fff3);
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new BootsRecipe(this, itemService.getCustomItem(CustomItemType.ENCHANTED_QUARTZ), generate()).build();
    }
}

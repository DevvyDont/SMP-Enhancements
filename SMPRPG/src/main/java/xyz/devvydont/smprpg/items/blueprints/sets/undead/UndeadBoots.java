package xyz.devvydont.smprpg.items.blueprints.sets.undead;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

public class UndeadBoots extends UndeadArmorSet {

    public UndeadBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 20;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x835432);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new BootsRecipe(this, itemService.getCustomItem(Material.ROTTEN_FLESH), generate()).build();
    }
}

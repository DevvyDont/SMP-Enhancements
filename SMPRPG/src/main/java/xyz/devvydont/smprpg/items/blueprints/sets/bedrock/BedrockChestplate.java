package xyz.devvydont.smprpg.items.blueprints.sets.bedrock;

import org.bukkit.Color;
import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

public class BedrockChestplate extends BedrockArmorSet implements Dyeable {

    public BedrockChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 300;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x1d1d21);
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.DEEPSLATE_SINGULARITY), generate()).build();
    }
}

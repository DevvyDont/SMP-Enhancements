package xyz.devvydont.smprpg.items.blueprints.sets.bedrock;

import org.bukkit.Color;
import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

public class BedrockBoots extends BedrockArmorSet implements Dyeable {

    public BedrockBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 225;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x1d1d21);
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new BootsRecipe(this, itemService.getCustomItem(CustomItemType.DEEPSLATE_SINGULARITY), generate()).build();
    }
}

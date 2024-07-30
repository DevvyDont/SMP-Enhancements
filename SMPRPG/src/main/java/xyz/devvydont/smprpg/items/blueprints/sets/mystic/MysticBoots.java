package xyz.devvydont.smprpg.items.blueprints.sets.mystic;

import org.bukkit.Color;
import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

public class MysticBoots extends MysticArmorSet implements Dyeable {

    public MysticBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 65;
    }

    @Override
    public int getHealth() {
        return 5;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.ENCHANTED_LAPIS), generate()).build();
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x3c44aa);
    }

}

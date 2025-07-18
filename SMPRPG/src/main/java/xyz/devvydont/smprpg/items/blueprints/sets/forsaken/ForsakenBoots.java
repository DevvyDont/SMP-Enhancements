package xyz.devvydont.smprpg.items.blueprints.sets.forsaken;

import org.bukkit.Color;
import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.IDyeable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;

public class ForsakenBoots extends ForsakenArmorSet implements IDyeable {

    public static final int DEFENSE = 85;
    public static final int HEALTH = 10;
    public static final double STRENGTH = .3;

    public ForsakenBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return DEFENSE;
    }

    @Override
    public int getHealth() {
        return HEALTH;
    }

    @Override
    public double getStrength() {
        return STRENGTH;
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
        return new BootsRecipe(this, ItemService.generate(ForsakenHelmet.CRAFTING_COMPONENT), generate()).build();
    }
}

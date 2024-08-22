package xyz.devvydont.smprpg.items.blueprints.sets.forsaken;

import org.bukkit.Color;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.services.ItemService;

public class ForsakenChestplate extends ForsakenArmorSet implements Dyeable {

    public ForsakenChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 175;
    }

    @Override
    public int getHealth() {
        return 155;
    }

    @Override
    public double getStrength() {
        return .5;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x1d1d21);
    }
}

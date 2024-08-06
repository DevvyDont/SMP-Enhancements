package xyz.devvydont.smprpg.items.blueprints.sets.bone;

import org.bukkit.Color;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.services.ItemService;

public class BoneBoots extends BoneArmorSet implements Dyeable {

    public BoneBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return 20;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x9d9d97);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }
}

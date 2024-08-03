package xyz.devvydont.smprpg.items.blueprints.sets.elderflame;

import org.bukkit.Color;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.services.ItemService;

public class ElderflameBoots extends ElderflameArmorSet implements Dyeable {

    public ElderflameBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x1d1d21);
    }
}

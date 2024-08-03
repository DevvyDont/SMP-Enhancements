package xyz.devvydont.smprpg.items.blueprints.sets.unstable;

import org.bukkit.Color;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.services.ItemService;

public class UnstableBoots extends UnstableArmorSet implements Dyeable {

    public UnstableBoots(ItemService itemService, CustomItemType type) {
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

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.RIB;
    }
}

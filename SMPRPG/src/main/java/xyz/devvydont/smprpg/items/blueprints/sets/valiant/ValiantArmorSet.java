package xyz.devvydont.smprpg.items.blueprints.sets.valiant;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class ValiantArmorSet extends CustomArmorBlueprint implements ToolBreakable {

    public ValiantArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getPowerRating() {
        return 85;
    }

    @Override
    public int getMaxDurability() {
        return 80_000;
    }
}

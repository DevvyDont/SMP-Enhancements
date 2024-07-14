package xyz.devvydont.smprpg.items.blueprints.resources;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

public class EmptyBlueprint extends CustomItemBlueprint {

    private final CustomItemType type;
    private final ItemClassification classification;

    public EmptyBlueprint(ItemService itemService, CustomItemType type, ItemClassification classification) {
        super(itemService);
        this.type = type;
        this.classification = classification;
    }

    @Override
    public CustomItemType getCustomItemType() {
        return type;
    }

    @Override
    public ItemClassification getItemClassification() {
        return classification;
    }
}

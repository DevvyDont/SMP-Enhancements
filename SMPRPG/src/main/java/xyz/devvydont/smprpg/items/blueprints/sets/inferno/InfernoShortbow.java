package xyz.devvydont.smprpg.items.blueprints.sets.inferno;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomShortbow;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class InfernoShortbow extends CustomShortbow {

    public InfernoShortbow(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 270),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.25)
        );
    }

    @Override
    public int getPowerRating() {
        return InfernoArmorSet.POWER;
    }
}

package xyz.devvydont.smprpg.items.blueprints.sets.bedrock;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeHelmetBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class BedrockHelmet extends CustomFakeHelmetBlueprint implements ToolBreakable {

    public BedrockHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 250),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, -.2),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.25),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .15)
        );
    }

    @Override
    public int getPowerRating() {
        return BedrockArmorSet.POWER;
    }

    @Override
    public int getMaxDurability() {
        return BedrockArmorSet.DURABILITY;
    }
}

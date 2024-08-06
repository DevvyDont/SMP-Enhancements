package xyz.devvydont.smprpg.items.blueprints.sets.redstone;

import org.bukkit.attribute.Attribute;
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

public class RedstoneHelmet extends CustomFakeHelmetBlueprint implements ToolBreakable {

    public RedstoneHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 40),
                new ScalarAttributeEntry(Attribute.GENERIC_MOVEMENT_SPEED, .05),
                new MultiplicativeAttributeEntry(Attribute.GENERIC_ATTACK_SPEED, .05)
        );
    }

    @Override
    public int getPowerRating() {
        return RedstoneArmorSet.POWER;
    }

    @Override
    public int getMaxDurability() {
        return RedstoneArmorSet.DURABILITY;
    }
}

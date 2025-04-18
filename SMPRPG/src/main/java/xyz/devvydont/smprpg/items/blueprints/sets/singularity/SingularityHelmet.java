package xyz.devvydont.smprpg.items.blueprints.sets.singularity;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeHelmetBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class SingularityHelmet extends CustomFakeHelmetBlueprint implements ToolBreakable {

    public SingularityHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getPowerRating() {
        return 100;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 6),
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 600),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 300),
                new ScalarAttributeEntry(Attribute.KNOCKBACK_RESISTANCE, .2),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .2),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .2)
        );
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getMaxDurability() {
        return 100_000;
    }
}

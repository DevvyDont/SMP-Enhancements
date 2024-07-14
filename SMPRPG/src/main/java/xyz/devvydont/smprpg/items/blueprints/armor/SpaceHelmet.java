package xyz.devvydont.smprpg.items.blueprints.armor;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeHelmetBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Wearable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class SpaceHelmet extends CustomFakeHelmetBlueprint implements Wearable {


    public SpaceHelmet(ItemService itemService) {
        super(itemService);
    }

    @Override
    public int getPowerRating() {
        return 100;
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.SPACE_HELMET;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ARMOR;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE.getAttribute(), 200),
                new AdditiveAttributeEntry(Attribute.GENERIC_SAFE_FALL_DISTANCE, 50),
                new MultiplicativeAttributeEntry(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER, -.50),
                new MultiplicativeAttributeEntry(Attribute.GENERIC_GRAVITY, -.9),
                new MultiplicativeAttributeEntry(Attribute.GENERIC_JUMP_STRENGTH, 2)
        );
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }
}

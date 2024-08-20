package xyz.devvydont.smprpg.items.blueprints.charms;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeTotem;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class SpeedCharm extends CustomFakeTotem {

    public SpeedCharm(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new ScalarAttributeEntry(Attribute.GENERIC_MOVEMENT_SPEED, .5),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, .5),
                new ScalarAttributeEntry(AttributeWrapper.MINING_SPEED, .5)
        );
    }

    @Override
    public int getPowerRating() {
        return 30;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHARM;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }
}

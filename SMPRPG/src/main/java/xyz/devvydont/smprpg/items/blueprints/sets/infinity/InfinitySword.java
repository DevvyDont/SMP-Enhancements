package xyz.devvydont.smprpg.items.blueprints.sets.infinity;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class InfinitySword extends CustomAttributeItem {


    public InfinitySword(ItemService itemService) {
        super(itemService);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ATTACK_DAMAGE, 25000),
                new AdditiveAttributeEntry(Attribute.PLAYER_ENTITY_INTERACTION_RANGE, 20),
                new ScalarAttributeEntry(Attribute.GENERIC_ATTACK_SPEED, 10)
        );
    }

    @Override
    public int getPowerRating() {
        return 1000;
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.INFINITY_SWORD;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.SWORD;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }
}

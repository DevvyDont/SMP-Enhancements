package xyz.devvydont.smprpg.items.blueprints.sets.neptune;

import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class NeptuneTrident extends CustomAttributeItem implements ToolBreakable {


    public NeptuneTrident(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 550),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemSword.SWORD_ATTACK_SPEED_DEBUFF)
        );
    }

    @Override
    public int getPowerRating() {
        return NeptuneArmorSet.POWER_LEVEL;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TRIDENT;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getMaxDurability() {
        return NeptuneArmorSet.DURABILITY;
    }
}

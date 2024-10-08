package xyz.devvydont.smprpg.items.blueprints.sets.exiled;

import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemAxe;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class ExiledAxe extends CustomAttributeItem implements ToolBreakable {

    public ExiledAxe(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 160),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemAxe.AXE_ATTACK_SPEED_DEBUFF),
                new ScalarAttributeEntry(AttributeWrapper.MINING_EFFICIENCY, .25)
        );
    }

    @Override
    public int getPowerRating() {
        return 30;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.AXE;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return 40_000;
    }
}

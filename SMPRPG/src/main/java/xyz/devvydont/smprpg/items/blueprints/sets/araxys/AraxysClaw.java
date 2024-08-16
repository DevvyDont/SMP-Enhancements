package xyz.devvydont.smprpg.items.blueprints.sets.araxys;

import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class AraxysClaw extends CustomAttributeItem implements ToolBreakable {


    public AraxysClaw(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 400),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.25)
        );
    }

    @Override
    public int getMaxDurability() {
        return 35_000;
    }

    @Override
    public int getPowerRating() {
        return AraxysArmorPiece.POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.WEAPON;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }
}

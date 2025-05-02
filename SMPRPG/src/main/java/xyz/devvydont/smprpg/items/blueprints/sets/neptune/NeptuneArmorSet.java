package xyz.devvydont.smprpg.items.blueprints.sets.neptune;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public abstract class NeptuneArmorSet extends CustomAttributeItem implements IBreakableEquipment, ICraftable {

    public static final int POWER_LEVEL = 20;
    public static final int OXYGEN_BONUS = 20;
    public static final int DURABILITY = 25_000;

    public NeptuneArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public boolean wantNerfedSellPrice() {
        return false;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, getStrength()/100.0),
                new AdditiveAttributeEntry(AttributeWrapper.OXYGEN_BONUS, OXYGEN_BONUS),
                new ScalarAttributeEntry(AttributeWrapper.BURNING_TIME, -.1)
        );
    }

    public abstract int getDefense();

    public abstract int getHealth();

    public abstract int getStrength();

    @Override
    public int getMaxDurability() {
        return DURABILITY;
    }

    @Override
    public int getPowerRating() {
        return POWER_LEVEL;
    }
}

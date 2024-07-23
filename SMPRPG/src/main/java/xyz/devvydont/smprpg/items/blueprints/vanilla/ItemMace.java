package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class ItemMace extends VanillaAttributeItem implements ToolBreakable {

    public static final int MACE_ATTACK_DAMAGE = 750;
    public static final double MACE_ATTACK_SPEED_DEBUFF = -0.85;

    public ItemMace(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public ItemRarity getDefaultRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MACE;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH.getAttribute(), MACE_ATTACK_DAMAGE),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED.getAttribute(), MACE_ATTACK_SPEED_DEBUFF)
        );
    }

    @Override
    public int getPowerRating() {
        return 75;
    }

    @Override
    public int getMaxDurability() {
        return 500_000;
    }
}

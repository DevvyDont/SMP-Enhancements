package xyz.devvydont.smprpg.items.blueprints.sets.singularity;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ITrimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapperLegacy;

import java.util.Collection;
import java.util.List;

public class SingularityChestplate extends CustomAttributeItem implements IBreakableEquipment, ITrimmable {


    public SingularityChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public int getPowerRating() {
        return 100;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.CHEST;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapperLegacy.ARMOR, 6),
                new AdditiveAttributeEntry(AttributeWrapperLegacy.DEFENSE, 1100),
                new AdditiveAttributeEntry(AttributeWrapperLegacy.HEALTH, 540),
                new ScalarAttributeEntry(AttributeWrapperLegacy.KNOCKBACK_RESISTANCE, .2),
                new ScalarAttributeEntry(AttributeWrapperLegacy.MOVEMENT_SPEED, .2),
                new ScalarAttributeEntry(AttributeWrapperLegacy.STRENGTH, .2)
        );
    }

    @Override
    public int getMaxDurability() {
        return 100_000;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.AMETHYST;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }
}

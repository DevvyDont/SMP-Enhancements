package xyz.devvydont.smprpg.items.blueprints.sets.fishing;

import org.bukkit.Color;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IDyeable;
import xyz.devvydont.smprpg.items.interfaces.ITrimmable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class MinnowSet extends CustomAttributeItem implements ITrimmable, IDyeable {

    public static final int POWER = 15;
    public static final int CATCH_QUALITY = 15;
    public static final double CHANCE = 1;
    public static final int COLOR = 0x9B9B9B;
    public static final TrimPattern TRIM = TrimPattern.RIB;
    public static final TrimMaterial TRIM_MATERIAL = TrimMaterial.DIAMOND;

    public MinnowSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return switch (this.getCustomItemType()) {
            case MINNOW_HELMET -> ItemClassification.HELMET;
            case MINNOW_CHESTPLATE -> ItemClassification.CHESTPLATE;
            case MINNOW_LEGGINGS -> ItemClassification.LEGGINGS;
            case MINNOW_BOOTS -> ItemClassification.BOOTS;
            default -> ItemClassification.ITEM;
        };
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                AttributeEntry.additive(AttributeWrapper.DEFENSE, getDefense()),
                AttributeEntry.additive(AttributeWrapper.HEALTH, getHealth()),
                AttributeEntry.additive(AttributeWrapper.FISHING_RATING, CATCH_QUALITY),
                AttributeEntry.additive(AttributeWrapper.FISHING_CREATURE_CHANCE, CHANCE)
        );
    }

    @Override
    public int getPowerRating() {
        return POWER;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(COLOR);
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TRIM_MATERIAL;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TRIM;
    }

    private int getDefense() {
        return switch (this.getCustomItemType()) {
            case MINNOW_HELMET -> 20;
            case MINNOW_CHESTPLATE -> 40;
            case MINNOW_LEGGINGS -> 25;
            case MINNOW_BOOTS -> 15;
            default -> 0;
        };
    }

    private int getHealth() {
        return switch (this.getCustomItemType()) {
            case MINNOW_HELMET -> 5;
            case MINNOW_CHESTPLATE -> 5;
            case MINNOW_LEGGINGS -> 5;
            case MINNOW_BOOTS -> 5;
            default -> 0;
        };
    }
}

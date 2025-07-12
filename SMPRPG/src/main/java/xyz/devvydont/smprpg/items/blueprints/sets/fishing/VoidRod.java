package xyz.devvydont.smprpg.items.blueprints.sets.fishing;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.IFishingRod;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class VoidRod extends CustomAttributeItem implements IBreakableEquipment, IFishingRod {

    public VoidRod(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ROD;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                AttributeEntry.additive(AttributeWrapper.STRENGTH, getStrength()),
                AttributeEntry.multiplicative(AttributeWrapper.ATTACK_SPEED, -.5),
                AttributeEntry.additive(AttributeWrapper.FISHING_RATING, getFishingRating()),
                AttributeEntry.additive(AttributeWrapper.FISHING_CREATURE_CHANCE, getChance()),
                AttributeEntry.additive(AttributeWrapper.FISHING_TREASURE_CHANCE, getChance())
        );
    }

    @Override
    public int getPowerRating() {
        return switch (getCustomItemType()) {
            case COMET_ROD -> 40;
            case NEBULA_ROD -> 50;
            default -> 0;
        };
    };

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getMaxDurability() {
        return getPowerRating() * 1_000;
    }

    @Override
    public Set<FishingFlag> getFishingFlags() {
        return Set.of(FishingFlag.VOID);
    }

    private int getFishingRating() {
        return switch (getCustomItemType()) {
            case COMET_ROD -> 200;
            case NEBULA_ROD -> 300;
            default -> 0;
        };
    };

    private int getStrength() {
        return switch (getCustomItemType()) {
            case COMET_ROD -> 100;
            case NEBULA_ROD -> 150;
            default -> 0;
        };
    };

    private int getChance() {
        return switch (getCustomItemType()) {
            case COMET_ROD -> 6;
            case NEBULA_ROD -> 8;
            default -> 0;
        };
    };
}

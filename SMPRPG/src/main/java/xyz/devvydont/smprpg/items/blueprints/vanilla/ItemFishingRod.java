package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.interfaces.IFishingRod;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ItemFishingRod extends VanillaAttributeItem implements IBreakableEquipment, IFishingRod {

    public ItemFishingRod(ItemService itemService, Material material) {
        super(itemService, material);
    }

    @Override
    public int getMaxDurability() {
        return 250;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                AttributeEntry.additive(AttributeWrapper.STRENGTH, 5),
                AttributeEntry.multiplicative(AttributeWrapper.ATTACK_SPEED, -.5),
                AttributeEntry.additive(AttributeWrapper.FISHING_RATING, 5),
                AttributeEntry.additive(AttributeWrapper.FISHING_TREASURE_CHANCE, .2),
                AttributeEntry.additive(AttributeWrapper.FISHING_TREASURE_CHANCE, .2)
        );
    }

    @Override
    public int getPowerRating() {
        return 2;
    }

    @Override
    public Set<FishingFlag> getFishingFlags() {
        return Set.of(
                FishingFlag.NORMAL
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ROD;
    }
}

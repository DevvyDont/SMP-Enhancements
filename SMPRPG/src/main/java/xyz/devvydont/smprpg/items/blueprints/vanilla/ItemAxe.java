package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ItemAxe extends VanillaAttributeItem {


    public static double getAxeDamage(Material material) {
        return switch (material) {
            case NETHERITE_AXE -> 450.0;
            case DIAMOND_AXE -> 135.0;
            case GOLDEN_AXE -> 40.0;
            case IRON_AXE -> 15.0;
            case STONE_AXE -> 10.0;
            case WOODEN_AXE -> 6.0;

            default -> 0;
        };
    }

    public static int getAxeRating(Material material) {
        return switch (material) {
            case NETHERITE_AXE -> 45;
            case DIAMOND_AXE -> 25;
            case GOLDEN_AXE -> 12;
            case IRON_AXE -> 7;
            case STONE_AXE -> 3;
            case WOODEN_AXE -> 2;
            default -> 1;
        };
    }

    public static double AXE_ATTACK_SPEED_DEBUFF = -0.8;

    public ItemAxe(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.AXE;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ATTACK_DAMAGE, getAxeDamage(getItem().getType())),
                new MultiplicativeAttributeEntry(Attribute.GENERIC_ATTACK_SPEED, AXE_ATTACK_SPEED_DEBUFF)
        );
    }

    @Override
    public int getPowerRating() {
        return getAxeRating(getItem().getType());
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }
}

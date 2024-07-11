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

public class ItemHoe extends VanillaAttributeItem {

    public static double getHoeDamage(Material material) {
        return switch (material) {
            case NETHERITE_HOE -> 45.0;
            case DIAMOND_HOE -> 18.0;
            case GOLDEN_HOE -> 9.0;
            case IRON_HOE -> 4.0;
            case STONE_HOE -> 2.0;
            case WOODEN_HOE -> 1.0;
            default -> 0;
        };
    }

    public static double getHoeAttackSpeedDebuff(Material material) {
        return switch (material) {
            case NETHERITE_HOE -> -0.05;
            case DIAMOND_HOE -> -0.15;
            case IRON_HOE -> -0.20;
            case STONE_HOE -> -0.25;
            case WOODEN_HOE, GOLDEN_HOE -> -0.35;
            default -> 0;
        };
    }

    public static int getHoeRating(Material material) {
        return switch (material) {
            case NETHERITE_HOE -> 45;
            case DIAMOND_HOE -> 25;
            case GOLDEN_HOE -> 12;
            case IRON_HOE -> 7;
            case STONE_HOE -> 3;
            case WOODEN_HOE -> 2;
            default -> 1;
        };
    }

    public ItemHoe(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOOL;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ATTACK_DAMAGE, getHoeDamage(getItem().getType())),
                new MultiplicativeAttributeEntry(Attribute.GENERIC_ATTACK_SPEED, getHoeAttackSpeedDebuff(getItem().getType()))
        );
    }

    @Override
    public int getPowerRating() {
        return getHoeRating(getItem().getType());
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }
}

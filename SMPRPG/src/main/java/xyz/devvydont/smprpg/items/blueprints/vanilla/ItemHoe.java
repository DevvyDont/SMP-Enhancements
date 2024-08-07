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
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolsUtil;

import javax.tools.Tool;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ItemHoe extends VanillaAttributeItem implements ToolBreakable {

    public static double getHoeDamage(Material material) {
        return switch (material) {
            case NETHERITE_HOE -> 190.0;
            case DIAMOND_HOE -> 90.0;
            case GOLDEN_HOE -> 18.0;
            case IRON_HOE -> 12.0;
            case STONE_HOE -> 5.0;
            case WOODEN_HOE -> 4.0;
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


    @Override
    public int getMaxDurability() {
        return switch (getItem().getType()) {
            case NETHERITE_HOE -> ToolsUtil.NETHERITE_TOOL_DURABILITY;
            case DIAMOND_HOE -> ToolsUtil.DIAMOND_TOOL_DURABILITY;
            case GOLDEN_HOE -> ToolsUtil.GOLD_TOOL_DURABILITY;
            case IRON_HOE -> ToolsUtil.IRON_TOOL_DURABILITY;
            case STONE_HOE -> ToolsUtil.STONE_TOOL_DURABILITY;
            case WOODEN_HOE -> ToolsUtil.WOOD_TOOL_DURABILITY;
            default -> 50_000;
        };
    }
    
}

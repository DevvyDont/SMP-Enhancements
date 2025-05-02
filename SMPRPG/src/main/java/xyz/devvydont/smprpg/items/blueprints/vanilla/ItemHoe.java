package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolsUtil;

import java.util.Collection;
import java.util.List;

public class ItemHoe extends VanillaAttributeItem implements IBreakableEquipment {

    public static double getHoeDamage(Material material) {
        return switch (material) {
            case NETHERITE_HOE -> 30;
            case DIAMOND_HOE -> 15;
            case GOLDEN_HOE -> 8;
            case IRON_HOE -> 5;
            case STONE_HOE -> 3;
            case WOODEN_HOE -> 2;
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
            case NETHERITE_HOE -> ToolsUtil.NETHERITE_TOOL_POWER;
            case DIAMOND_HOE -> ToolsUtil.DIAMOND_TOOL_POWER;
            case GOLDEN_HOE -> ToolsUtil.GOLD_TOOL_POWER;
            case IRON_HOE -> ToolsUtil.IRON_TOOL_POWER;
            case STONE_HOE -> ToolsUtil.STONE_TOOL_POWER;
            case WOODEN_HOE -> ToolsUtil.WOOD_TOOL_POWER;
            default -> 1;
        };
    }

    public ItemHoe(ItemService itemService, Material material) {
        super(itemService, material);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOOL;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(Attribute.ATTACK_DAMAGE, getHoeDamage(material)),
                new MultiplicativeAttributeEntry(Attribute.ATTACK_SPEED, getHoeAttackSpeedDebuff(material))
        );
    }

    @Override
    public int getPowerRating() {
        return getHoeRating(material);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }


    @Override
    public int getMaxDurability() {
        return switch (material) {
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

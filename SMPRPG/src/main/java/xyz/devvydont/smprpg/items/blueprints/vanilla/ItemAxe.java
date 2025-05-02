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

public class ItemAxe extends VanillaAttributeItem implements IBreakableEquipment {

    public static double getAxeDamage(Material material) {
        return switch (material) {
            case NETHERITE_AXE -> 150;
            case DIAMOND_AXE -> 100;
            case GOLDEN_AXE -> 65;
            case IRON_AXE -> 50;
            case STONE_AXE -> 35;
            case WOODEN_AXE -> 30;

            default -> 0;
        };
    }

    public static int getAxeRating(Material material) {
        return switch (material) {
            case NETHERITE_AXE -> ToolsUtil.NETHERITE_TOOL_POWER;
            case DIAMOND_AXE -> ToolsUtil.DIAMOND_TOOL_POWER;
            case GOLDEN_AXE -> ToolsUtil.GOLD_TOOL_POWER;
            case IRON_AXE -> ToolsUtil.IRON_TOOL_POWER;
            case STONE_AXE -> ToolsUtil.STONE_TOOL_POWER;
            case WOODEN_AXE -> ToolsUtil.WOOD_TOOL_POWER;
            default -> 1;
        };
    }

    public static double AXE_ATTACK_SPEED_DEBUFF = -0.8;

    public ItemAxe(ItemService itemService, Material material) {
        super(itemService, material);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.AXE;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(Attribute.ATTACK_DAMAGE, getAxeDamage(material)),
                new MultiplicativeAttributeEntry(Attribute.ATTACK_SPEED, AXE_ATTACK_SPEED_DEBUFF)
        );
    }

    @Override
    public int getPowerRating() {
        return getAxeRating(material);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return switch (material) {
            case NETHERITE_AXE -> ToolsUtil.NETHERITE_TOOL_DURABILITY;
            case DIAMOND_AXE -> ToolsUtil.DIAMOND_TOOL_DURABILITY;
            case GOLDEN_AXE -> ToolsUtil.GOLD_TOOL_DURABILITY;
            case IRON_AXE -> ToolsUtil.IRON_TOOL_DURABILITY;
            case STONE_AXE -> ToolsUtil.STONE_TOOL_DURABILITY;
            case WOODEN_AXE -> ToolsUtil.WOOD_TOOL_DURABILITY;
            default -> 50_000;
        };
    }
}

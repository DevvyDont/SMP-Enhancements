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

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ItemAxe extends VanillaAttributeItem implements ToolBreakable {


    public static double getAxeDamage(Material material) {
        return switch (material) {
            case NETHERITE_AXE -> 900.0;
            case DIAMOND_AXE -> 250.0;
            case GOLDEN_AXE -> 120.0;
            case IRON_AXE -> 75.0;
            case STONE_AXE -> 48.0;
            case WOODEN_AXE -> 32.0;

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

    @Override
    public int getMaxDurability() {
        return switch (getItem().getType()) {
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

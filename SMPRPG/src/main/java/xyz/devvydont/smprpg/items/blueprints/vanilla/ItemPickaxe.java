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

public class ItemPickaxe extends VanillaAttributeItem implements IBreakableEquipment {

    public static double getPickaxeDamage(Material material) {
        return switch (material) {
            case NETHERITE_PICKAXE -> 40;
            case DIAMOND_PICKAXE -> 20;
            case GOLDEN_PICKAXE -> 10;
            case IRON_PICKAXE -> 7;
            case STONE_PICKAXE -> 5;
            case WOODEN_PICKAXE -> 4;
            default -> 0;
        };
    }

    public static int getPickaxeRating(Material material) {
        return switch (material) {
            case NETHERITE_PICKAXE -> ToolsUtil.NETHERITE_TOOL_POWER;
            case DIAMOND_PICKAXE -> ToolsUtil.DIAMOND_TOOL_POWER;
            case GOLDEN_PICKAXE -> ToolsUtil.GOLD_TOOL_POWER;
            case IRON_PICKAXE -> ToolsUtil.IRON_TOOL_POWER;
            case STONE_PICKAXE -> ToolsUtil.STONE_TOOL_POWER;
            case WOODEN_PICKAXE -> ToolsUtil.WOOD_TOOL_POWER;
            default -> 1;
        };
    }

    public static double PICKAXE_ATTACK_SPEED_DEBUFF = -0.7;

    public ItemPickaxe(ItemService itemService, Material material) {
        super(itemService, material);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOOL;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(Attribute.ATTACK_DAMAGE, getPickaxeDamage(material)),
                new MultiplicativeAttributeEntry(Attribute.ATTACK_SPEED, PICKAXE_ATTACK_SPEED_DEBUFF)
        );
    }

    @Override
    public int getPowerRating() {
        return getPickaxeRating(material);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return switch (material) {
            case NETHERITE_PICKAXE -> ToolsUtil.NETHERITE_TOOL_DURABILITY;
            case DIAMOND_PICKAXE -> ToolsUtil.DIAMOND_TOOL_DURABILITY;
            case GOLDEN_PICKAXE -> ToolsUtil.GOLD_TOOL_DURABILITY;
            case IRON_PICKAXE -> ToolsUtil.IRON_TOOL_DURABILITY;
            case STONE_PICKAXE -> ToolsUtil.STONE_TOOL_DURABILITY;
            case WOODEN_PICKAXE -> ToolsUtil.WOOD_TOOL_DURABILITY;
            default -> 1_000;
        };
    }
}

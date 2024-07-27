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

public class ItemShovel extends VanillaAttributeItem implements ToolBreakable {

    public static double getShovelDamage(Material material) {
        return switch (material) {
            case NETHERITE_SHOVEL -> 200.0;
            case DIAMOND_SHOVEL -> 90.0;
            case GOLDEN_SHOVEL -> 25.0;
            case IRON_SHOVEL -> 12.0;
            case STONE_SHOVEL -> 6.0;
            case WOODEN_SHOVEL -> 4.0;
            default -> 0;
        };
    }

    public static int getShovelRating(Material material) {
        return switch (material) {
            case NETHERITE_SHOVEL -> 45;
            case DIAMOND_SHOVEL -> 25;
            case GOLDEN_SHOVEL -> 12;
            case IRON_SHOVEL -> 7;
            case STONE_SHOVEL -> 3;
            case WOODEN_SHOVEL -> 2;
            default -> 1;
        };
    }

    public static double SHOVEL_ATTACK_SPEED_DEBUFF = -0.75;

    public ItemShovel(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOOL;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ATTACK_DAMAGE, getShovelDamage(getItem().getType())),
                new MultiplicativeAttributeEntry(Attribute.GENERIC_ATTACK_SPEED, SHOVEL_ATTACK_SPEED_DEBUFF)
        );
    }

    @Override
    public int getPowerRating() {
        return getShovelRating(getItem().getType());
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return switch (getItem().getType()) {
            case NETHERITE_SHOVEL -> ToolsUtil.NETHERITE_TOOL_DURABILITY;
            case DIAMOND_SHOVEL -> ToolsUtil.DIAMOND_TOOL_DURABILITY;
            case GOLDEN_SHOVEL -> ToolsUtil.GOLD_TOOL_DURABILITY;
            case IRON_SHOVEL -> ToolsUtil.IRON_TOOL_DURABILITY;
            case STONE_SHOVEL -> ToolsUtil.STONE_TOOL_DURABILITY;
            case WOODEN_SHOVEL -> ToolsUtil.WOOD_TOOL_DURABILITY;
            default -> 50_000;
        };
    }
}

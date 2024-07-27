package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolsUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class ItemSword extends VanillaAttributeItem implements ToolBreakable {

    public static double getSwordDamage(Material material) {
        return switch (material) {
            case NETHERITE_SWORD -> 500.0;
            case TRIDENT -> 250.0;
            case DIAMOND_SWORD -> 200.0;
            case GOLDEN_SWORD -> 60.0;
            case IRON_SWORD -> 30.0;
            case STONE_SWORD -> 15.0;
            case WOODEN_SWORD -> 9.0;
            default -> 0;
        };
    }

    public static int getSwordRating(Material material) {
        return switch (material) {
            case NETHERITE_SWORD -> 45;
            case TRIDENT -> 30;
            case DIAMOND_SWORD -> 25;
            case GOLDEN_SWORD -> 12;
            case IRON_SWORD -> 7;
            case STONE_SWORD -> 3;
            case WOODEN_SWORD -> 2;
            default -> 1;
        };
    }

    public static double SWORD_ATTACK_SPEED_DEBUFF = -0.6;

    public ItemSword(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.SWORD;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ATTACK_DAMAGE, getSwordDamage(getItem().getType())),
                new MultiplicativeAttributeEntry(Attribute.GENERIC_ATTACK_SPEED, SWORD_ATTACK_SPEED_DEBUFF)
        );
    }

    @Override
    public int getPowerRating() {
        return getSwordRating(getItem().getType());
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return switch (getItem().getType()) {
            case NETHERITE_SWORD -> ToolsUtil.NETHERITE_TOOL_DURABILITY;
            case DIAMOND_SWORD -> ToolsUtil.DIAMOND_TOOL_DURABILITY;
            case GOLDEN_SWORD -> ToolsUtil.GOLD_TOOL_DURABILITY;
            case IRON_SWORD -> ToolsUtil.IRON_TOOL_DURABILITY;
            case STONE_SWORD -> ToolsUtil.STONE_TOOL_DURABILITY;
            case WOODEN_SWORD -> ToolsUtil.WOOD_TOOL_DURABILITY;
            default -> 50_000;
        };
    }
}

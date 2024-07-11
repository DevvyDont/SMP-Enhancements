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

public class ItemShovel extends VanillaAttributeItem {

    public static double getShovelDamage(Material material) {
        return switch (material) {
            case NETHERITE_SHOVEL -> 160.0;
            case DIAMOND_SHOVEL -> 35.0;
            case GOLDEN_SHOVEL -> 15.0;
            case IRON_SHOVEL -> 5.0;
            case STONE_SHOVEL -> 3.0;
            case WOODEN_SHOVEL -> 2.0;
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
}

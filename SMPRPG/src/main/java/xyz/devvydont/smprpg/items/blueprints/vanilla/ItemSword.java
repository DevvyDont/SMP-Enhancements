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
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class ItemSword extends VanillaAttributeItem {

    public static double getSwordDamage(Material material) {
        return switch (material) {
            case NETHERITE_SWORD -> 320.0;
            case TRIDENT -> 115.0;
            case DIAMOND_SWORD -> 90.0;
            case GOLDEN_SWORD -> 25.0;
            case IRON_SWORD -> 10.0;
            case STONE_SWORD -> 7.0;
            case WOODEN_SWORD -> 4.0;
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
}

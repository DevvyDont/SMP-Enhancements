package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.items.ToolsUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemArmor extends VanillaAttributeItem implements ToolBreakable {

    public ItemArmor(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    public static int getDefenseFromMaterial(Material material) {

        return switch (material) {

            case ELYTRA -> 100;

            case LEATHER_HORSE_ARMOR -> 100;
            case IRON_HORSE_ARMOR -> 250;
            case GOLDEN_HORSE_ARMOR -> 500;
            case DIAMOND_HORSE_ARMOR -> 1000;
            case WOLF_ARMOR -> 500;

            case TURTLE_HELMET -> 10;

            case LEATHER_HELMET -> 2;
            case LEATHER_CHESTPLATE -> 5;
            case LEATHER_LEGGINGS -> 4;
            case LEATHER_BOOTS -> 2;

            case CHAINMAIL_HELMET -> 8;
            case CHAINMAIL_CHESTPLATE -> 12;
            case CHAINMAIL_LEGGINGS -> 10;
            case CHAINMAIL_BOOTS -> 9;

            case GOLDEN_HELMET -> 12;
            case GOLDEN_CHESTPLATE -> 18;
            case GOLDEN_LEGGINGS -> 15;
            case GOLDEN_BOOTS -> 10;

            case IRON_HELMET -> 10;
            case IRON_CHESTPLATE -> 15;
            case IRON_LEGGINGS -> 12;
            case IRON_BOOTS -> 8;

            case DIAMOND_HELMET -> 22;
            case DIAMOND_CHESTPLATE -> 30;
            case DIAMOND_LEGGINGS -> 25;
            case DIAMOND_BOOTS -> 20;

            case NETHERITE_HELMET -> 60;
            case NETHERITE_CHESTPLATE -> 75;
            case NETHERITE_LEGGINGS -> 65;
            case NETHERITE_BOOTS -> 55;

            default -> 0;
        };
    }

    /**
     * Gets the armor rating for a vanilla material.
     *
     * Armor is the amount of additional i-frames that a player gets
     * after taking damage.
     *
     * @param material The material fallback for an item.
     * @return How much armor it gives.
     */
    public static double getArmorFromMaterial(Material material) {

        return switch (material) {
            case NETHERITE_HELMET -> 2;
            case NETHERITE_CHESTPLATE -> 3;
            case NETHERITE_LEGGINGS -> 3;
            case NETHERITE_BOOTS -> 2;

            case DIAMOND_CHESTPLATE -> 1;
            case DIAMOND_LEGGINGS -> 1;

            default -> 0;
        };

    }

    public static double getHealthFromMaterial(Material material) {

        return switch (material) {

            case ELYTRA -> 100;

            case NETHERITE_HELMET -> 20;
            case NETHERITE_CHESTPLATE -> 25;
            case NETHERITE_LEGGINGS -> 25;
            case NETHERITE_BOOTS -> 20;

            case DIAMOND_HELMET -> 5;
            case DIAMOND_CHESTPLATE -> 5;
            case DIAMOND_LEGGINGS -> 5;
            case DIAMOND_BOOTS -> 5;

            default -> 0;
        };

    }

    public static double getDamageFromMaterial(Material material) {

        return switch (material) {
            case NETHERITE_HELMET, NETHERITE_BOOTS, NETHERITE_LEGGINGS, NETHERITE_CHESTPLATE -> .3;
            case DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS -> .15;
            case DIAMOND_HELMET, DIAMOND_BOOTS -> .12;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS -> .05;

            default -> 0;
        };

    }

    public static double getKnockbackResistanceFromMaterial(Material material) {
        return switch (material) {
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> .1;
            default -> 0;
        };
    }

    public static int getArmorPowerRating(Material material) {

        return switch (material) {

            case ELYTRA -> 62;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> ToolsUtil.NETHERITE_TOOL_POWER;

            case WOLF_ARMOR -> 30;
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS, DIAMOND_HORSE_ARMOR -> ToolsUtil.DIAMOND_TOOL_POWER;
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS, GOLDEN_HORSE_ARMOR -> ToolsUtil.GOLD_TOOL_POWER;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS, IRON_HORSE_ARMOR -> ToolsUtil.IRON_TOOL_POWER;
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS, TURTLE_HELMET -> 6;
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS, LEATHER_HORSE_ARMOR -> 4;

            default -> 1;
        };

    }

    public static int getMaxDurability(Material material) {
        return switch (material) {

            case ELYTRA -> 10_000;

            case NETHERITE_HELMET -> 30_600;
            case NETHERITE_CHESTPLATE -> 32_000;
            case NETHERITE_LEGGINGS -> 31_100;
            case NETHERITE_BOOTS -> 29_500;

            case DIAMOND_HELMET -> 10_200;
            case DIAMOND_CHESTPLATE -> 11_000;
            case DIAMOND_LEGGINGS -> 10_500;
            case DIAMOND_BOOTS -> 9_990;

            case GOLDEN_HELMET -> 3100;
            case GOLDEN_CHESTPLATE -> 3500;
            case GOLDEN_LEGGINGS -> 3200;
            case GOLDEN_BOOTS -> 2900;

            case IRON_HELMET -> 1800;
            case IRON_CHESTPLATE -> 2200;
            case IRON_LEGGINGS -> 1900;
            case IRON_BOOTS -> 1600;

            case CHAINMAIL_HELMET -> 730;
            case CHAINMAIL_CHESTPLATE -> 990;
            case CHAINMAIL_LEGGINGS -> 880;
            case CHAINMAIL_BOOTS -> 610;

            case LEATHER_HELMET -> 390;
            case LEATHER_CHESTPLATE -> 500;
            case LEATHER_LEGGINGS -> 440;
            case LEATHER_BOOTS -> 350;

            case TURTLE_HELMET -> 500;

            default -> material.getMaxDurability() * 100;
        };
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.resolveVanillaMaterial(getItem().getType());
    }

    @Override
    public int getPowerRating() {
        return getArmorPowerRating(getItem().getType());
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        List<AttributeEntry> modifiers = new ArrayList<>();

        // If we have true defense...
        double trueDef = getArmorFromMaterial(getItem().getType());
        if (trueDef > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.ARMOR, getArmorFromMaterial(getItem().getType())));

        // If we have health...
        double health = getHealthFromMaterial(getItem().getType());
        if (health > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.MAX_HEALTH, health));

        // If we have defense...
        double defense = getDefenseFromMaterial(getItem().getType());
        if (defense > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.ARMOR_TOUGHNESS, defense));

        // If we have knockback resist...
        double kbResist = getKnockbackResistanceFromMaterial(getItem().getType());
        if (kbResist > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.KNOCKBACK_RESISTANCE, kbResist));

        // If we have damage...
        double dmg = getDamageFromMaterial(getItem().getType());
        if (dmg > 0)
            modifiers.add(new ScalarAttributeEntry(AttributeWrapper.STRENGTH, dmg));

        // If we have no modifiers, we need to have something to get rid of the vanilla stats
        // Crappy armor won't have any attributes since defense isn't an attribute
        if (modifiers.isEmpty())
            modifiers.add(new AdditiveAttributeEntry(Attribute.ARMOR, 0));

        return modifiers;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getMaxDurability() {
        return getMaxDurability(getItem().getType());
    }
}

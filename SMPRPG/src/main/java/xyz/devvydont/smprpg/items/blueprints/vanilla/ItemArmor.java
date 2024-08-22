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

import java.util.*;

public class ItemArmor extends VanillaAttributeItem implements ToolBreakable {

    public ItemArmor(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    public static int getDefenseFromMaterial(Material material) {

        return switch (material) {

            case ELYTRA -> 150;

            case LEATHER_HORSE_ARMOR -> 100;
            case IRON_HORSE_ARMOR -> 250;
            case GOLDEN_HORSE_ARMOR -> 500;
            case DIAMOND_HORSE_ARMOR -> 1000;
            case WOLF_ARMOR -> 500;

            case TURTLE_HELMET -> 20;

            case LEATHER_HELMET -> 10;
            case LEATHER_CHESTPLATE -> 18;
            case LEATHER_LEGGINGS -> 15;
            case LEATHER_BOOTS -> 8;

            case CHAINMAIL_HELMET -> 24;
            case CHAINMAIL_CHESTPLATE -> 30;
            case CHAINMAIL_LEGGINGS -> 26;
            case CHAINMAIL_BOOTS -> 20;

            case GOLDEN_HELMET -> 35;
            case GOLDEN_CHESTPLATE -> 50;
            case GOLDEN_LEGGINGS -> 44;
            case GOLDEN_BOOTS -> 32;

            case IRON_HELMET -> 32;
            case IRON_CHESTPLATE -> 42;
            case IRON_LEGGINGS -> 38;
            case IRON_BOOTS -> 28;

            case DIAMOND_HELMET -> 115;
            case DIAMOND_CHESTPLATE -> 200;
            case DIAMOND_LEGGINGS -> 175;
            case DIAMOND_BOOTS -> 90;

            case NETHERITE_HELMET -> 100;
            case NETHERITE_CHESTPLATE -> 130;
            case NETHERITE_LEGGINGS -> 125;
            case NETHERITE_BOOTS -> 95;

            default -> 0;
        };
    }

    /**
     * True defense is the replaced vanilla armor stat. It caps at 30, and for this plugin True Defense will be
     * a flat damage reduction after all previous damage reductions.
     *
     * @param material
     * @return
     */
    public static double getTrueDefenseFromMaterial(Material material) {

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

            case NETHERITE_HELMET -> 90;
            case NETHERITE_CHESTPLATE -> 110;
            case NETHERITE_LEGGINGS -> 100;
            case NETHERITE_BOOTS -> 80;

            case DIAMOND_HELMET -> 10;
            case DIAMOND_CHESTPLATE -> 10;
            case DIAMOND_LEGGINGS -> 10;
            case DIAMOND_BOOTS -> 5;

            default -> 0;
        };

    }

    public static double getDamageFromMaterial(Material material) {

        return switch (material) {
            case NETHERITE_HELMET -> .18;
            case NETHERITE_CHESTPLATE -> .3;
            case NETHERITE_LEGGINGS -> .2;
            case NETHERITE_BOOTS -> .12;

            case DIAMOND_HELMET -> .04;
            case DIAMOND_CHESTPLATE -> .08;
            case DIAMOND_LEGGINGS -> .06;
            case DIAMOND_BOOTS -> .02;

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

            case ELYTRA -> 50_000;

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
    public Collection<AttributeEntry> getAttributeModifiers() {
        List<AttributeEntry> modifiers = new ArrayList<>();

        // If we have true defense...
        double trueDef = getTrueDefenseFromMaterial(getItem().getType());
        if (trueDef > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR, getTrueDefenseFromMaterial(getItem().getType())));

        // If we have health...
        double health = getHealthFromMaterial(getItem().getType());
        if (health > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.GENERIC_MAX_HEALTH, health));

        // If we have defense...
        double defense = getDefenseFromMaterial(getItem().getType());
        if (defense > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR_TOUGHNESS, defense));

        // If we have knockback resist...
        double kbResist = getKnockbackResistanceFromMaterial(getItem().getType());
        if (kbResist > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.GENERIC_KNOCKBACK_RESISTANCE, kbResist));

        // If we have damage...
        double dmg = getDamageFromMaterial(getItem().getType());
        if (dmg > 0)
            modifiers.add(new ScalarAttributeEntry(AttributeWrapper.STRENGTH, dmg));

        // If we have no modifiers, we need to have something to get rid of the vanilla stats
        // Crappy armor won't have any attributes since defense isn't an attribute
        if (modifiers.isEmpty())
            modifiers.add(new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR, 0));

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

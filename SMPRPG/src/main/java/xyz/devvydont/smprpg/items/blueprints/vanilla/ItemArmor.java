package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolsUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemArmor extends VanillaAttributeItem implements IBreakableEquipment {

    public ItemArmor(ItemService itemService, Material material) {
        super(itemService, material);
    }

    public static int getDefenseFromMaterial(Material material) {

        return switch (material) {

            case ELYTRA -> 100;

            case LEATHER_HORSE_ARMOR -> 100;
            case IRON_HORSE_ARMOR -> 250;
            case GOLDEN_HORSE_ARMOR -> 500;
            case DIAMOND_HORSE_ARMOR -> 1000;
            case WOLF_ARMOR -> 500;

            case TURTLE_HELMET -> 25;

            case LEATHER_HELMET -> 10;
            case LEATHER_CHESTPLATE -> 15;
            case LEATHER_LEGGINGS -> 13;
            case LEATHER_BOOTS -> 7;

            case CHAINMAIL_HELMET -> 12;
            case CHAINMAIL_CHESTPLATE -> 18;
            case CHAINMAIL_LEGGINGS -> 14;
            case CHAINMAIL_BOOTS -> 10;

            case GOLDEN_HELMET -> 12;
            case GOLDEN_CHESTPLATE -> 18;
            case GOLDEN_LEGGINGS -> 15;
            case GOLDEN_BOOTS -> 10;

            case IRON_HELMET -> 20;
            case IRON_CHESTPLATE -> 30;
            case IRON_LEGGINGS -> 25;
            case IRON_BOOTS -> 15;

            case DIAMOND_HELMET -> 30;
            case DIAMOND_CHESTPLATE -> 50;
            case DIAMOND_LEGGINGS -> 40;
            case DIAMOND_BOOTS -> 25;

            case NETHERITE_HELMET -> 75;
            case NETHERITE_CHESTPLATE -> 120;
            case NETHERITE_LEGGINGS -> 90;
            case NETHERITE_BOOTS -> 60;

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
            default -> 0;
        };

    }

    public static double getDamageFromMaterial(Material material) {

        return switch (material) {
            case NETHERITE_LEGGINGS, NETHERITE_CHESTPLATE, NETHERITE_BOOTS, NETHERITE_HELMET -> .1;
            default -> 0;
        };

    }

    public static double getIntelligenceFromMaterial(Material material) {
        return switch (material) {
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> 15;
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

            case ELYTRA, WOLF_ARMOR -> 30;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> ToolsUtil.NETHERITE_TOOL_POWER;
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS, DIAMOND_HORSE_ARMOR -> ToolsUtil.DIAMOND_TOOL_POWER;
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS, GOLDEN_HORSE_ARMOR -> ToolsUtil.GOLD_TOOL_POWER;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS, IRON_HORSE_ARMOR -> ToolsUtil.IRON_TOOL_POWER;
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS, TURTLE_HELMET -> 6;
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS, LEATHER_HORSE_ARMOR -> 3;

            default -> 1;
        };

    }

    public static int getMaxDurability(Material material) {
        return switch (material) {

            case ELYTRA -> 2_500;

            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> ToolsUtil.NETHERITE_TOOL_DURABILITY;
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> ToolsUtil.DIAMOND_TOOL_DURABILITY;
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> ToolsUtil.GOLD_TOOL_DURABILITY;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS -> ToolsUtil.IRON_TOOL_DURABILITY;
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS -> ToolsUtil.COPPER_TOOL_DURABILITY;
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS, TURTLE_HELMET -> ToolsUtil.WOOD_TOOL_DURABILITY;

            default -> material.getMaxDurability() * 10;
        };
    }


    @Override
    public int getPowerRating() {
        return getArmorPowerRating(material);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        List<AttributeEntry> modifiers = new ArrayList<>();

        // If we have true defense...
        double trueDef = getArmorFromMaterial(material);
        if (trueDef > 0)
            modifiers.add(new AdditiveAttributeEntry(AttributeWrapper.ARMOR, getArmorFromMaterial(material)));

        // If we have health...
        double health = getHealthFromMaterial(material);
        if (health > 0)
            modifiers.add(new AdditiveAttributeEntry(AttributeWrapper.HEALTH, health));

        // If we have defense...
        double defense = getDefenseFromMaterial(material);
        if (defense > 0)
            modifiers.add(new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, defense));

        // If we have knockback resist...
        double kbResist = getKnockbackResistanceFromMaterial(material);
        if (kbResist > 0)
            modifiers.add(new AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, kbResist));

        // If we have damage...
        double dmg = getDamageFromMaterial(material);
        if (dmg > 0)
            modifiers.add(new ScalarAttributeEntry(AttributeWrapper.STRENGTH, dmg));

        // If we have intelligence...
        double intelligence = getIntelligenceFromMaterial(material);
        if (intelligence > 0)
            modifiers.add(new AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, intelligence));

        // If we have no modifiers, we need to have something to get rid of the vanilla stats
        // Crappy armor won't have any attributes since defense isn't an attribute
        if (modifiers.isEmpty())
            modifiers.add(new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 0));

        return modifiers;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getMaxDurability() {
        return getMaxDurability(material);
    }
}

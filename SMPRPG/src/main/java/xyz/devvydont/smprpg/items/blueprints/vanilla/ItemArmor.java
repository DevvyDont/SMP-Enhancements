package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.Wearable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.*;

public class ItemArmor extends VanillaAttributeItem implements Wearable {

    public ItemArmor(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    public static int getArmorFromMaterial(Material material) {

        return switch (material) {

            case ELYTRA -> 50;

            case LEATHER_HORSE_ARMOR -> 100;
            case IRON_HORSE_ARMOR -> 250;
            case GOLDEN_HORSE_ARMOR -> 500;
            case DIAMOND_HORSE_ARMOR -> 1000;
            case WOLF_ARMOR -> 500;

            case TURTLE_HELMET -> 5;

            case LEATHER_HELMET -> 7;
            case LEATHER_CHESTPLATE -> 15;
            case LEATHER_LEGGINGS -> 10;
            case LEATHER_BOOTS -> 5;

            case CHAINMAIL_HELMET -> 12;
            case CHAINMAIL_CHESTPLATE -> 25;
            case CHAINMAIL_LEGGINGS -> 15;
            case CHAINMAIL_BOOTS -> 10;

            case GOLDEN_HELMET -> 25;
            case GOLDEN_CHESTPLATE -> 45;
            case GOLDEN_LEGGINGS -> 35;
            case GOLDEN_BOOTS -> 20;

            case IRON_HELMET -> 20;
            case IRON_CHESTPLATE -> 35;
            case IRON_LEGGINGS -> 25;
            case IRON_BOOTS -> 15;

            case DIAMOND_HELMET -> 60;
            case DIAMOND_CHESTPLATE -> 75;
            case DIAMOND_LEGGINGS -> 65;
            case DIAMOND_BOOTS -> 45;

            case NETHERITE_HELMET -> 105;
            case NETHERITE_CHESTPLATE -> 140;
            case NETHERITE_LEGGINGS -> 120;
            case NETHERITE_BOOTS -> 80;

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
            case NETHERITE_HELMET -> 60;
            case NETHERITE_CHESTPLATE -> 140;
            case NETHERITE_LEGGINGS -> 80;
            case NETHERITE_BOOTS -> 40;

            case DIAMOND_HELMET -> 30;
            case DIAMOND_CHESTPLATE -> 70;
            case DIAMOND_LEGGINGS -> 40;
            case DIAMOND_BOOTS -> 20;

            default -> 0;
        };

    }

    public static double getArmorToughnessFromMaterial(Material material) {

        return switch (material) {

            case DIAMOND_HORSE_ARMOR -> 2;

            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> 2;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> 3;

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

            case ELYTRA -> 50;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS -> 45;

            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS, DIAMOND_HORSE_ARMOR, WOLF_ARMOR -> 25;
            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS, GOLDEN_HORSE_ARMOR -> 15;
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS, IRON_HORSE_ARMOR -> 10;
            case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS, TURTLE_HELMET -> 7;
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS, LEATHER_HORSE_ARMOR -> 5;

            default -> 1;
        };

    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ARMOR;
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

        // If we have armor toughness...
        double toughness = getArmorToughnessFromMaterial(getItem().getType());
        if (toughness > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR_TOUGHNESS, toughness));

        // If we have knockback resist...
        double kbResist = getKnockbackResistanceFromMaterial(getItem().getType());
        if (kbResist > 0)
            modifiers.add(new AdditiveAttributeEntry(Attribute.GENERIC_KNOCKBACK_RESISTANCE, kbResist));

        // If we have no modifiers, we need to have something to get rid of the vanilla stats
        // Crappy armor won't have any attributes since defense isn't an attribute
        if (modifiers.isEmpty())
            modifiers.add(new AdditiveAttributeEntry(Attribute.GENERIC_ARMOR, 0));

        return modifiers;
    }

    @Override
    public int getDefense() {
        return getArmorFromMaterial(getItem().getType());
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }
}

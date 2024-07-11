package xyz.devvydont.smprpg.items.reforges.definitions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.reforges.ReforgeBase;
import xyz.devvydont.smprpg.items.reforges.ReforgeType;
import xyz.devvydont.smprpg.items.reforges.interfaces.*;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReforgeAccelerated extends ReforgeBase implements ArmorReforgeable, HarvestReforgeable, HoldingReforgeable, MeleeReforgeable, RangedReforgeable {

    public ReforgeAccelerated(ItemService itemService) {
        super(itemService);
    }

    public static float getMovementSpeedBuff(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> .20f;
            case UNCOMMON -> .25f;
            case RARE -> .30f;
            case EPIC -> .40f;
            case LEGENDARY -> .50f;
            case MYTHIC -> .80f;
            case DIVINE -> 1f;
            case TRANSCENDENT -> 1.35f;
            case SPECIAL -> 1.35f;
        } + 1.0f;
    }

    public static float getMiningSpeedBuff(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> .10f;
            case UNCOMMON -> .12f;
            case RARE -> .15f;
            case EPIC -> .20f;
            case LEGENDARY -> .30f;
            case MYTHIC -> .50f;
            case DIVINE -> .75f;
            case TRANSCENDENT -> 1.0f;
            case SPECIAL -> 1.0f;
        } + 1.0f;
    }

    @Override
    public List<Component> getDescription() {
        return Arrays.asList(
                Component.text("When applied, provides").color(NamedTextColor.GRAY),
                Component.text("an ").color(NamedTextColor.GRAY).append(Component.text("EXTREME").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD)).append(Component.text(" boost to")),
                Component.text("speed related stats.")
        );
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.ACCELERATED;
    }

    public Map<Attribute, AttributeModifier> getAttributesForGroup(ItemRarity rarity, EquipmentSlotGroup group) {
        return Map.of(
                Attribute.GENERIC_MOVEMENT_SPEED, generateMultiplicativeModifier(getMovementSpeedBuff(rarity), group),
                Attribute.PLAYER_BLOCK_BREAK_SPEED, generateMultiplicativeModifier(getMiningSpeedBuff(rarity), group)
        );
    }

    @Override
    public Map<Attribute, AttributeModifier> getArmorModifiers(ItemRarity rarity) {
        return getAttributesForGroup(rarity, EquipmentSlotGroup.ARMOR);
    }

    @Override
    public Map<Attribute, AttributeModifier> getHarvesterModifiers(ItemRarity rarity) {
        return getAttributesForGroup(rarity, EquipmentSlotGroup.MAINHAND);
    }

    @Override
    public Map<Attribute, AttributeModifier> getHeldModifiers(ItemRarity rarity) {
        return getAttributesForGroup(rarity, EquipmentSlotGroup.HAND);
    }

    @Override
    public Map<Attribute, AttributeModifier> getMeleeModifiers(ItemRarity rarity) {
        return getAttributesForGroup(rarity, EquipmentSlotGroup.MAINHAND);
    }

    @Override
    public Map<Attribute, AttributeModifier> getRangedModifiers(ItemRarity rarity) {
        return getAttributesForGroup(rarity, EquipmentSlotGroup.HAND);
    }
}

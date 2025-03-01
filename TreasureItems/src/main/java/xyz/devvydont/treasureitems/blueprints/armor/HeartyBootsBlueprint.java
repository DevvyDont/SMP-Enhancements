package xyz.devvydont.treasureitems.blueprints.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.util.ComponentUtils;
import xyz.devvydont.treasureitems.util.PotentialEnchantmentWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.*;

public class HeartyBootsBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_BOOTS);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.displayName(Component.text("Hearty Boots", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        itemMeta.addAttributeModifier(Attribute.MAX_HEALTH, generateAttributeModifier(Attribute.MAX_HEALTH, EquipmentSlotGroup.FEET, AttributeModifier.Operation.ADD_NUMBER, 2));
        itemMeta.addAttributeModifier(Attribute.ARMOR, generateAttributeModifier(Attribute.ARMOR, EquipmentSlotGroup.FEET, AttributeModifier.Operation.ADD_NUMBER, 3));
        itemMeta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, generateAttributeModifier(Attribute.ARMOR_TOUGHNESS, EquipmentSlotGroup.FEET, AttributeModifier.Operation.ADD_NUMBER, 2));

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.PROTECTION, new PotentialEnchantmentWrapper(Enchantment.PROTECTION, 3, 4, .95f));
        allowedEnchants.put(Enchantment.BLAST_PROTECTION, new PotentialEnchantmentWrapper(Enchantment.BLAST_PROTECTION, 2, 4, .15f));
        allowedEnchants.put(Enchantment.FIRE_PROTECTION, new PotentialEnchantmentWrapper(Enchantment.FIRE_PROTECTION, 2, 4, .15f));
        allowedEnchants.put(Enchantment.PROJECTILE_PROTECTION, new PotentialEnchantmentWrapper(Enchantment.PROJECTILE_PROTECTION, 2, 4, .15f));
        allowedEnchants.put(Enchantment.FEATHER_FALLING, new PotentialEnchantmentWrapper(Enchantment.FEATHER_FALLING, 3, 4, .9f));
        allowedEnchants.put(Enchantment.UNBREAKING, new PotentialEnchantmentWrapper(Enchantment.UNBREAKING, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.THORNS, new PotentialEnchantmentWrapper(Enchantment.THORNS, 1, 3, .05f));
        allowedEnchants.put(Enchantment.DEPTH_STRIDER, new PotentialEnchantmentWrapper(Enchantment.DEPTH_STRIDER, 2, 3, .75f));

        return allowedEnchants;
    }

    @Override
    protected List<Component> getExtraLore() {
        return List.of(
                ComponentUtils.UNREPAIRABLE_ENCHANTABLE
        );
    }

    @Override
    public String key() {
        return "hearty_boots";
    }
}

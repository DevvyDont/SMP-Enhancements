package me.devvy.customitems.blueprints.armor;

import me.devvy.customitems.blueprints.CustomItemBlueprint;
import me.devvy.customitems.util.PotentialEnchantmentWrapper;
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

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Hearty Boots");

        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_hp", 2, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 3, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "toughness", 2, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, new PotentialEnchantmentWrapper(Enchantment.PROTECTION_ENVIRONMENTAL, 3, 4, .95f));
        allowedEnchants.put(Enchantment.PROTECTION_EXPLOSIONS, new PotentialEnchantmentWrapper(Enchantment.PROTECTION_EXPLOSIONS, 2, 4, .15f));
        allowedEnchants.put(Enchantment.PROTECTION_FIRE, new PotentialEnchantmentWrapper(Enchantment.PROTECTION_FIRE, 2, 4, .15f));
        allowedEnchants.put(Enchantment.PROTECTION_PROJECTILE, new PotentialEnchantmentWrapper(Enchantment.PROTECTION_PROJECTILE, 2, 4, .15f));
        allowedEnchants.put(Enchantment.PROTECTION_FALL, new PotentialEnchantmentWrapper(Enchantment.PROTECTION_FALL, 3, 4, .9f));
        allowedEnchants.put(Enchantment.DURABILITY, new PotentialEnchantmentWrapper(Enchantment.DURABILITY, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.THORNS, new PotentialEnchantmentWrapper(Enchantment.THORNS, 1, 3, .05f));
        allowedEnchants.put(Enchantment.DEPTH_STRIDER, new PotentialEnchantmentWrapper(Enchantment.DEPTH_STRIDER, 2, 3, .75f));

        return allowedEnchants;
    }

    @Override
    protected List<String> getExtraLore() {
        return Arrays.asList(
                ChatColor.GRAY + "Cannot be " + ChatColor.RED + "repaired/enchanted" + ChatColor.GRAY + "!"
        );
    }

    @Override
    public String key() {
        return "hearty_boots";
    }
}

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

public class HeartyLeggingsBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_LEGGINGS);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Hearty Leggings");

        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_hp", 6, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 6, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "toughness", 2, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));

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
        allowedEnchants.put(Enchantment.DURABILITY, new PotentialEnchantmentWrapper(Enchantment.DURABILITY, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.THORNS, new PotentialEnchantmentWrapper(Enchantment.THORNS, 1, 3, .05f));
        allowedEnchants.put(Enchantment.SWIFT_SNEAK, new PotentialEnchantmentWrapper(Enchantment.SWIFT_SNEAK, 3, 4, .5f));

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
        return "hearty_leggings";
    }
}

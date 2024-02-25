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
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.*;

public class SpeedsterBootsBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Speedster Boots");

        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "speed", .3, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.FEET));

        itemMeta.setRepairCost(1000);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        leatherArmorMeta.setColor(org.bukkit.Color.fromRGB(255, 255, 0));
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
        allowedEnchants.put(Enchantment.DURABILITY, new PotentialEnchantmentWrapper(Enchantment.DURABILITY, 10, 20, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
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
        return "speester_boots";
    }
}

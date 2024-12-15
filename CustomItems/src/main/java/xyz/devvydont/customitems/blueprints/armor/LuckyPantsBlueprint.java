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

public class LuckyPantsBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Lucky Pants");

        itemMeta.addAttributeModifier(Attribute.GENERIC_LUCK, new AttributeModifier(UUID.randomUUID(), "luck", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));

        itemMeta.setRepairCost(1000);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        leatherArmorMeta.setColor(org.bukkit.Color.fromRGB(0, 255, 0));
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
        allowedEnchants.put(Enchantment.SWIFT_SNEAK, new PotentialEnchantmentWrapper(Enchantment.SWIFT_SNEAK, 2, 3, .6f));
        allowedEnchants.put(Enchantment.DURABILITY, new PotentialEnchantmentWrapper(Enchantment.DURABILITY, 10, 20, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));

        return allowedEnchants;
    }

    @Override
    protected List<String> getExtraLore() {
        return Arrays.asList(
                ChatColor.GRAY + "Wear to become " + ChatColor.GREEN + "lucky" + ChatColor.GRAY + "!",
                "",
                ChatColor.GRAY + "Cannot be " + ChatColor.RED + "repaired/enchanted" + ChatColor.GRAY + "!"
        );
    }

    @Override
    public String key() {
        return "lucky_pants";
    }
}

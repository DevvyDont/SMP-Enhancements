package xyz.devvydont.treasureitems.blueprints.armor;

import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
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

public class HeartyHelmetBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Hearty Helmet");

        itemMeta.addAttributeModifier(Attribute.MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "max_hp", 4, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        itemMeta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 3, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        itemMeta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "toughness", 2, org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));

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
        allowedEnchants.put(Enchantment.UNBREAKING, new PotentialEnchantmentWrapper(Enchantment.UNBREAKING, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.AQUA_AFFINITY, new PotentialEnchantmentWrapper(Enchantment.AQUA_AFFINITY, 1, 1, .75f));
        allowedEnchants.put(Enchantment.RESPIRATION, new PotentialEnchantmentWrapper(Enchantment.RESPIRATION, 3, 5, .9f));
        allowedEnchants.put(Enchantment.THORNS, new PotentialEnchantmentWrapper(Enchantment.THORNS, 1, 3, .05f));

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
        return "hearty_helmet";
    }
}

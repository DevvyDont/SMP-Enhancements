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

public class HeartyHelmetBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.displayName(Component.text("Hearty Helmet", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        itemMeta.addAttributeModifier(Attribute.MAX_HEALTH, generateAttributeModifier(Attribute.MAX_HEALTH, EquipmentSlotGroup.HEAD, AttributeModifier.Operation.ADD_NUMBER, 4));
        itemMeta.addAttributeModifier(Attribute.ARMOR, generateAttributeModifier(Attribute.ARMOR, EquipmentSlotGroup.HEAD, AttributeModifier.Operation.ADD_NUMBER, 3));
        itemMeta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, generateAttributeModifier(Attribute.ARMOR_TOUGHNESS, EquipmentSlotGroup.HEAD, AttributeModifier.Operation.ADD_NUMBER, 2));

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
    protected List<Component> getExtraLore() {
        return List.of(
                ComponentUtils.UNREPAIRABLE_ENCHANTABLE
        );
    }

    @Override
    public String key() {
        return "hearty_helmet";
    }
}

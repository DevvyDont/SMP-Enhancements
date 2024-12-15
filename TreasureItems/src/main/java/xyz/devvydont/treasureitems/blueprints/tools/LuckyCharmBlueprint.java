package xyz.devvydont.treasureitems.blueprints.tools;

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

public class LuckyCharmBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Lucky Charm");

        itemMeta.addAttributeModifier(Attribute.LUCK, new AttributeModifier(UUID.randomUUID(), "luck", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
        itemMeta.addAttributeModifier(Attribute.LUCK, new AttributeModifier(UUID.randomUUID(), "luck", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));


        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.FORTUNE, new PotentialEnchantmentWrapper(Enchantment.FORTUNE, 2, 5, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.LOOTING, new PotentialEnchantmentWrapper(Enchantment.LOOTING, 2, 5, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.LUCK_OF_THE_SEA, new PotentialEnchantmentWrapper(Enchantment.LUCK_OF_THE_SEA, 2, 5, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));

        return allowedEnchants;
    }

    @Override
    protected List<String> getExtraLore() {
        return Arrays.asList(
                ChatColor.GRAY + "Hold to become " + ChatColor.GREEN + "lucky" + ChatColor.GRAY + "!",
                "",
                ChatColor.GRAY + "Also functions as a normal",
                ChatColor.YELLOW + "Totem of Undying" + ChatColor.GRAY + "!"
        );
    }

    @Override
    public String key() {
        return "lucky_charm";
    }
}

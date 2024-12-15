package me.devvy.customitems.blueprints.tools;

import me.devvy.customitems.blueprints.CustomItemBlueprint;
import me.devvy.customitems.util.PotentialEnchantmentWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharpenedAxeBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_AXE);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Sharpened Axe");

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.DAMAGE_ALL, new PotentialEnchantmentWrapper(Enchantment.DAMAGE_ALL, 6, 8, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.DURABILITY, new PotentialEnchantmentWrapper(Enchantment.DURABILITY, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.DIG_SPEED, new PotentialEnchantmentWrapper(Enchantment.DIG_SPEED, 6, 8, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.SILK_TOUCH, new PotentialEnchantmentWrapper(Enchantment.SILK_TOUCH, 1, 1, .5f));


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
        return "sharpened_axe";
    }
}

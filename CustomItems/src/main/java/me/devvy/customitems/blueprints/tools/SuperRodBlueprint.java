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

public class SuperRodBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Super Rod");

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.DURABILITY, new PotentialEnchantmentWrapper(Enchantment.DURABILITY, 20, 50, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.LUCK, new PotentialEnchantmentWrapper(Enchantment.LUCK, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.LURE, new PotentialEnchantmentWrapper(Enchantment.LURE, 2, 5, .8f));


        return allowedEnchants;
    }

    @Override
    public void randomlyEnchant(ItemStack item, float luckBoost) {
        super.randomlyEnchant(item, luckBoost);

        // Make sure the unbreakable level is a multiple of 10
        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
        if (unbreakingLevel == 0)
            return;

        int newUnbreakingLevel = Math.round((unbreakingLevel+4.99f) / 10f) * 10;
        item.removeEnchantment(Enchantment.DURABILITY);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, newUnbreakingLevel);
    }

    @Override
    protected List<String> getExtraLore() {
        return Arrays.asList(
                ChatColor.GRAY + "Cannot be " + ChatColor.RED + "repaired/enchanted" + ChatColor.GRAY + "!"
        );
    }

    @Override
    public String key() {
        return "super_rod";
    }
}

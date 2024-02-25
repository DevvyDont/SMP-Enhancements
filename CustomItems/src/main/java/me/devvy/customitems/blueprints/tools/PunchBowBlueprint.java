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

public class PunchBowBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.BOW);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Punch Bow");

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.ARROW_KNOCKBACK, new PotentialEnchantmentWrapper(Enchantment.ARROW_KNOCKBACK, 5, 10, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.ARROW_DAMAGE, new PotentialEnchantmentWrapper(Enchantment.ARROW_DAMAGE, 1, 3, .5f));
        allowedEnchants.put(Enchantment.ARROW_FIRE, new PotentialEnchantmentWrapper(Enchantment.ARROW_FIRE, 1, 1, .2f));
        allowedEnchants.put(Enchantment.ARROW_INFINITE, new PotentialEnchantmentWrapper(Enchantment.ARROW_INFINITE, 1, 1, .8f));
        allowedEnchants.put(Enchantment.DURABILITY, new PotentialEnchantmentWrapper(Enchantment.DURABILITY, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));

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
        return "punch_bow";
    }
}

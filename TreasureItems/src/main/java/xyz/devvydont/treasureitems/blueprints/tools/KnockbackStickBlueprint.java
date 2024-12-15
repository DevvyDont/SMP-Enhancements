package xyz.devvydont.treasureitems.blueprints.tools;

import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.util.PotentialEnchantmentWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnockbackStickBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.STICK);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Knockback Stick");

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.FIRE_ASPECT, new PotentialEnchantmentWrapper(Enchantment.FIRE_ASPECT, 1, 2, .2f));
        allowedEnchants.put(Enchantment.KNOCKBACK, new PotentialEnchantmentWrapper(Enchantment.KNOCKBACK, 5, 10, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));

        return allowedEnchants;
    }

    @Override
    protected List<String> getExtraLore() {
        return Arrays.asList(
                ChatColor.GRAY + "Cannot be " + ChatColor.RED + "enchanted" + ChatColor.GRAY + "!"
        );
    }

    @Override
    public String key() {
        return "knockback_stick";
    }
}

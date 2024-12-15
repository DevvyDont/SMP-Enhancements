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

public class SharpenedSwordBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Sharpened Sword");

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.SHARPNESS, new PotentialEnchantmentWrapper(Enchantment.SHARPNESS, 7, 10, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.SMITE, new PotentialEnchantmentWrapper(Enchantment.SMITE, 3, 7, .25f));
        allowedEnchants.put(Enchantment.BANE_OF_ARTHROPODS, new PotentialEnchantmentWrapper(Enchantment.BANE_OF_ARTHROPODS, 3, 7, .25f));
        allowedEnchants.put(Enchantment.UNBREAKING, new PotentialEnchantmentWrapper(Enchantment.UNBREAKING, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.LOOTING, new PotentialEnchantmentWrapper(Enchantment.LOOTING, 2, 3, .8f));
        allowedEnchants.put(Enchantment.KNOCKBACK, new PotentialEnchantmentWrapper(Enchantment.KNOCKBACK, 1, 2, .25f));
        allowedEnchants.put(Enchantment.FIRE_ASPECT, new PotentialEnchantmentWrapper(Enchantment.FIRE_ASPECT, 1, 2, .25f));
        allowedEnchants.put(Enchantment.SWEEPING_EDGE, new PotentialEnchantmentWrapper(Enchantment.SWEEPING_EDGE, 2, 3, .7f));

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
        return "sharpened_sword";
    }
}

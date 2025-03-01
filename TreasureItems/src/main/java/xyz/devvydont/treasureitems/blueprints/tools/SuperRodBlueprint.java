package xyz.devvydont.treasureitems.blueprints.tools;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.util.ComponentUtils;
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

public class SuperRodBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.displayName(Component.text("Super Rod", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.UNBREAKING, new PotentialEnchantmentWrapper(Enchantment.UNBREAKING, 20, 50, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.LUCK_OF_THE_SEA, new PotentialEnchantmentWrapper(Enchantment.LUCK_OF_THE_SEA, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.LURE, new PotentialEnchantmentWrapper(Enchantment.LURE, 2, 5, .8f));


        return allowedEnchants;
    }

    @Override
    public void randomlyEnchant(ItemStack item, float luckBoost) {
        super.randomlyEnchant(item, luckBoost);

        // Make sure the unbreakable level is a multiple of 10
        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);
        if (unbreakingLevel == 0)
            return;

        int newUnbreakingLevel = Math.round((unbreakingLevel+4.99f) / 10f) * 10;
        item.removeEnchantment(Enchantment.UNBREAKING);
        item.addUnsafeEnchantment(Enchantment.UNBREAKING, newUnbreakingLevel);
    }

    @Override
    protected List<Component> getExtraLore() {
        return List.of(
                ComponentUtils.UNREPAIRABLE_ENCHANTABLE
        );
    }

    @Override
    public String key() {
        return "super_rod";
    }
}

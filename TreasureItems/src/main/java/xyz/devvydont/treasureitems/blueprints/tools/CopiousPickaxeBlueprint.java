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

public class CopiousPickaxeBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.displayName(Component.text("Copious Pickaxe", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.EFFICIENCY, new PotentialEnchantmentWrapper(Enchantment.EFFICIENCY, 4, 5, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.FORTUNE, new PotentialEnchantmentWrapper(Enchantment.FORTUNE, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.UNBREAKING, new PotentialEnchantmentWrapper(Enchantment.UNBREAKING, 4, 7, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));

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
        return "copious_pickaxe";
    }
}

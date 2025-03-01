package xyz.devvydont.treasureitems.blueprints.tools;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.meta.Damageable;
import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.util.ComponentUtils;
import xyz.devvydont.treasureitems.util.PotentialEnchantmentWrapper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GigadrillPickaxeBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.GOLDEN_PICKAXE);
        Repairable itemMeta = (Repairable) item.getItemMeta();
        Damageable damageable = (Damageable) itemMeta;
        damageable.setMaxDamage(64_000);

        itemMeta.displayName(Component.text("Gigadrill", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.EFFICIENCY, new PotentialEnchantmentWrapper(Enchantment.EFFICIENCY, 6, 10, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));
        allowedEnchants.put(Enchantment.SILK_TOUCH, new PotentialEnchantmentWrapper(Enchantment.SILK_TOUCH, 1, 1, .5f));
        allowedEnchants.put(Enchantment.UNBREAKING, new PotentialEnchantmentWrapper(Enchantment.UNBREAKING, 3, 10, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));

        return allowedEnchants;
    }

    @Override
    protected List<Component> getExtraLore() {
        return List.of(ComponentUtils.UNREPAIRABLE_ENCHANTABLE);
    }

    @Override
    public String key() {
        return "gigadrill_pickaxe";
    }

}

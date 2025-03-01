package xyz.devvydont.treasureitems.blueprints.tools;

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

public class LuckyCharmBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.displayName(Component.text("Lucky Charm", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        itemMeta.addAttributeModifier(Attribute.LUCK, generateAttributeModifier(Attribute.LUCK, EquipmentSlotGroup.HAND, AttributeModifier.Operation.ADD_NUMBER, 1));


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
    protected List<Component> getExtraLore() {

        return List.of(
                ComponentUtils.merge(Component.text("Hold to become ", NamedTextColor.GRAY), Component.text("lucky", NamedTextColor.GREEN), Component.text("!", NamedTextColor.GRAY)),
                Component.empty(),
                Component.text("Also functions as a normal", NamedTextColor.GRAY),
                ComponentUtils.merge(Component.text("Totem of Undying", NamedTextColor.YELLOW), Component.text("!", NamedTextColor.GRAY))
        );
    }

    @Override
    public String key() {
        return "lucky_charm";
    }
}

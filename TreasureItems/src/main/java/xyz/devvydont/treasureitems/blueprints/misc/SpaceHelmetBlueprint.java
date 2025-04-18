package xyz.devvydont.treasureitems.blueprints.misc;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.components.EquippableComponent;
import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.util.PotentialEnchantmentWrapper;

import java.util.HashMap;
import java.util.Map;

public class SpaceHelmetBlueprint extends CustomItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();
        Repairable repairable = (Repairable) item.getItemMeta();

        meta.displayName(Component.text("Space Helmet", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        meta.addAttributeModifier(Attribute.SAFE_FALL_DISTANCE, generateAttributeModifier(Attribute.SAFE_FALL_DISTANCE, EquipmentSlotGroup.HEAD, AttributeModifier.Operation.ADD_NUMBER, 50));
        meta.addAttributeModifier(Attribute.JUMP_STRENGTH, generateAttributeModifier(Attribute.JUMP_STRENGTH, EquipmentSlotGroup.HEAD, AttributeModifier.Operation.ADD_NUMBER, 2));
        meta.addAttributeModifier(Attribute.GRAVITY, generateAttributeModifier(Attribute.GRAVITY, EquipmentSlotGroup.HEAD, AttributeModifier.Operation.MULTIPLY_SCALAR_1, -.9));
        meta.addAttributeModifier(Attribute.FALL_DAMAGE_MULTIPLIER, generateAttributeModifier(Attribute.SAFE_FALL_DISTANCE, EquipmentSlotGroup.HEAD, AttributeModifier.Operation.MULTIPLY_SCALAR_1, -.75));

        repairable.setRepairCost(1000);

        meta.setMaxStackSize(1);
        meta.setEquippable(new ItemStack(Material.IRON_HELMET).getItemMeta().getEquippable());

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.PROTECTION, new PotentialEnchantmentWrapper(Enchantment.PROTECTION, 3, 4, .95f));
        allowedEnchants.put(Enchantment.BLAST_PROTECTION, new PotentialEnchantmentWrapper(Enchantment.BLAST_PROTECTION, 2, 4, .15f));
        allowedEnchants.put(Enchantment.FIRE_PROTECTION, new PotentialEnchantmentWrapper(Enchantment.FIRE_PROTECTION, 2, 4, .15f));
        allowedEnchants.put(Enchantment.PROJECTILE_PROTECTION, new PotentialEnchantmentWrapper(Enchantment.PROJECTILE_PROTECTION, 2, 4, .15f));
        allowedEnchants.put(Enchantment.AQUA_AFFINITY, new PotentialEnchantmentWrapper(Enchantment.AQUA_AFFINITY, 1, 1, .75f));
        allowedEnchants.put(Enchantment.RESPIRATION, new PotentialEnchantmentWrapper(Enchantment.RESPIRATION, 3, 5, .9f));
        allowedEnchants.put(Enchantment.THORNS, new PotentialEnchantmentWrapper(Enchantment.THORNS, 1, 3, .05f));

        return allowedEnchants;
    }

    @Override
    public String key() {
        return "space_helmet";
    }
}

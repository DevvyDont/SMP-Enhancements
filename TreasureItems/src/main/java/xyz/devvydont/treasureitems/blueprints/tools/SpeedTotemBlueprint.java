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

public class SpeedTotemBlueprint extends CustomItemBlueprint {

    public static final float SPEED_MIN_BOUND = .2f;
    public static final float SPEED_MAX_BOUND = .4f;

    private float getRandomSpeed() {
        float r = (float) (Math.random() * (SPEED_MAX_BOUND - SPEED_MIN_BOUND) + SPEED_MIN_BOUND);
        // Round it to the nearest hundredths place
        return (float) Math.round(r * 100) / 100;
    }

    public float getSpeed(ItemStack itemStack) {

        try {
            return (float) itemStack.getItemMeta().getAttributeModifiers(Attribute.MOVEMENT_SPEED).stream().findFirst().get().getAmount();
        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.displayName(Component.text("Speed Totem", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        float speed = getRandomSpeed();

        itemMeta.addAttributeModifier(Attribute.MOVEMENT_SPEED, generateAttributeModifier(Attribute.MOVEMENT_SPEED, EquipmentSlotGroup.HAND, AttributeModifier.Operation.MULTIPLY_SCALAR_1, speed));

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public int calculateScore(ItemStack itemStack) {
        // Return a number from 0-100 depending on the speed of the item
        return (int) Math.floor((getSpeed(itemStack) - SPEED_MIN_BOUND) / (SPEED_MAX_BOUND - SPEED_MIN_BOUND) * 100);
    }

    @Override
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        Map<Enchantment, PotentialEnchantmentWrapper> allowedEnchants = new HashMap<>();

        allowedEnchants.put(Enchantment.UNBREAKING, new PotentialEnchantmentWrapper(Enchantment.UNBREAKING, 1, 1, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));

        return allowedEnchants;
    }

    @Override
    protected List<Component> getExtraLore() {

        return List.of(
                ComponentUtils.merge(Component.text("Hold to become ", NamedTextColor.GRAY), Component.text("quick", NamedTextColor.AQUA), Component.text("!", NamedTextColor.GRAY)),
                Component.empty(),
                Component.text("Also functions as a normal", NamedTextColor.GRAY),
                ComponentUtils.merge(Component.text("Totem of Undying", NamedTextColor.YELLOW), Component.text("!", NamedTextColor.GRAY))
        );
    }

    @Override
    public String key() {
        return "speed_totem";
    }
}

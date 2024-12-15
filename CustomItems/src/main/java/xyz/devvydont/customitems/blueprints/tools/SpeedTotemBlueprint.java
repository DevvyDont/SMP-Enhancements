package me.devvy.customitems.blueprints.tools;

import me.devvy.customitems.blueprints.CustomItemBlueprint;
import me.devvy.customitems.util.PotentialEnchantmentWrapper;
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
            return (float) itemStack.getItemMeta().getAttributeModifiers(Attribute.GENERIC_MOVEMENT_SPEED).stream().findFirst().get().getAmount();
        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Speed Totem");

        float speed = getRandomSpeed();

        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "speed", speed, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.OFF_HAND));
        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "speed", speed, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HAND));


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

        allowedEnchants.put(Enchantment.DURABILITY, new PotentialEnchantmentWrapper(Enchantment.DURABILITY, 1, 1, PotentialEnchantmentWrapper.GUARANTEED_CHANCE));

        return allowedEnchants;
    }

    @Override
    protected List<String> getEnchantLore(ItemStack itemStack, boolean includeStats) {
        return new ArrayList<>();
    }

    @Override
    protected List<String> getExtraLore() {
        return Arrays.asList(
                ChatColor.GRAY + "Hold to become " + ChatColor.AQUA + "quick" + ChatColor.GRAY + "!",
                "",
                ChatColor.GRAY + "Also functions as a normal",
                ChatColor.YELLOW + "Totem of Undying" + ChatColor.GRAY + "!"
        );
    }

    @Override
    public String key() {
        return "speed_totem";
    }
}

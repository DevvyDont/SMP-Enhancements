package xyz.devvydont.smprpg.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnchantmentUtil {

    public static int getEnchantLevel(Enchantment enchantment, ItemStack item) {
        if (item == null || item.getItemMeta() == null)
            return 0;

        return item.getEnchantmentLevel(enchantment);
    }

    public static int getEnchantLevel(CustomEnchantment enchantment, ItemStack item) {
        return getEnchantLevel(enchantment.getEnchantment(), item);
    }

    /**
     * Given an entity that is able to wear armor, calculate how much total levels of a certain enchantment they
     * currently have
     *
     * @param enchantment
     * @param equipment
     * @return
     */
    public static int getWornEnchantLevel(Enchantment enchantment, @Nullable EntityEquipment equipment) {

        if (equipment == null)
            return 0;

        int sum = 0;
        sum += getEnchantLevel(enchantment, equipment.getHelmet());
        sum += getEnchantLevel(enchantment, equipment.getChestplate());
        sum += getEnchantLevel(enchantment, equipment.getLeggings());
        sum += getEnchantLevel(enchantment, equipment.getBoots());
        return sum;
    }

    public static int getWornEnchantLevel(CustomEnchantment enchantment, EntityEquipment equipment) {
        return getWornEnchantLevel(enchantment.getEnchantment(), equipment);
    }

    public boolean wearingEnchantment(Enchantment enchantment, EntityEquipment equipment) {
        return getWornEnchantLevel(enchantment, equipment) > 0;
    }

    public boolean wearingEnchantment(CustomEnchantment enchantment, EntityEquipment equipment) {
        return wearingEnchantment(enchantment.getEnchantment(), equipment);
    }

    public static int getHoldingEnchantLevel(Enchantment enchantment, EquipmentSlotGroup slot, @Nullable EntityEquipment equipment) {

        if (equipment == null)
            return 0;

        int main = getEnchantLevel(enchantment, equipment.getItemInMainHand());
        int off = getEnchantLevel(enchantment, equipment.getItemInOffHand());

        if (slot.equals(EquipmentSlotGroup.MAINHAND))
            return main;

        if (slot.equals(EquipmentSlotGroup.OFFHAND))
            return off;

        if (slot.equals(EquipmentSlotGroup.HAND))
            return main + off;

        return 0;
    }

    public static int getHoldingEnchantLevel(CustomEnchantment enchantment, EquipmentSlotGroup slot, EntityEquipment equipment) {
        return getHoldingEnchantLevel(enchantment.getEnchantment(), slot, equipment);
    }



}

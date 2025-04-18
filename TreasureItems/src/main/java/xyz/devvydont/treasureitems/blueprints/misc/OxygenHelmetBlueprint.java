package xyz.devvydont.treasureitems.blueprints.misc;

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
import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.util.PotentialEnchantmentWrapper;
import xyz.devvydont.treasureitems.util.RandomUtil;

import java.util.HashMap;
import java.util.Map;

public class OxygenHelmetBlueprint extends CustomItemBlueprint {

    public static final int MIN_OXYGEN = 3;
    public static final int MAX_OXYGEN = 10;

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.GLASS);
        ItemMeta meta = item.getItemMeta();
        Repairable repairable = (Repairable) item.getItemMeta();

        meta.displayName(Component.text("Oxygen Helmet", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        meta.addAttributeModifier(Attribute.OXYGEN_BONUS, generateAttributeModifier(Attribute.OXYGEN_BONUS, EquipmentSlotGroup.HEAD, AttributeModifier.Operation.ADD_NUMBER, RandomUtil.randomIntBound(MIN_OXYGEN, MAX_OXYGEN)));

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
        allowedEnchants.put(Enchantment.THORNS, new PotentialEnchantmentWrapper(Enchantment.THORNS, 1, 3, .05f));

        return allowedEnchants;
    }

    @Override
    public int calculateScore(ItemStack itemStack) {
        int enchantmentScore =  super.calculateScore(itemStack);
        int oxygenScore = (int) Math.floor((getOxygen(itemStack) - MIN_OXYGEN) / (MAX_OXYGEN - MIN_OXYGEN) * 100);
        return (enchantmentScore + oxygenScore) / 2;
    }

    public float getOxygen(ItemStack itemStack) {

        try {
            return (float) itemStack.getItemMeta().getAttributeModifiers(Attribute.OXYGEN_BONUS).stream().findFirst().get().getAmount();
        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    public String key() {
        return "oxygen_helmet";
    }

}

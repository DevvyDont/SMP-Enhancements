package me.devvy.customitems.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.regex.Pattern;

import static org.bukkit.enchantments.Enchantment.*;

public class FormatUtil {

    /**
     * Convert an integer to its roman numeral representation
     *
     * @param n
     * @return
     */
    public static String numToRomanNumeral(int n) {

        // Can't be asked to do this the right way rn
        switch (n) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";

            case 20:
                return "XX";

            case 30:
                return "XXX";

            case 40:
                return "XL";

            case 50:
                return "L";

            default:
                return String.valueOf(n);
        }

    }

    public static String enchantFriendlyName(Enchantment enchantment) {

        if (enchantment.equals(DURABILITY)) {
            return "Unbreaking";
        } else if (enchantment.equals(DIG_SPEED)) {
            return "Efficiency";
        } else if (enchantment.equals(LOOT_BONUS_BLOCKS)) {
            return "Fortune";
        } else if (enchantment.equals(SILK_TOUCH)) {
            return "Silk Touch";
        } else if (enchantment.equals(ARROW_DAMAGE)) {
            return "Power";
        } else if (enchantment.equals(ARROW_FIRE)) {
            return "Flame";
        } else if (enchantment.equals(ARROW_INFINITE)) {
            return "Infinity";
        } else if (enchantment.equals(ARROW_KNOCKBACK)) {
            return "Punch";
        } else if (enchantment.equals(BINDING_CURSE)) {
            return "Curse of Binding";
        } else if (enchantment.equals(CHANNELING)) {
            return "Channeling";
        } else if (enchantment.equals(DAMAGE_ALL)) {
            return "Sharpness";
        } else if (enchantment.equals(DAMAGE_ARTHROPODS)) {
            return "Bane of Arthropods";
        } else if (enchantment.equals(DAMAGE_UNDEAD)) {
            return "Smite";
        } else if (enchantment.equals(DEPTH_STRIDER)) {
            return "Depth Strider";
        } else if (enchantment.equals(WATER_WORKER)) {
            return "Aqua Affinity";
        } else if (enchantment.equals(FIRE_ASPECT)) {
            return "Fire Aspect";
        } else if (enchantment.equals(FROST_WALKER)) {
            return "Frost Walker";
        } else if (enchantment.equals(KNOCKBACK)) {
            return "Knockback";
        } else if (enchantment.equals(LOOT_BONUS_MOBS)) {
            return "Looting";
        } else if (enchantment.equals(LOYALTY)) {
            return "Loyalty";
        } else if (enchantment.equals(LUCK)) {
            return "Luck of the Sea";
        } else if (enchantment.equals(LURE)) {
            return "Lure";
        } else if (enchantment.equals(MENDING)) {
            return "Mending";
        } else if (enchantment.equals(OXYGEN)) {
            return "Respiration";
        } else if (enchantment.equals(PROTECTION_ENVIRONMENTAL)) {
            return "Protection";
        } else if (enchantment.equals(PROTECTION_EXPLOSIONS)) {
            return "Blast Protection";
        } else if (enchantment.equals(PROTECTION_FALL)) {
            return "Feather Falling";
        } else if (enchantment.equals(PROTECTION_FIRE)) {
            return "Fire Protection";
        } else if (enchantment.equals(PROTECTION_PROJECTILE)) {
            return "Projectile Protection";
        } else if (enchantment.equals(QUICK_CHARGE)) {
            return "Quick Charge";
        } else if (enchantment.equals(RIPTIDE)) {
            return "Riptide";
        } else if (enchantment.equals(THORNS)) {
            return "Thorns";
        } else if (enchantment.equals(VANISHING_CURSE)) {
            return "Curse of Vanishing";
        } else if (enchantment.equals(SWEEPING_EDGE)) {
            return "Sweeping Edge";
        } else if (enchantment.equals(SWIFT_SNEAK)) {
            return "Swift Sneak";
        } else if (enchantment.equals(IMPALING)) {
            return "Impaling";
        } else if (enchantment.equals(SOUL_SPEED)) {
            return "Soul Speed";
        } else {
            return "key error: " + enchantment.getKey().getKey();
        }

    }

    public static String getTitledString(String string) {
        String regex = "\\b(.)(.*?)\\b";
        String result = Pattern.compile(regex).matcher(string).replaceAll(
                matche -> matche.group(1).toUpperCase() + matche.group(2)
        );

        return result;
    }

    public static void spawnFireworksInstantly(Location location, Color color) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location.add(0, .5, 0), EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Builder effectBuilder = FireworkEffect.builder();
        effectBuilder.with(FireworkEffect.Type.BALL_LARGE);
        effectBuilder.withColor(color);
        meta.addEffect(effectBuilder.build());
        firework.setFireworkMeta(meta);
        firework.detonate();
    }

}

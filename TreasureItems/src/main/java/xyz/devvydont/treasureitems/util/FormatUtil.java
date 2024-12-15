package xyz.devvydont.treasureitems.util;

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
        return switch (n) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            case 20 -> "XX";
            case 30 -> "XXX";
            case 40 -> "XL";
            case 50 -> "L";
            default -> String.valueOf(n);
        };

    }

    public static String enchantFriendlyName(Enchantment enchantment) {

        if (enchantment.equals(UNBREAKING)) {
            return "Unbreaking";
        } else if (enchantment.equals(EFFICIENCY)) {
            return "Efficiency";
        } else if (enchantment.equals(FORTUNE)) {
            return "Fortune";
        } else if (enchantment.equals(SILK_TOUCH)) {
            return "Silk Touch";
        } else if (enchantment.equals(POWER)) {
            return "Power";
        } else if (enchantment.equals(FLAME)) {
            return "Flame";
        } else if (enchantment.equals(INFINITY)) {
            return "Infinity";
        } else if (enchantment.equals(PUNCH)) {
            return "Punch";
        } else if (enchantment.equals(BINDING_CURSE)) {
            return "Curse of Binding";
        } else if (enchantment.equals(CHANNELING)) {
            return "Channeling";
        } else if (enchantment.equals(SHARPNESS)) {
            return "Sharpness";
        } else if (enchantment.equals(BANE_OF_ARTHROPODS)) {
            return "Bane of Arthropods";
        } else if (enchantment.equals(SMITE)) {
            return "Smite";
        } else if (enchantment.equals(DEPTH_STRIDER)) {
            return "Depth Strider";
        } else if (enchantment.equals(AQUA_AFFINITY)) {
            return "Aqua Affinity";
        } else if (enchantment.equals(FIRE_ASPECT)) {
            return "Fire Aspect";
        } else if (enchantment.equals(FROST_WALKER)) {
            return "Frost Walker";
        } else if (enchantment.equals(KNOCKBACK)) {
            return "Knockback";
        } else if (enchantment.equals(LOOTING)) {
            return "Looting";
        } else if (enchantment.equals(LOYALTY)) {
            return "Loyalty";
        } else if (enchantment.equals(LUCK_OF_THE_SEA)) {
            return "Luck of the Sea";
        } else if (enchantment.equals(LURE)) {
            return "Lure";
        } else if (enchantment.equals(MENDING)) {
            return "Mending";
        } else if (enchantment.equals(RESPIRATION)) {
            return "Respiration";
        } else if (enchantment.equals(PROTECTION)) {
            return "Protection";
        } else if (enchantment.equals(BLAST_PROTECTION)) {
            return "Blast Protection";
        } else if (enchantment.equals(FEATHER_FALLING)) {
            return "Feather Falling";
        } else if (enchantment.equals(FIRE_PROTECTION)) {
            return "Fire Protection";
        } else if (enchantment.equals(PROJECTILE_PROTECTION)) {
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
        Firework firework = (Firework) location.getWorld().spawnEntity(location.add(0, .5, 0), EntityType.FIREWORK_ROCKET);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Builder effectBuilder = FireworkEffect.builder();
        effectBuilder.with(FireworkEffect.Type.BALL_LARGE);
        effectBuilder.withColor(color);
        meta.addEffect(effectBuilder.build());
        firework.setFireworkMeta(meta);
        firework.detonate();
    }

}

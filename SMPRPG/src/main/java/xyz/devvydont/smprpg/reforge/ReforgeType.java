package xyz.devvydont.smprpg.reforge;

import org.apache.commons.lang.StringUtils;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.reforge.definitions.AcceleratedReforge;
import xyz.devvydont.smprpg.reforge.definitions.HealthyReforge;
import xyz.devvydont.smprpg.reforge.definitions.HeartyReforge;
import xyz.devvydont.smprpg.reforge.definitions.UnimplementedReforge;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Contains ALL reforges that are possible to get
 * When reforges are applied, we can apply the following stats:
 *
 * HP
 * DEFENSE
 * ARMOR TOUGHNESS
 * KNOCKBACK RESIST
 *
 * MOVEMENT SPEED
 * MINING SPEED
 *
 * DAMAGE
 * ATTACK SPEED
 *
 * BLOCK REACH
 * COMBAT REACH
 *
 * SAFE FALL DISTANCE
 * FALL DAMAGE MULTIPLIER
 *
 * LUCK
 * JUMP/GRAVITY
 */
public enum ReforgeType {

    // HP oriented
    HEALTHY(HealthyReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
    HEARTY(HeartyReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//
//    // ARMOR oriented
//    DURABLE(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    FORTIFIED(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//
//    // KNOCKBACK oriented
//    FIRM(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    HEAVY(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    WEIGHTY(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//
//    // ALL AROUND ARMOR
//    CLEAN(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    DIRTY(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    ANCIENT(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    FRAIL(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    STRONG(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//
//    // Movement Speed oriented
//    LIGHT(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    SWIFT(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    AGILE(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//
//    // Mining/Dig speed oriented
//    HASTY(UnimplementedReforge.class, ItemClassification.TOOL),
//    RAPID(UnimplementedReforge.class, ItemClassification.TOOL),
//    QUICK(UnimplementedReforge.class, ItemClassification.TOOL),
//
//    // Luck oriented
//    LUCKY(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    COPIOUS(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    STAINED(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//    MYTHICAL(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),
//
//    // DAMAGE (melee)
//    SPICY(UnimplementedReforge.class, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),
//    SHARP(UnimplementedReforge.class, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),
//    DULL(UnimplementedReforge.class, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),
//    SLUGGISH(UnimplementedReforge.class, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),
//    SHARPENED(UnimplementedReforge.class, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),
//    STINGING(UnimplementedReforge.class, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),
//
//    // DAMAGE (ranged)
//    ODD(UnimplementedReforge.class, ItemClassification.BOW),
//    SLOW(UnimplementedReforge.class, ItemClassification.BOW),
//    PIERCING(UnimplementedReforge.class, ItemClassification.BOW),
//    POWERFUL(UnimplementedReforge.class, ItemClassification.BOW),
//    FORCEFUL(UnimplementedReforge.class, ItemClassification.BOW),
//
//    // ALL AROUND REFOREGES, generally what people would probably want in most cases
//    BALANCED(UnimplementedReforge.class, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),  // Swords/Axes
//    TUNED(UnimplementedReforge.class, ItemClassification.BOW),     // Bows
//    POLISHED(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),  // Armor

    // META reforges, only dropped from rare drops
    ACCELERATED(AcceleratedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),  // MAX Movement Speed
//    OVERCLOCKED(UnimplementedReforge.class, ItemClassification.TOOL),  // MAX Dig speed
//    EPHEMERAL(UnimplementedReforge.class, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),    // MAX Attack Speed
//    IMMORTAL(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),     // MAX DEF armor
//    TITANIC(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),      // MAX Knockback/Toughness
//    VIGOROUS(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),     // MAX HP armor
//    FORTUITOUS(UnimplementedReforge.class, ItemClassification.TOOL),   // MAX Luck
//    PROTRACTED(UnimplementedReforge.class, ItemClassification.TOOL, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),   // MAX reach
//
//    RENOWNED(UnimplementedReforge.class, ItemClassification.HELMET, ItemClassification.CHESTPLATE, ItemClassification.LEGGINGS, ItemClassification.BOOTS),     // All around armor
//    FABLED(UnimplementedReforge.class, ItemClassification.SWORD, ItemClassification.AXE, ItemClassification.MACE),       // All around melee
//    DEMONIC(UnimplementedReforge.class, ItemClassification.BOW),      // All around ranged

    ;

    private final Class<? extends ReforgeBase> handler;
    private final Collection<ItemClassification> allowedItems;

    ReforgeType(Class<? extends ReforgeBase> handler, ItemClassification...whitelist) {
        this.handler = handler;
        this.allowedItems = new HashSet<>();
        this.allowedItems.addAll(List.of(whitelist));

        if (whitelist.length <= 0)
            throw new IllegalStateException("Reforge must contain allowed item types!");
    }

    public String key() {
        return this.name().toLowerCase();
    }

    public static ReforgeType fromKey(String key) {
        return ReforgeType.valueOf(key.toUpperCase());
    }

    public String display() {
        return StringUtils.capitalize(key());
    }

    public Class<? extends ReforgeBase> getHandler() {
        return handler;
    }

    public Collection<ItemClassification> getAllowedItems() {
        return allowedItems;
    }

    /**
     * To be called once during manager instantiation. Used as a "helper" to create the singleton instance responsible
     * for a reforge.
     *
     * @return
     */
    public ReforgeBase createHandler() {
        try {
            return this.handler.getConstructor(this.getClass()).newInstance(this);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            SMPRPG.getInstance().getLogger().severe("Failed to instantiate handler for " + this.name() + "! Does the constructor match ReforgeBase?");
            throw new RuntimeException(e);
        }
    }
}

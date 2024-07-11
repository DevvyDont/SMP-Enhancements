package xyz.devvydont.smprpg.items.reforges;

import org.apache.commons.lang.StringUtils;

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
    HEALTHY,
    HEARTY,

    // ARMOR oriented
    DURABLE,
    FORTIFIED,

    // KNOCKBACK oriented
    FIRM,
    HEAVY,
    WEIGHTY,

    // ALL AROUND ARMOR
    CLEAN,
    DIRTY,
    ANCIENT,
    FRAIL,
    STRONG,

    // Movement Speed oriented
    LIGHT,
    SWIFT,
    AGILE,

    // Mining/Dig speed oriented
    HASTY,
    RAPID,
    QUICK,

    // Luck oriented
    LUCKY,
    COPIOUS,
    STAINED,
    MYTHICAL,

    // DAMAGE (melee)
    SPICY,
    SHARP,
    DULL,
    SLUGGISH,
    SHARPENED,
    STINGING,

    // DAMAGE (ranged)
    ODD,
    SLOW,
    PIERCING,
    POWERFUL,
    FORCEFUL,

    // ALL AROUND REFOREGES, generally what people would probably want in most cases
    BALANCED,  // Swords/Axes
    TUNED,     // Bows
    POLISHED,  // Armor

    // META reforges, only dropped from rare drops
    ACCELERATED,  // MAX Movement Speed
    OVERCLOCKED,  // MAX Dig speed
    EPHEMERAL,    // MAX Attack Speed
    IMMORTAL,     // MAX DEF armor
    TITANIC,      // MAX Knockback/Toughness
    VIGOROUS,     // MAX HP armor
    FORTUITOUS,   // MAX Luck
    PROTRACTED,   // MAX reach

    RENOWNED,     // All around armor
    FABLED,       // All around melee
    DEMONIC,      // All around ranged

    ;

    public String key() {
        return this.name().toLowerCase();
    }

    public static ReforgeType fromKey(String key) {
        return ReforgeType.valueOf(key.toUpperCase());
    }

    public String display() {
        return StringUtils.capitalize(key());
    }
}

package xyz.devvydont.smprpg.util.attributes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.w3c.dom.Attr;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to associate additional information alongside attributes.
 */
public enum AttributeWrapper {

    HEALTH(Attribute.GENERIC_MAX_HEALTH, "Health", AttributeType.POSITIVE,
            Component.text("The amount of ").color(NamedTextColor.GRAY)
                    .append(Component.text("health").color(NamedTextColor.GREEN))
                    .append(Component.text(" you have in half hearts.")).color(NamedTextColor.GRAY)),

    DEFENSE(Attribute.GENERIC_ARMOR_TOUGHNESS, "Defense", AttributeType.POSITIVE,
            Component.text("Amount of general ").color(NamedTextColor.GRAY)
                    .append(Component.text("damage reduction").color(NamedTextColor.RED))
                    .append(Component.text(" from all sources.").color(NamedTextColor.GRAY))),

    ARMOR(Attribute.GENERIC_ARMOR, "Armor", AttributeType.POSITIVE,
            Component.text("Amount of ").color(NamedTextColor.GRAY)
                    .append(Component.text("flat damage reduction").color(NamedTextColor.AQUA))
                    .append(Component.text(" applied after all damage calculations.")).color(NamedTextColor.GRAY)),

    ABSORPTION(Attribute.GENERIC_MAX_ABSORPTION, "Absorption", AttributeType.POSITIVE,
            Component.text("Amount of ").color(NamedTextColor.GRAY)
                    .append(Component.text("overflow absorption health").color(NamedTextColor.YELLOW))
                    .append(Component.text("is retained with the ").color(NamedTextColor.GRAY))
                    .append(Component.text("absorption").color(NamedTextColor.YELLOW))
                    .append(Component.text(" potion effect.")).color(NamedTextColor.GRAY)),

    KNOCKBACK_RESISTANCE(Attribute.GENERIC_KNOCKBACK_RESISTANCE, "Sturdiness", AttributeType.POSITIVE,
            Component.text("Reduces ").color(NamedTextColor.GRAY)
                    .append(Component.text("knockback").color(NamedTextColor.GOLD))
                    .append(Component.text(" inflicted from incoming damage.")).color(NamedTextColor.GRAY)),

    EXPLOSION_KNOCKBACK_RESISTANCE(Attribute.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE, "Explosion Knockback Resistance", AttributeType.POSITIVE,
            Component.text("Reduces ").color(NamedTextColor.GRAY)
                    .append(Component.text("knockback").color(NamedTextColor.GOLD))
                    .append(Component.text(" inflicted from explosions.")).color(NamedTextColor.GRAY)),

    BURNING_TIME(Attribute.GENERIC_BURNING_TIME, "Burn Time", AttributeType.NEGATIVE,
            Component.text("Amount of time ").color(NamedTextColor.GRAY)
                    .append(Component.text("fire ticks").color(NamedTextColor.RED))
                    .append(Component.text(" last when ignited.")).color(NamedTextColor.GRAY)),

    STRENGTH(Attribute.GENERIC_ATTACK_DAMAGE, "Strength", AttributeType.POSITIVE,
            Component.text("How much ").color(NamedTextColor.GRAY)
                    .append(Component.text("base damage").color(NamedTextColor.RED))
                    .append(Component.text(" is dealt when attacking.")).color(NamedTextColor.GRAY)),

    SWEEPING(Attribute.PLAYER_SWEEPING_DAMAGE_RATIO, "Sweep Damage", AttributeType.POSITIVE,
            Component.text("How much ").color(NamedTextColor.GRAY)
                    .append(Component.text("base damage").color(NamedTextColor.RED))
                    .append(Component.text(" is dealt when attacking with a ")).color(NamedTextColor.GRAY)
                    .append(Component.text("sweeping edge").color(NamedTextColor.GOLD))
                    .append(Component.text(" attack.").color(NamedTextColor.GRAY))),

    ATTACK_KNOCKBACK(Attribute.GENERIC_ATTACK_KNOCKBACK, "Knockback", AttributeType.POSITIVE,
            Component.text("How much ").color(NamedTextColor.GRAY)
                    .append(Component.text("outgoing knockback").color(NamedTextColor.GOLD))
                    .append(Component.text(" is applied when dealing damage.")).color(NamedTextColor.GRAY)),

    ATTACK_SPEED(Attribute.GENERIC_ATTACK_SPEED, "Recovery", AttributeType.POSITIVE,
            Component.text("How fast you ").color(NamedTextColor.GRAY)
                    .append(Component.text("recover").color(NamedTextColor.YELLOW))
                    .append(Component.text(" from the attack cooldown.")).color(NamedTextColor.GRAY)),


    MINING_SPEED(Attribute.PLAYER_BLOCK_BREAK_SPEED, "Mining Speed", AttributeType.POSITIVE,
            Component.text("The speed at which ").color(NamedTextColor.GRAY)
                    .append(Component.text("blocks").color(NamedTextColor.AQUA))
                    .append(Component.text(" can be broken.")).color(NamedTextColor.GRAY)),

    MINING_EFFICIENCY(Attribute.PLAYER_MINING_EFFICIENCY, "Mining Efficiency", AttributeType.POSITIVE,
            Component.text("The speed at which ").color(NamedTextColor.GRAY)
                    .append(Component.text("blocks").color(NamedTextColor.AQUA))
                    .append(Component.text(" can be mined when using the")).color(NamedTextColor.GRAY)
                    .append(Component.text(" correct tool.").color(NamedTextColor.GOLD))),

    UNDERWATER_MINING(Attribute.PLAYER_SUBMERGED_MINING_SPEED, "Underwater Mining Speed", AttributeType.POSITIVE,
            Component.text("The speed at which ").color(NamedTextColor.GRAY)
                    .append(Component.text("blocks").color(NamedTextColor.AQUA))
                    .append(Component.text(" can be broken while")).color(NamedTextColor.GRAY)
                    .append(Component.text(" underwater.").color(NamedTextColor.BLUE))),

    MOVEMENT_SPEED(Attribute.GENERIC_MOVEMENT_SPEED, "Speed", AttributeType.POSITIVE,
            Component.text("How quickly you ").color(NamedTextColor.GRAY)
                    .append(Component.text("traverse").color(NamedTextColor.WHITE))
                    .append(Component.text(" the world on foot.")).color(NamedTextColor.GRAY)),

    SNEAKING_SPEED(Attribute.PLAYER_SNEAKING_SPEED, "Sneak Speed", AttributeType.POSITIVE,
            Component.text("How quickly you ").color(NamedTextColor.GRAY)
                    .append(Component.text("traverse").color(NamedTextColor.WHITE))
                    .append(Component.text(" the world while sneaking.")).color(NamedTextColor.GRAY)
                    .append(Component.text(" while sneaking").color(NamedTextColor.GOLD))
                    .append(Component.text(".").color(NamedTextColor.GRAY))),

    MOVEMENT_EFFICIENCY(Attribute.GENERIC_MOVEMENT_EFFICIENCY, "Movement Efficiency", AttributeType.POSITIVE,
            Component.text("Decreases ").color(NamedTextColor.GRAY)
                    .append(Component.text("movement penalty").color(NamedTextColor.RED))
                    .append(Component.text(" when walking on certain surfaces.")).color(NamedTextColor.GRAY)),

    OXYGEN_BONUS(Attribute.GENERIC_OXYGEN_BONUS, "Lung Capacity", AttributeType.POSITIVE,
            Component.text("Amount of time ").color(NamedTextColor.GRAY)
                    .append(Component.text("breath can be held").color(NamedTextColor.AQUA))
                    .append(Component.text(" while underwater.")).color(NamedTextColor.GRAY)),


    WATER_MOVEMENT(Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY, "Water Speed", AttributeType.POSITIVE,
            Component.text("The speed of ").color(NamedTextColor.GRAY)
                    .append(Component.text("traversal").color(NamedTextColor.WHITE))
                    .append(Component.text(" while underwater.")).color(NamedTextColor.GRAY)),

    MINING_REACH(Attribute.PLAYER_BLOCK_INTERACTION_RANGE, "Mining Reach", AttributeType.POSITIVE,
            Component.text("The distance at which ").color(NamedTextColor.GRAY)
                    .append(Component.text("blocks").color(NamedTextColor.BLUE))
                    .append(Component.text(" can be mined.")).color(NamedTextColor.GRAY)),

    COMBAT_REACH(Attribute.PLAYER_ENTITY_INTERACTION_RANGE, "Combat Reach", AttributeType.POSITIVE,
            Component.text("The distance at which ").color(NamedTextColor.GRAY)
                    .append(Component.text("entities").color(NamedTextColor.BLUE))
                    .append(Component.text(" can be attacked.")).color(NamedTextColor.GRAY)),

    FOLLOW_RANGE(Attribute.GENERIC_FOLLOW_RANGE, "Follow Range", AttributeType.POSITIVE),


    FLYING_SPEED(Attribute.GENERIC_FLYING_SPEED, "Flying Speed", AttributeType.POSITIVE,
            Component.text("How quickly you ").color(NamedTextColor.GRAY)
                    .append(Component.text("fly").color(NamedTextColor.AQUA))
                    .append(Component.text(" around the world.")).color(NamedTextColor.GRAY)),

    FALL_DAMAGE_MULTIPLIER(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER, "Fall Damage%", AttributeType.NEGATIVE,
            Component.text("How much your ").color(NamedTextColor.GRAY)
                    .append(Component.text("fall damage").color(NamedTextColor.RED))
                    .append(Component.text(" is multiplied by.")).color(NamedTextColor.GRAY)),

    SAFE_FALL(Attribute.GENERIC_SAFE_FALL_DISTANCE, "Safe Fall", AttributeType.POSITIVE,
            Component.text("Affects the height at which ").color(NamedTextColor.GRAY)
                    .append(Component.text("fall damage").color(NamedTextColor.RED))
                    .append(Component.text(" is ignored.")).color(NamedTextColor.GRAY)),

    STEP(Attribute.GENERIC_STEP_HEIGHT, "Step", AttributeType.SPECIAL,
            Component.text("Affects the ").color(NamedTextColor.GRAY)
                    .append(Component.text("step height").color(NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text(" for climbing higher without jumping.")).color(NamedTextColor.GRAY)),

    GRAVITY(Attribute.GENERIC_GRAVITY, "Gravity", AttributeType.SPECIAL,
            Component.text("The strength of ").color(NamedTextColor.GRAY)
                    .append(Component.text("gravity's influence").color(NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text(" when airborne.")).color(NamedTextColor.GRAY)),

    JUMP_HEIGHT(Attribute.GENERIC_JUMP_STRENGTH, "Jump Height", AttributeType.SPECIAL,
            Component.text("How high ").color(NamedTextColor.GRAY)
                    .append(Component.text("jumping").color(NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text(" is.")).color(NamedTextColor.GRAY)),

    LUCK(Attribute.GENERIC_LUCK, "Luckiness", AttributeType.SPECIAL,
            Component.text("Rate at which ").color(NamedTextColor.GRAY)
                    .append(Component.text("rare items").color(NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text(" are dropped/generated.")).color(NamedTextColor.GRAY)),

    SCALE(Attribute.GENERIC_SCALE, "Size", AttributeType.SPECIAL,
            Component.text("How ").color(NamedTextColor.GRAY)
                    .append(Component.text("big or small").color(NamedTextColor.LIGHT_PURPLE))
                    .append(Component.text(" you are.")).color(NamedTextColor.GRAY)),

    ZOMBIE_REINFORCEMENTS(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS, "Zombie Reinforcements", AttributeType.SPECIAL)
    ;

    public enum AttributeType {
        POSITIVE,  // Having a positive value of this attribute means it helps the entity
        NEGATIVE,  // Having a negative value of this attribute means it helps the entity
        SPECIAL    // Whether this attribute is negative or positive it doesn't really matter
    }

    private final Attribute attribute;
    private final String clean;
    private final AttributeType type;
    private final Component description;

    AttributeWrapper(Attribute attribute, String clean, AttributeType type, Component description) {
        this.attribute = attribute;
        this.clean = clean;
        this.type = type;
        this.description = description;
    }

    AttributeWrapper(Attribute attribute, String clean, AttributeType type) {
        this.attribute = attribute;
        this.clean = clean;
        this.type = type;
        this.description = Component.text("This attribute does not have a description.");
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getCleanName() {
        return clean;
    }

    public AttributeType getAttributeType() {
        return type;
    }

    public Component getDescription() {
        return description;
    }

    private static final Map<Attribute, AttributeWrapper> attributeWrappers = new HashMap<>();

    static {
        for (AttributeWrapper wrapper : AttributeWrapper.values())
            attributeWrappers.put(wrapper.getAttribute(), wrapper);
    }

    public static AttributeWrapper ofAttribute(Attribute attribute) {
        return attributeWrappers.get(attribute);
    }

}

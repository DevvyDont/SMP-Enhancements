package xyz.devvydont.smprpg.util.attributes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to associate additional information alongside attributes.
 */
public enum AttributeWrapper {

    HEALTH(Attribute.GENERIC_MAX_HEALTH, "Health", AttributeType.POSITIVE,
            ComponentUtils.create("The amount of ")
                    .append(ComponentUtils.create("health", NamedTextColor.GREEN))
                    .append(ComponentUtils.create(" you have in half hearts."))),

    DEFENSE(Attribute.GENERIC_ARMOR_TOUGHNESS, "Defense", AttributeType.POSITIVE,
            ComponentUtils.create("Amount of general ")
                    .append(ComponentUtils.create("damage reduction", NamedTextColor.RED))
                    .append(ComponentUtils.create(" from all sources."))),

    ARMOR(Attribute.GENERIC_ARMOR, "Armor", AttributeType.POSITIVE,
            ComponentUtils.create("Amount of ")
                    .append(ComponentUtils.create("flat damage reduction", NamedTextColor.AQUA))
                    .append(ComponentUtils.create(" applied after all damage calculations."))),

    ABSORPTION(Attribute.GENERIC_MAX_ABSORPTION, "Absorption", AttributeType.POSITIVE,
            ComponentUtils.create("Amount of ")
                    .append(ComponentUtils.create("overflow absorption health", NamedTextColor.YELLOW))
                    .append(ComponentUtils.create("is retained with the "))
                    .append(ComponentUtils.create("absorption", NamedTextColor.YELLOW))
                    .append(ComponentUtils.create(" potion effect."))),

    KNOCKBACK_RESISTANCE(Attribute.GENERIC_KNOCKBACK_RESISTANCE, "Sturdiness", AttributeType.POSITIVE,
            ComponentUtils.create("Reduces ")
                    .append(ComponentUtils.create("knockback", NamedTextColor.GOLD))
                    .append(ComponentUtils.create(" inflicted from incoming damage."))),

    EXPLOSION_KNOCKBACK_RESISTANCE(Attribute.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE, "Explosion Knockback Resistance", AttributeType.POSITIVE,
            ComponentUtils.create("Reduces ")
                    .append(ComponentUtils.create("knockback", NamedTextColor.GOLD))
                    .append(ComponentUtils.create(" inflicted from explosions."))),

    BURNING_TIME(Attribute.GENERIC_BURNING_TIME, "Burn Time", AttributeType.NEGATIVE,
            ComponentUtils.create("Amount of time ")
                    .append(ComponentUtils.create("fire ticks", NamedTextColor.RED))
                    .append(ComponentUtils.create(" last when ignited."))),

    STRENGTH(Attribute.GENERIC_ATTACK_DAMAGE, "Strength", AttributeType.POSITIVE,
            ComponentUtils.create("How much ")
                    .append(ComponentUtils.create("base damage", NamedTextColor.RED))
                    .append(ComponentUtils.create(" is dealt when attacking."))),

    SWEEPING(Attribute.PLAYER_SWEEPING_DAMAGE_RATIO, "Sweep Damage", AttributeType.POSITIVE,
            ComponentUtils.create("How much ")
                    .append(ComponentUtils.create("base damage", NamedTextColor.RED))
                    .append(ComponentUtils.create(" is dealt when attacking with a "))
                    .append(ComponentUtils.create("sweeping edge", NamedTextColor.GOLD))
                    .append(ComponentUtils.create(" attack."))),

    ATTACK_KNOCKBACK(Attribute.GENERIC_ATTACK_KNOCKBACK, "Knockback", AttributeType.POSITIVE,
            ComponentUtils.create("How much ")
                    .append(ComponentUtils.create("outgoing knockback", NamedTextColor.GOLD))
                    .append(ComponentUtils.create(" is applied when dealing damage."))),

    ATTACK_SPEED(Attribute.GENERIC_ATTACK_SPEED, "Recovery", AttributeType.POSITIVE,
            ComponentUtils.create("How fast you ")
                    .append(ComponentUtils.create("recover", NamedTextColor.YELLOW))
                    .append(ComponentUtils.create(" from the attack cooldown."))),


    MINING_SPEED(Attribute.PLAYER_BLOCK_BREAK_SPEED, "Mining Speed", AttributeType.POSITIVE,
            ComponentUtils.create("The speed at which ")
                    .append(ComponentUtils.create("blocks", NamedTextColor.AQUA))
                    .append(ComponentUtils.create(" can be broken."))),

    MINING_EFFICIENCY(Attribute.PLAYER_MINING_EFFICIENCY, "Mining Efficiency", AttributeType.POSITIVE,
            ComponentUtils.create("The speed at which ")
                    .append(ComponentUtils.create("blocks", NamedTextColor.AQUA))
                    .append(ComponentUtils.create(" can be mined when using the"))
                    .append(ComponentUtils.create(" correct tool.", NamedTextColor.GOLD))),

    UNDERWATER_MINING(Attribute.PLAYER_SUBMERGED_MINING_SPEED, "Underwater Mining Speed", AttributeType.POSITIVE,
            ComponentUtils.create("The speed at which ")
                    .append(ComponentUtils.create("blocks", NamedTextColor.AQUA))
                    .append(ComponentUtils.create(" can be broken while"))
                    .append(ComponentUtils.create(" underwater.", NamedTextColor.BLUE))),

    MOVEMENT_SPEED(Attribute.GENERIC_MOVEMENT_SPEED, "Speed", AttributeType.POSITIVE,
            ComponentUtils.create("How quickly you ")
                    .append(ComponentUtils.create("traverse", NamedTextColor.WHITE))
                    .append(ComponentUtils.create(" the world on foot."))),

    SNEAKING_SPEED(Attribute.PLAYER_SNEAKING_SPEED, "Sneak Speed", AttributeType.POSITIVE,
            ComponentUtils.create("How quickly you ")
                    .append(ComponentUtils.create("traverse", NamedTextColor.WHITE))
                    .append(ComponentUtils.create(" the world while sneaking."))
                    .append(ComponentUtils.create(" while sneaking", NamedTextColor.GOLD))
                    .append(ComponentUtils.create("."))),

    MOVEMENT_EFFICIENCY(Attribute.GENERIC_MOVEMENT_EFFICIENCY, "Movement Efficiency", AttributeType.POSITIVE,
            ComponentUtils.create("Decreases ")
                    .append(ComponentUtils.create("movement penalty", NamedTextColor.RED))
                    .append(ComponentUtils.create(" when walking on certain surfaces."))),

    OXYGEN_BONUS(Attribute.GENERIC_OXYGEN_BONUS, "Lung Capacity", AttributeType.POSITIVE,
            ComponentUtils.create("Amount of time ")
                    .append(ComponentUtils.create("breath can be held", NamedTextColor.AQUA))
                    .append(ComponentUtils.create(" while underwater."))),


    WATER_MOVEMENT(Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY, "Water Speed", AttributeType.POSITIVE,
            ComponentUtils.create("The speed of ")
                    .append(ComponentUtils.create("traversal", NamedTextColor.WHITE))
                    .append(ComponentUtils.create(" while underwater."))),

    MINING_REACH(Attribute.PLAYER_BLOCK_INTERACTION_RANGE, "Mining Reach", AttributeType.POSITIVE,
            ComponentUtils.create("The distance at which ")
                    .append(ComponentUtils.create("blocks", NamedTextColor.BLUE))
                    .append(ComponentUtils.create(" can be mined."))),

    COMBAT_REACH(Attribute.PLAYER_ENTITY_INTERACTION_RANGE, "Combat Reach", AttributeType.POSITIVE,
            ComponentUtils.create("The distance at which ")
                    .append(ComponentUtils.create("entities", NamedTextColor.BLUE))
                    .append(ComponentUtils.create(" can be attacked."))),

    FOLLOW_RANGE(Attribute.GENERIC_FOLLOW_RANGE, "Follow Range", AttributeType.POSITIVE),


    FLYING_SPEED(Attribute.GENERIC_FLYING_SPEED, "Flying Speed", AttributeType.POSITIVE,
            ComponentUtils.create("How quickly you ")
                    .append(ComponentUtils.create("fly", NamedTextColor.AQUA))
                    .append(ComponentUtils.create(" around the world."))),

    FALL_DAMAGE_MULTIPLIER(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER, "Fall Damage%", AttributeType.NEGATIVE,
            ComponentUtils.create("How much your ")
                    .append(ComponentUtils.create("fall damage", NamedTextColor.RED))
                    .append(ComponentUtils.create(" is multiplied by."))),

    SAFE_FALL(Attribute.GENERIC_SAFE_FALL_DISTANCE, "Safe Fall", AttributeType.POSITIVE,
            ComponentUtils.create("Affects the height at which ")
                    .append(ComponentUtils.create("fall damage", NamedTextColor.RED))
                    .append(ComponentUtils.create(" is ignored."))),

    STEP(Attribute.GENERIC_STEP_HEIGHT, "Step", AttributeType.SPECIAL,
            ComponentUtils.create("Affects the ")
                    .append(ComponentUtils.create("step height", NamedTextColor.LIGHT_PURPLE))
                    .append(ComponentUtils.create(" for climbing higher without jumping."))),

    GRAVITY(Attribute.GENERIC_GRAVITY, "Gravity", AttributeType.SPECIAL,
            ComponentUtils.create("The strength of ")
                    .append(ComponentUtils.create("gravity's influence", NamedTextColor.LIGHT_PURPLE))
                    .append(ComponentUtils.create(" when airborne."))),

    JUMP_HEIGHT(Attribute.GENERIC_JUMP_STRENGTH, "Jump Height", AttributeType.SPECIAL,
            ComponentUtils.create("How high ")
                    .append(ComponentUtils.create("jumping", NamedTextColor.LIGHT_PURPLE))
                    .append(ComponentUtils.create(" is."))),

    LUCK(Attribute.GENERIC_LUCK, "Luckiness", AttributeType.SPECIAL,
            ComponentUtils.create("Rate at which ")
                    .append(ComponentUtils.create("rare items", NamedTextColor.LIGHT_PURPLE))
                    .append(ComponentUtils.create(" are dropped/generated."))),

    SCALE(Attribute.GENERIC_SCALE, "Size", AttributeType.SPECIAL,
            ComponentUtils.create("How ")
                    .append(ComponentUtils.create("big or small", NamedTextColor.LIGHT_PURPLE))
                    .append(ComponentUtils.create(" you are."))),

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
        this.description = ComponentUtils.create("This attribute does not have a description.");
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

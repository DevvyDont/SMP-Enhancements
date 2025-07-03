package xyz.devvydont.smprpg.attribute;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.create;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.merge;

/**
 * Contains all the possible attributes that can be associated with items, players, and entities.
 * This functions as a way to unify minecraft's vanilla attribute system and well as add our own
 * custom attributes that attempt to function extremely similar to how the vanilla attributes
 * work. The goal is to keep all the ugliness confined to the attribute package of the project
 * so that the rest of the project can simply call AttributeService.getAttribute(player, MANA).setBaseValue(100) etc.
 * This will also allow us to go more in depth with combat attributes, allowing things like crit damage and crit % (w/o jumping)
 */
public enum AttributeWrapper {

    // First, start with vanilla attributes. These attributes interact with the vanilla attribute system in our API.
    // Keep in mind, if an attribute has vanilla support, it should be used over a custom one.
    HEALTH(Attribute.MAX_HEALTH,
            "Health",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("The amount of "),
                create("health", GREEN),
                create(" you have in half hearts.")
            )),

    REGENERATION("Regeneration",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                    create("The effectiveness of"),
                    create(" passive health regeneration", GREEN),
                    create(".")
            )),

    DEFENSE("Defense",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("Amount of general "),
                create("damage reduction", RED),
                create(" from most sources.")
            )),

    INTELLIGENCE("Intelligence",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                    create("The "),
                    create("maximum mana", BLUE),
                    create(" available and effectiveness of"),
                    create(" magic", LIGHT_PURPLE),
                    create("/"),
                    create("abilities", GOLD),
                    create(".")
            )),

    STRENGTH(Attribute.ATTACK_DAMAGE,
            "Strength",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                    create("How much "),
                    create("base damage", RED),
                    create(" is dealt when attacking.")
            )),

    ATTACK_SPEED(Attribute.ATTACK_SPEED,
            "Attack Recovery",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                    create("How fast you "),
                    create("recover", YELLOW),
                    create(" from the attack cooldown."),
                    create(" (full attacks per second)", DARK_GRAY)
            )),

    MOVEMENT_SPEED(Attribute.MOVEMENT_SPEED,
            "Speed",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                    create("Generic "),
                    create("movement speed ", WHITE),
                    create("while on foot.")
            )),

    ARMOR(Attribute.ARMOR,
            "Armor",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("The delay before "),
                create("damage can be received again", RED),
                create("."),
                create(" (aka 'invincibility frames')", DARK_GRAY)
            )),

    ABSORPTION(Attribute.MAX_ABSORPTION,
            "Absorption Retention",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("Amount of "),
                create("overflow absorption health", YELLOW),
                create(" that is retained when the "),
                create("absorption", YELLOW),
                create(" potion effect runs out.")
            )),

    KNOCKBACK_RESISTANCE(Attribute.KNOCKBACK_RESISTANCE,
            "Knockback Resistance",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("Reduces "),
                create("knockback", RED),
                create(" inflicted from taking damage from certain sources.")
            )),

    EXPLOSION_KNOCKBACK_RESISTANCE(Attribute.EXPLOSION_KNOCKBACK_RESISTANCE,
            "Explosion Knockback Resistance",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("Reduces "),
                create("knockback", RED),
                create(" inflicted from explosions.")
            )),

    BURNING_TIME(Attribute.BURNING_TIME,
            "Burn Time",
            AttributeCategory.COMBAT,
            AttributeType.PUNISHING,
            merge(
                create("Amount of time "),
                create("fire ticks", RED),
                create(" last when ignited.")
            )),

    SWEEPING(Attribute.SWEEPING_DAMAGE_RATIO,
            "Sweeping Efficiency",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("The effectiveness of "),
                create("sweeping damage", RED),
                create(" relative to the base damage of "),
                create("sweeping edge", DARK_RED),
                create(" attacks.")
            )),

    ATTACK_KNOCKBACK(Attribute.ATTACK_KNOCKBACK,
            "Knockback",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("The amount of "),
                create("extra outgoing knockback", YELLOW),
                create(" to apply when dealing damage.")
            )),

    MINING_SPEED(Attribute.BLOCK_BREAK_SPEED,
            "Mining Speed",
            AttributeCategory.FORAGING,
            AttributeType.HELPFUL,
            merge(
                create("The speed at which "),
                create("any blocks", AQUA),
                create(" can be broken.")
            )),

    MINING_EFFICIENCY(Attribute.MINING_EFFICIENCY,
            "Mining Efficiency",
            AttributeCategory.FORAGING,
            AttributeType.HELPFUL,
            merge(
                create("The speed at which "),
                create("certain blocks", AQUA),
                create(" can be mined when using the"),
                create(" correct tool.", GOLD)
            )),

    UNDERWATER_MINING(Attribute.SUBMERGED_MINING_SPEED,
            "Underwater Mining Speed",
            AttributeCategory.FORAGING,
            AttributeType.HELPFUL,
            merge(
                create("The speed at which "),
                create("any blocks", AQUA),
                create(" can be broken while"),
                create(" submerged", BLUE),
                create(".")
            )),

    SNEAKING_SPEED(Attribute.SNEAKING_SPEED,
            "Sneak Speed",
            AttributeCategory.SPECIAL,
            AttributeType.HELPFUL,
            merge(
                create("The effectiveness of "),
                create("sneaking movement speed", WHITE),
                create(".")
            )),

    MOVEMENT_EFFICIENCY(Attribute.MOVEMENT_EFFICIENCY,
            "Movement Efficiency",
            AttributeCategory.SPECIAL,
            AttributeType.HELPFUL,
            merge(
                create("The effectiveness of "),
                create("movement speed when penalized", RED),
                create(" by walking on certain surfaces.")
            )),

    OXYGEN_BONUS(Attribute.OXYGEN_BONUS,
            "Lung Capacity",
            AttributeCategory.SPECIAL,
            AttributeType.HELPFUL,
            merge(
                create("Grants "),
                create("extra oxygen time", AQUA),
                create(" while holding breath underwater.")
            )),

    WATER_MOVEMENT(Attribute.WATER_MOVEMENT_EFFICIENCY,
            "Water Speed",
            AttributeCategory.SPECIAL,
            AttributeType.HELPFUL,
            merge(
                create("Generic "),
                create("movement speed", WHITE),
                create(" while underwater.")
            )),

    MINING_REACH(Attribute.BLOCK_INTERACTION_RANGE,
            "Mining Reach",
            AttributeCategory.FORAGING,
            AttributeType.HELPFUL,
            merge(
                create("The distance at which "),
                create("any blocks", AQUA),
                create(" can be mined/interacted with.")
            )),

    COMBAT_REACH(Attribute.ENTITY_INTERACTION_RANGE,
            "Combat Reach",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("The distance at which "),
                create("any entities", AQUA),
                create(" can be attacked/interacted with.")
            )),

    FOLLOW_RANGE(Attribute.FOLLOW_RANGE,
            "Follow Range",
            AttributeCategory.SPECIAL,
            AttributeType.HELPFUL),

    FLYING_SPEED(Attribute.FLYING_SPEED,
            "Flying Speed",
            AttributeCategory.SPECIAL,
            AttributeType.HELPFUL,
            merge(
                create("The "),
                create("speed", WHITE),
                create(" of flight.")

            )),

    FALL_DAMAGE_MULTIPLIER(Attribute.FALL_DAMAGE_MULTIPLIER,
            "Fall Damage",
            AttributeCategory.SPECIAL,
            AttributeType.PUNISHING,
            merge(
                create("The multiplier of incoming damage due to "),
                create("falling", RED),
                create(".")
            )),

    SAFE_FALL(Attribute.SAFE_FALL_DISTANCE,
            "Safe Fall",
            AttributeCategory.SPECIAL,
            AttributeType.HELPFUL,
            merge(
                create("The maximum height at which "),
                create("fall damage", RED),
                create(" is ignored.")
            )),

    STEP(Attribute.STEP_HEIGHT,
            "Step",
            AttributeCategory.SPECIAL,
            AttributeType.HELPFUL,
            merge(
                create("Affects the "),
                create("step height", LIGHT_PURPLE),
                create(" for climbing blocks without jumping.")
            )),

    GRAVITY(Attribute.GRAVITY,
            "Gravity",
            AttributeCategory.SPECIAL,
            AttributeType.SPECIAL,
            merge(
                create("The strength of "),
                create("gravity's influence", LIGHT_PURPLE),
                create(" when airborne.")
            )),

    JUMP_HEIGHT(Attribute.JUMP_STRENGTH,
            "Jump Strength",
            AttributeCategory.SPECIAL,
            AttributeType.SPECIAL,
            merge(
                create("Affects "),
                create("jump height", LIGHT_PURPLE),
                create(".")
            )),

    LUCK(Attribute.LUCK,
            "Luckiness",
            AttributeCategory.SPECIAL,
            AttributeType.HELPFUL,
            merge(
                create("The effectiveness of "),
                create("rare items", LIGHT_PURPLE),
                create(" dropping."),
                create(" (in most contexts!)", DARK_GRAY)
            )),

    SCALE(Attribute.SCALE,
            "Size",
            AttributeCategory.SPECIAL,
            AttributeType.SPECIAL,
            merge(
                create("How "),
                create("big or small", LIGHT_PURPLE),
                create(" you are.")
            )),

    ZOMBIE_REINFORCEMENTS(Attribute.SPAWN_REINFORCEMENTS,
            "Zombie Reinforcements",
            AttributeCategory.SPECIAL,
            AttributeType.SPECIAL),

    // Attributes that exist for backwards compatibility. These attributes do nothing, and exist so the plugin doesn't
    // spit tracebacks for old unconverted items.
    LEGACY_DEFENSE(Attribute.ARMOR_TOUGHNESS,
            "Defense (Legacy)",
            AttributeCategory.COMBAT,
            AttributeType.HELPFUL,
            merge(
                create("Vanilla Minecraft's 'armor toughness'. Completely ineffective in this plugin.")
            )),
    ;

    @Nullable
    public static AttributeWrapper fromKey(NamespacedKey attributeKey) {

        if (!attributeKey.getNamespace().equals("smprpg"))
            return null;
        try {
            return AttributeWrapper.valueOf(attributeKey.getKey().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Nullable
    public static AttributeWrapper fromAttribute(Attribute attribute) {
        for (AttributeWrapper attributeWrapper : AttributeWrapper.values())
            if (attribute.equals(attributeWrapper.getWrappedAttribute()))
                return attributeWrapper;
        return null;
    }
    
    @Nullable
    private Attribute _vanillaAttribute = null;
    
    @NotNull
    public final String DisplayName;
    
    @Nullable
    public final Component Description;
    
    @NotNull
    public final AttributeCategory Category;
    
    @NotNull
    public final AttributeType Type;

    @NotNull
    private final NamespacedKey _key;

    /**
     * Builds an attribute. This is called for either custom or vanilla ones.
     * @param displayName The safe display name to render on components in game.
     * @param category The category of the attribute for organization.
     * @param type The type of attribute, (helpful, harmful?)
     * @param description An explanation of what this attribute does.
     */
    AttributeWrapper(@NotNull String displayName,
                     @NotNull AttributeCategory category,
                     @NotNull AttributeType type,
                     @Nullable Component description) {
        this.DisplayName = displayName;
        this.Category = category;
        this.Type = type;
        this.Description = description;
        _key = new NamespacedKey("smprpg", this.name().toLowerCase());
    }

    /**
     * Builds an attribute without a description.
     * Some attributes don't exactly need a description, but every attribute probably should just to be safe.
     * @param displayName The safe display name to render on components in game.
     * @param category The category of the attribute for organization.
     * @param type The type of attribute, (helpful, harmful?)
     */
    AttributeWrapper(@NotNull String displayName,
                     @NotNull AttributeCategory category,
                     @NotNull AttributeType type) {
        this(displayName, category, type, null);
    }
    
    /**
     * Builds an attribute that is considered vanilla, meaning we are just wrapping over an already existing attribute.
     * @param vanillaAttribute The vanilla attribute type.
     * @param displayName The safe display name to render on components in game.
     * @param category The category of the attribute for organization.
     * @param type The type of attribute, (helpful, harmful?)
     * @param description An explanation of what this attribute does.
     */
    AttributeWrapper(@NotNull Attribute vanillaAttribute,
                     String displayName,
                     AttributeCategory category,
                     AttributeType type,
                     Component description) {
        this(displayName, category, type, description);
        _vanillaAttribute = vanillaAttribute;
    }

    /**
     * Builds an attribute that is considered vanilla, but omits the description.
     * @param vanillaAttribute The vanilla attribute type.
     * @param displayName The safe display name to render on components in game.
     * @param category The category of the attribute for organization.
     * @param type The type of attribute, (helpful, harmful?)
     */
    AttributeWrapper(Attribute vanillaAttribute, String displayName, AttributeCategory category, AttributeType type) {
        this(vanillaAttribute, displayName, category, type, create("This attribute does not have a description."));
    }



    /**
     * Checks if this is simply a wrapper for a vanilla attribute.
     * @return True if this is a vanilla attribute wrapper.
     */
    public boolean isVanilla() {
        return _vanillaAttribute != null;
    }

    /**
     * Checks if this is a custom and new attribute.
     * @return True if this is a custom attribute.
     */
    public boolean isCustom() {
        return !isVanilla();
    }

    /**
     * Gets the wrapped attribute this attribute refers to.
     * Keep in mind, this will only return non-null values when isVanilla() is true.
     * @return The vanilla attribute this instance is wrapping. Returns null if this is a custom attribute.
     */
    @Nullable
    public Attribute getWrappedAttribute() {
        return _vanillaAttribute;
    }

    /**
     * Retrieve the unique key for this attribute. Useful for persistent data containers.
     * @return A valid unique identifier for this attribute.
     */
    public @NotNull NamespacedKey key() {
        return _key;
    }
}

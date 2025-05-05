package xyz.devvydont.smprpg.attribute;

/**
 * Represents what kind of attribute this is depending on the effect it provides.
 */
public enum AttributeType {

    /**
     * This attribute is considered helpful when higher numbers of it are applied.
     */
    HELPFUL,

    /**
     * This attribute doesn't have defined behavior on whether it is going up or down. It is reserved for
     * situational or silly attributes. (Scale, gravity, etc.)
     */
    SPECIAL,

    /**
     * This attribute is considered a punishment if higher numbers of it are applied.
     * (Fall damage multiplier, attack speed, etc.)
     */
    PUNISHING;

}

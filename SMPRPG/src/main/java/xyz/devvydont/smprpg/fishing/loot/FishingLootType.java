package xyz.devvydont.smprpg.fishing.loot;

/**
 * Represents the different types of things you can fish up.
 */
public enum FishingLootType {

    /**
     * A standard fish. Has a common -> legendary rating, and acts as a standard vanilla fish. Chance rolling wise,
     * it fills in the gap of what's remaining from the sum of the other chances. Ideally, this should never go lower
     * than 50%.
     */
    FISH,

    /**
     * Treasure is a vanilla Minecraft concept, but it is much more prominent here. This can be plenty of things, but
     * they are meant to be general items that are not considered fish. Works like a normal loot pool
     * with no special rules like fish have. As long as the requirements for an item are met, it can roll.
     * Ideally, we never want this to go any higher than 10%.
     */
    TREASURE,

    /**
     * A new concept when comparing to vanilla, this "loot" is a custom entity that gets fished up that is meant to be
     * killed by the player. That entity will have its own loot pool, defined by its
     * {@link xyz.devvydont.smprpg.entity.base.CustomEntityInstance} class. The addition of this feature combats
     * AFK fishing and adds a bit more depth to fishing.
     * Ideally, we never want this to go higher than 10%.
     */
    CREATURE,

    /**
     * Also a vanilla Minecraft concept, this refers to mostly useless items you get from fishing. The presence of
     * "junk" allows the Luck attribute to actually do something, otherwise there would be no place for it.
     * This should have a starting value of 10%, and decrease with luck.
     */
    JUNK
}

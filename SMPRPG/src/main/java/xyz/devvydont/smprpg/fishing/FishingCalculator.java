package xyz.devvydont.smprpg.fishing;

/**
 * Handles calculating what an entity should fish up given the context of the catch. The following factors need to be
 * taken into account:
 * - Fishing catch quality rating
 * - Sea creature chance
 * - Treasure chance
 * - Junk chance
 * - Normal fish chance, fills in the blank.
 * - Biome
 * - Any special rod abilities
 * <p>
 * The general process is going to go something like this:
 * - Determine what pool to use: fish, junk, treasure, creature. This is determined by chance ratings. Luck stat decreases junk chance, fish chance is a fill in.
 * - After determining pool, filter out any options that don't fit requirements. These are tested by passing in a {@link FishingContext}.
 * - - Things can be filtered by stuff like catch quality, fishing level, biome, rod rating, etc.
 * - Randomly select an item from the pool based on weight.
 * - Apply any post-processing effects (via Bukkit event).
 *
 * It is important to note that the luck stat doesn't have that much of an impact here, since we are using a loot pool
 * rather than a "drop chance". The only thing your luck stat does for fishing is decrease your chance to fish up junk.
 */
public class FishingCalculator {
}

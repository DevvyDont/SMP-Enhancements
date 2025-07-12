package xyz.devvydont.smprpg.items.interfaces;

import net.kyori.adventure.text.format.TextColor;
import xyz.devvydont.smprpg.entity.fishing.SeaCreature;

import java.util.Set;

/**
 * An item that has the ability to cast lines. Every single material of this item should be a FISHING_ROD.
 * For our plugin, the only thing that fishing rods have custom logic for is what contexts it can fish in.
 */
public interface IFishingRod {

    enum FishingFlag {
        NORMAL("Water", SeaCreature.NAME_COLOR),
        LAVA("Lava", TextColor.color(255, 100, 28)),
        VOID("Void", TextColor.color(69, 56, 94)),
        ;

        /**
         * Fishing rods can be complex. Return a proper prefix for the rod when displaying an item tag.
         * @param flags The flags on the item.
         * @return A prefix.
         */
        public static String prefix(Set<FishingFlag> flags) {

            if (flags.isEmpty())
                return "";

            var listified = flags.stream().toList();

            // If there's only one, use that.
            if (listified.size() == 1)
                return listified.getFirst().Display;

            // If there's not 2, use multi.
            if (listified.size() > 2)
                return "Multi";

            // If this rod can do normal and lava
            if (flags.contains(FishingFlag.NORMAL) && flags.contains(FishingFlag.LAVA))
                return "Fluid";

            // If this rod can do normal and void
            if (flags.contains(FishingFlag.NORMAL) && flags.contains(FishingFlag.VOID))
                return "Drift";

            // If this rod can do lava and void
            if (flags.contains(FishingFlag.LAVA) && flags.contains(FishingFlag.VOID))
                return "Warped";

            return "Unknown";
        }

        /**
         * Used to construct the fishing rod prefix on the item type.
         */
        public final String Display;
        public final TextColor Color;

        FishingFlag(String display, TextColor color) {
            Display = display;
            Color = color;
        }
    }

    /**
     * Check what contexts this fishing rod is allowed to fish in. for example, if this rod can catch things in the
     * void then it will contain FishingFlag.VOID.
     * @return A set of fishing flags this rod contains.
     */
    Set<FishingFlag> getFishingFlags();

}

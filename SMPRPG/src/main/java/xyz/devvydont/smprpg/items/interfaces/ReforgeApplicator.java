package xyz.devvydont.smprpg.items.interfaces;

import xyz.devvydont.smprpg.reforge.ReforgeType;

/*
 * Represents an item that can be applied with a piece of gear to put a reforge on it in an anvil.
 */
public interface ReforgeApplicator {

    /*
     * The reforge that is associated with this item.
     */
    ReforgeType getReforgeType();

    /*
     * How much experience it should cost to apply this reforge to an item.
     */
    int getExperienceCost();

}

package xyz.devvydont.smprpg.items;

import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint;

/**
 * Used as a data information storage when our plugin attempts to read item types. This way, when we want information
 * about an item, we can retrieve any possible information we might need easily.
 *
 * Retrieve the following things:
 * - Enum of TYPE (VANILLA, CUSTOM)
 * - Blueprint instance (Some child class of SMPItem)
 */
public record SMPItemQuery(ItemType type, SMPItemBlueprint blueprint) {

    public enum ItemType {
        VANILLA,
        CUSTOM
    }

    public boolean isCustom() {
        return blueprint.isCustom();
    }

    /**
     * Retrieve the casted blueprint to vanilla item blueprint for quality of life, will throw class cast exception
     * if type != VANILLA
     */
    public VanillaItemBlueprint getVanillaItemBlueprint() {
        return (VanillaItemBlueprint) blueprint;
    }

    /**
     * Retrieve the casted blueprint to custom item blueprint for quality of life, will throw class cast exception
     * if type != CUSTOM
     */
    public CustomItemBlueprint getCustomItemBlueprint() {
        return (CustomItemBlueprint) blueprint;
    }






}

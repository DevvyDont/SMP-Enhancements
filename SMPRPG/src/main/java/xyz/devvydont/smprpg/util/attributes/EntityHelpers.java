package xyz.devvydont.smprpg.util.attributes;

import org.bukkit.entity.EntityType;

public class EntityHelpers {

    /**
     * Some vanilla entities will have a bare minimum level that they can spawn in as
     *
     * @param entityType
     * @return
     */
    public static int getMinimumLevel(EntityType entityType) {

        return switch (entityType) {

            case WARDEN -> 99;

            case BREEZE -> 65;
            case BOGGED -> 65;

            case SHULKER -> 60;
            case ENDER_DRAGON -> 50;
            case WITHER -> 50;

            case ENDERMAN -> 40;
            case SILVERFISH -> 30;

            case PIGLIN_BRUTE -> 45;

            case PIGLIN -> 30;
            case BLAZE -> 35;
            case WITHER_SKELETON -> 35;
            case MAGMA_CUBE -> 30;
            case ZOMBIFIED_PIGLIN -> 25;
            case STRIDER -> 30;
            case GHAST -> 25;
            case HOGLIN -> 30;
            case ZOGLIN -> 25;

            case VEX -> 15;
            case VINDICATOR -> 10;
            case PILLAGER -> 10;
            case RAVAGER -> 15;
            case ILLUSIONER -> 10;
            case EVOKER -> 12;

            case HUSK -> 5;
            case STRAY -> 5;
            case DROWNED -> 5;

            case ELDER_GUARDIAN -> 30;
            case GUARDIAN -> 25;

            case IRON_GOLEM -> 10;
            case VILLAGER -> 10;
            case ZOMBIE_VILLAGER -> 10;

            default -> 1;
        };
    }

}

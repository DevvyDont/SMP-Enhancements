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
            case PIGLIN_BRUTE -> 80;
            case PIGLIN -> 75;

            case BREEZE -> 65;
            case BOGGED -> 65;

            case SHULKER -> 60;
            case ENDER_DRAGON -> 50;
            case WITHER -> 50;

            case ENDERMAN -> 40;
            case SILVERFISH -> 30;

            case BLAZE -> 30;
            case WITHER_SKELETON -> 30;
            case MAGMA_CUBE -> 25;
            case ZOMBIFIED_PIGLIN -> 25;
            case STRIDER -> 30;
            case GHAST -> 25;
            case HOGLIN -> 25;
            case ZOGLIN -> 25;

            case VEX -> 15;
            case VINDICATOR -> 15;
            case PILLAGER -> 15;
            case RAVAGER -> 20;
            case ILLUSIONER -> 15;
            case EVOKER -> 15;

            case HUSK -> 10;
            case STRAY -> 10;
            case DROWNED -> 10;

            case ELDER_GUARDIAN -> 30;
            case GUARDIAN -> 25;

            case IRON_GOLEM -> 20;
            case VILLAGER -> 15;
            case ZOMBIE_VILLAGER -> 15;

            default -> 1;
        };
    }

}

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

            case WARDEN -> 100;

            case BREEZE -> 25;
            case BOGGED -> 25;

            case SHULKER -> 50;
            case ENDER_DRAGON -> 40;
            case WITHER -> 30;

            case ENDERMAN -> 35;
            case ENDERMITE -> 35;
            case SILVERFISH -> 35;

            case PIGLIN_BRUTE -> 40;

            case ALLAY -> 30;

            case PIGLIN -> 30;
            case BLAZE -> 30;
            case WITHER_SKELETON -> 25;
            case MAGMA_CUBE -> 20;
            case ZOMBIFIED_PIGLIN -> 20;
            case STRIDER -> 25;
            case GHAST -> 20;
            case HOGLIN -> 25;
            case ZOGLIN -> 25;
            case SNIFFER -> 25;

            case VEX -> 16;
            case VINDICATOR -> 20;
            case PILLAGER -> 15;
            case RAVAGER -> 20;
            case ILLUSIONER -> 20;
            case EVOKER -> 20;

            case HUSK -> 5;
            case STRAY -> 5;
            case DROWNED -> 5;

            case ELDER_GUARDIAN -> 20;
            case GUARDIAN -> 18;

            case WOLF -> 15;
            case HORSE -> 15;

            case SLIME -> 15;
            case WITCH -> 15;

            case IRON_GOLEM -> 25;
            case VILLAGER -> 20;
            case ZOMBIE_VILLAGER -> 10;

            case GOAT -> 10;
            case CAMEL -> 10;
            case DONKEY -> 10;
            case MULE -> 10;
            case PANDA -> 10;

            default -> 1;
        };
    }

}

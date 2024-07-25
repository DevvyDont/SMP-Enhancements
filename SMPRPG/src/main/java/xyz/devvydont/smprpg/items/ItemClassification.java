package xyz.devvydont.smprpg.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum ItemClassification {

    SWORD,
    MACE,
    BOW,         // Both bows and crossbows
    AXE,         // Some combat focused, some harvest focused
    ARMOR,       // Anything that can be worn
    EQUIPMENT,   // Misc stuff, shields, totems, things meant to be held and not worn
    TOOL,        // Pickaxes, Hoes, Shovels
    MATERIAL,    // Stuff used for crafting mainly, like ores and stuff
    CONSUMABLE,  // Stuff that can be eaten
    ITEM,        // Literally everything else

    ;

    /**
     * Given a vanilla minecraft item, determine its classification (so we can allow reforges on vanilla items)
     *
     * @param material
     * @return
     */
    public static ItemClassification resolveVanillaMaterial(@NotNull Material material) {

        if (material.asItemType() != null && material.asItemType().isEdible())
            return CONSUMABLE;

        return switch (material) {
            case IRON_SWORD, STONE_SWORD, DIAMOND_SWORD, GOLDEN_SWORD, NETHERITE_SWORD, WOODEN_SWORD -> SWORD;
            case BOW, CROSSBOW -> BOW;
            case DIAMOND_AXE, GOLDEN_AXE, IRON_AXE, NETHERITE_AXE, WOODEN_AXE, STONE_AXE -> AXE;
            case CHAINMAIL_HELMET, CHAINMAIL_BOOTS, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS,
                 DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS,
                 GOLDEN_HELMET, GOLDEN_BOOTS, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS,
                 IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS,
                 LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_BOOTS, LEATHER_LEGGINGS,
                 NETHERITE_HELMET, NETHERITE_BOOTS, NETHERITE_LEGGINGS, NETHERITE_CHESTPLATE,
                 TURTLE_HELMET-> ARMOR;
            case SHIELD, TOTEM_OF_UNDYING, FISHING_ROD -> EQUIPMENT;
            case DIAMOND_PICKAXE, GOLDEN_PICKAXE, IRON_PICKAXE, NETHERITE_PICKAXE, STONE_PICKAXE, WOODEN_PICKAXE,
                 DIAMOND_SHOVEL, GOLDEN_SHOVEL, IRON_SHOVEL, NETHERITE_SHOVEL, STONE_SHOVEL, WOODEN_SHOVEL,
                 DIAMOND_HOE, WOODEN_HOE, STONE_HOE, NETHERITE_HOE, GOLDEN_HOE, IRON_HOE -> TOOL;
            case COAL, COAL_BLOCK, COAL_ORE, DEEPSLATE_COAL_ORE,
                 IRON_INGOT, IRON_BLOCK, IRON_ORE, DEEPSLATE_IRON_ORE, RAW_IRON_BLOCK, RAW_IRON, IRON_NUGGET,
                 GOLD_INGOT, GOLD_BLOCK, GOLD_ORE, DEEPSLATE_GOLD_ORE, NETHER_GOLD_ORE, RAW_GOLD_BLOCK, RAW_GOLD, GOLD_NUGGET,
                 COPPER_INGOT, COPPER_BLOCK, COPPER_ORE, DEEPSLATE_COPPER_ORE, RAW_COPPER_BLOCK, RAW_COPPER,
                 DIAMOND, DIAMOND_BLOCK, DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE,
                 EMERALD, EMERALD_BLOCK, EMERALD_ORE, DEEPSLATE_EMERALD_ORE,
                 QUARTZ, QUARTZ_BLOCK, NETHER_QUARTZ_ORE,
                 LAPIS_LAZULI, LAPIS_BLOCK, LAPIS_ORE, DEEPSLATE_LAPIS_ORE,
                 REDSTONE, REDSTONE_BLOCK, REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE,
                 NETHERITE_BLOCK, NETHERITE_INGOT, NETHERITE_SCRAP, ANCIENT_DEBRIS,
                 AMETHYST_BLOCK, AMETHYST_CLUSTER, AMETHYST_SHARD,
                 PRISMARINE_CRYSTALS, PRISMARINE_SHARD,
                 FLINT, STICK
                  -> MATERIAL;

            case POTION, MILK_BUCKET, HONEY_BOTTLE, OMINOUS_BOTTLE -> CONSUMABLE;

            default -> ITEM;
        };
    }

    public static boolean reforgable(ItemClassification classification) {
        return switch (classification) {
            case SWORD, BOW, AXE, ARMOR, EQUIPMENT, TOOL -> true;
            default -> false;
        };
    }

}

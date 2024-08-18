package xyz.devvydont.smprpg.items.blueprints.resources;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a common crafting ingredient/material that is meant to be a simple vanilla material that can be sold.
 * Can include things such as diamonds, wheat, seeds, and dirt.
 *
 * todo make this into a file.
 */
public class VanillaResource extends VanillaItemBlueprint implements Sellable {

    private static final Map<Material, Integer> materialWorthMap;

    // Instantiate worth of items here. This is how the item service knows to register certain materials to this
    // class.
    static {
        materialWorthMap = new HashMap<>();

        // Materials from general world generation.
        materialWorthMap.put(Material.STONE, 1);
        materialWorthMap.put(Material.STONE_BRICKS, 1);
        materialWorthMap.put(Material.COBBLESTONE, 1);
        materialWorthMap.put(Material.GRANITE, 1);
        materialWorthMap.put(Material.DIORITE, 1);
        materialWorthMap.put(Material.ANDESITE, 1);
        materialWorthMap.put(Material.TUFF, 1);
        materialWorthMap.put(Material.BRICKS, 1);
        materialWorthMap.put(Material.DRIPSTONE_BLOCK, 1);
        materialWorthMap.put(Material.DEEPSLATE, 1);
        materialWorthMap.put(Material.COBBLED_DEEPSLATE, 1);
        materialWorthMap.put(Material.SAND, 1);
        materialWorthMap.put(Material.SANDSTONE, 1);
        materialWorthMap.put(Material.RED_SAND, 1);
        materialWorthMap.put(Material.RED_SANDSTONE, 1);
        materialWorthMap.put(Material.DIRT, 1);
        materialWorthMap.put(Material.OBSIDIAN, 30);
        materialWorthMap.put(Material.CRYING_OBSIDIAN, 40);

        // Materials from trees.
        materialWorthMap.put(Material.BAMBOO, 1);
        materialWorthMap.put(Material.BAMBOO_BLOCK, 2);
        materialWorthMap.put(Material.OAK_LOG, 3);
        materialWorthMap.put(Material.ACACIA_LOG, 3);
        materialWorthMap.put(Material.BIRCH_LOG, 3);
        materialWorthMap.put(Material.SPRUCE_LOG, 4);
        materialWorthMap.put(Material.DARK_OAK_LOG, 4);
        materialWorthMap.put(Material.JUNGLE_LOG, 5);
        materialWorthMap.put(Material.CHERRY_LOG, 5);
        materialWorthMap.put(Material.MANGROVE_LOG, 5);
        materialWorthMap.put(Material.CRIMSON_STEM, 8);
        materialWorthMap.put(Material.WARPED_STEM, 8);

        materialWorthMap.put(Material.BAMBOO_PLANKS, 1);
        materialWorthMap.put(Material.OAK_PLANKS, 1);
        materialWorthMap.put(Material.ACACIA_PLANKS, 1);
        materialWorthMap.put(Material.BIRCH_PLANKS, 1);
        materialWorthMap.put(Material.SPRUCE_PLANKS, 1);
        materialWorthMap.put(Material.DARK_OAK_PLANKS, 1);
        materialWorthMap.put(Material.JUNGLE_PLANKS, 1);
        materialWorthMap.put(Material.CHERRY_PLANKS, 1);
        materialWorthMap.put(Material.MANGROVE_PLANKS, 1);
        materialWorthMap.put(Material.CRIMSON_PLANKS, 1);
        materialWorthMap.put(Material.WARPED_PLANKS, 1);

        materialWorthMap.put(Material.OAK_LEAVES, 3);
        materialWorthMap.put(Material.ACACIA_LEAVES, 3);
        materialWorthMap.put(Material.BIRCH_LEAVES, 3);
        materialWorthMap.put(Material.SPRUCE_LEAVES, 4);
        materialWorthMap.put(Material.DARK_OAK_LEAVES, 4);
        materialWorthMap.put(Material.JUNGLE_LEAVES, 5);
        materialWorthMap.put(Material.CHERRY_LEAVES, 5);
        materialWorthMap.put(Material.MANGROVE_LEAVES, 5);
        materialWorthMap.put(Material.CRIMSON_HYPHAE, 8);
        materialWorthMap.put(Material.WARPED_WART_BLOCK, 8);

        // Materials in the ocean.
        materialWorthMap.put(Material.CLAY_BALL, 1);
        materialWorthMap.put(Material.CLAY, 2);
        materialWorthMap.put(Material.PRISMARINE, 1);
        materialWorthMap.put(Material.PRISMARINE_BRICKS, 1);
        materialWorthMap.put(Material.DARK_PRISMARINE, 1);
        materialWorthMap.put(Material.PRISMARINE_CRYSTALS, 3);

        // Materials from mining. These are used for gear sets so make sure they don't generate infinite money glitch
        materialWorthMap.put(Material.COAL, 10);
        materialWorthMap.put(Material.COAL_ORE, 5);
        materialWorthMap.put(Material.DEEPSLATE_COAL_ORE, 5);
        materialWorthMap.put(Material.COAL_BLOCK, 90);
        materialWorthMap.put(Material.CHARCOAL, 4);

        materialWorthMap.put(Material.COPPER_INGOT, 12);
        materialWorthMap.put(Material.COPPER_BLOCK, 108);
        materialWorthMap.put(Material.RAW_COPPER, 6);
        materialWorthMap.put(Material.COPPER_ORE, 6);
        materialWorthMap.put(Material.DEEPSLATE_COPPER_ORE, 6);

        materialWorthMap.put(Material.IRON_INGOT, 15);
        materialWorthMap.put(Material.IRON_BLOCK, 135);
        materialWorthMap.put(Material.IRON_ORE, 7);
        materialWorthMap.put(Material.DEEPSLATE_IRON_ORE, 7);
        materialWorthMap.put(Material.RAW_IRON, 7);

        materialWorthMap.put(Material.GOLD_INGOT, 70);
        materialWorthMap.put(Material.GOLD_BLOCK, 630);
        materialWorthMap.put(Material.DEEPSLATE_GOLD_ORE, 35);
        materialWorthMap.put(Material.GOLD_ORE, 35);
        materialWorthMap.put(Material.RAW_GOLD, 35);

        materialWorthMap.put(Material.AMETHYST_SHARD, 5);
        materialWorthMap.put(Material.AMETHYST_BLOCK, 20);

        materialWorthMap.put(Material.LAPIS_LAZULI, 20);
        materialWorthMap.put(Material.LAPIS_ORE, 10);
        materialWorthMap.put(Material.DEEPSLATE_LAPIS_ORE, 10);
        materialWorthMap.put(Material.LAPIS_BLOCK, 180);

        materialWorthMap.put(Material.REDSTONE, 20);
        materialWorthMap.put(Material.REDSTONE_ORE, 10);
        materialWorthMap.put(Material.DEEPSLATE_REDSTONE_ORE, 10);
        materialWorthMap.put(Material.REDSTONE_BLOCK, 180);

        materialWorthMap.put(Material.DIAMOND, 300);
        materialWorthMap.put(Material.DIAMOND_ORE, 150);
        materialWorthMap.put(Material.DEEPSLATE_DIAMOND_ORE, 150);
        materialWorthMap.put(Material.DIAMOND_BLOCK, 2700);

        materialWorthMap.put(Material.EMERALD_BLOCK, 4500);
        materialWorthMap.put(Material.EMERALD, 500);
        materialWorthMap.put(Material.EMERALD_ORE, 250);
        materialWorthMap.put(Material.DEEPSLATE_EMERALD_ORE, 250);

        materialWorthMap.put(Material.ANCIENT_DEBRIS, 1200);
        materialWorthMap.put(Material.NETHERITE_INGOT, 9000);
        materialWorthMap.put(Material.NETHERITE_BLOCK, 81000);

        // Materials from mobs.
        materialWorthMap.put(Material.ROTTEN_FLESH, 6);
        materialWorthMap.put(Material.BONE, 6);
        materialWorthMap.put(Material.STRING, 8);
        materialWorthMap.put(Material.SPIDER_EYE, 4);
        materialWorthMap.put(Material.PHANTOM_MEMBRANE, 35);
        materialWorthMap.put(Material.GUNPOWDER, 9);
        materialWorthMap.put(Material.SLIME_BALL, 12);
        materialWorthMap.put(Material.PRISMARINE_SHARD, 14);
        materialWorthMap.put(Material.NAUTILUS_SHELL, 100);
        materialWorthMap.put(Material.ECHO_SHARD, 250);
        materialWorthMap.put(Material.BLAZE_ROD, 25);
        materialWorthMap.put(Material.MAGMA_CREAM, 25);
        materialWorthMap.put(Material.NETHER_STAR, 5000);
        materialWorthMap.put(Material.ENDER_PEARL, 40);
        materialWorthMap.put(Material.SHULKER_SHELL, 290);

        // Materials from farming.
        materialWorthMap.put(Material.WHEAT_SEEDS, 1);
        materialWorthMap.put(Material.MELON_SEEDS, 1);
        materialWorthMap.put(Material.PUMPKIN_SEEDS, 1);
        materialWorthMap.put(Material.BEETROOT_SEEDS, 1);

        materialWorthMap.put(Material.WHEAT, 3);
        materialWorthMap.put(Material.BREAD, 7);
        materialWorthMap.put(Material.HAY_BLOCK, 27);
        materialWorthMap.put(Material.CARROT, 3);
        materialWorthMap.put(Material.POTATO, 3);
        materialWorthMap.put(Material.BAKED_POTATO, 7);
        materialWorthMap.put(Material.GOLDEN_CARROT, 15);
        materialWorthMap.put(Material.BEETROOT, 2);
        materialWorthMap.put(Material.SUGAR_CANE, 2);
        materialWorthMap.put(Material.PUMPKIN, 5);
        materialWorthMap.put(Material.MELON, 5);
        materialWorthMap.put(Material.CACTUS, 5);
        materialWorthMap.put(Material.MELON_SLICE, 1);
        materialWorthMap.put(Material.KELP, 1);
        materialWorthMap.put(Material.NETHER_WART, 5);

        materialWorthMap.put(Material.BEEF, 2);
        materialWorthMap.put(Material.COOKED_BEEF, 5);
        materialWorthMap.put(Material.MUTTON, 2);
        materialWorthMap.put(Material.COOKED_MUTTON, 4);
        materialWorthMap.put(Material.CHICKEN, 2);
        materialWorthMap.put(Material.COOKED_CHICKEN, 4);
        materialWorthMap.put(Material.PORKCHOP, 2);
        materialWorthMap.put(Material.COOKED_PORKCHOP, 4);
        materialWorthMap.put(Material.FEATHER, 2);
        materialWorthMap.put(Material.EGG, 2);
        materialWorthMap.put(Material.LEATHER, 5);

        // Materials in the nether.
        materialWorthMap.put(Material.BASALT, 1);
        materialWorthMap.put(Material.SMOOTH_BASALT, 1);
        materialWorthMap.put(Material.NETHERRACK, 1);
        materialWorthMap.put(Material.NETHER_BRICKS, 1);
        materialWorthMap.put(Material.RED_NETHER_BRICKS, 1);
        materialWorthMap.put(Material.SOUL_SAND, 2);
        materialWorthMap.put(Material.SOUL_SOIL, 2);

        // Materials found in the end.
        materialWorthMap.put(Material.END_STONE, 2);
        materialWorthMap.put(Material.END_STONE_BRICKS, 2);
        materialWorthMap.put(Material.PURPUR_BLOCK, 3);
        materialWorthMap.put(Material.PURPUR_PILLAR, 3);

        // Fishing
        materialWorthMap.put(Material.COD, 4);
        materialWorthMap.put(Material.SALMON, 4);
        materialWorthMap.put(Material.PUFFERFISH, 7);

        // Treasure items
        materialWorthMap.put(Material.NAME_TAG, 1000);
    }

    /**
     * Retrieves the value of a vanilla material. 0 If the material does not have a value.
     *
     * @param material Vanilla material to check against
     * @return An integer representing a sell price for a vanilla material
     */
    public static int getMaterialValue(Material material) {
        return materialWorthMap.getOrDefault(material, 0);
    }

    public static Map<Material, Integer> getMaterialWorthMap() {
        return materialWorthMap;
    }


    public VanillaResource(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public int getWorth() {
        return getMaterialValue(getItem().getType());
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }
}
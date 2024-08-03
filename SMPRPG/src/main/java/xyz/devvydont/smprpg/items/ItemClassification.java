package xyz.devvydont.smprpg.items;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.Material;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public enum ItemClassification {

    SWORD(ItemTypeTagKeys.ENCHANTABLE_WEAPON, ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON, ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING),
    TRIDENT(ItemTypeTagKeys.ENCHANTABLE_WEAPON, ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON, ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING, ItemTypeTagKeys.ENCHANTABLE_TRIDENT),
    MACE(ItemTypeTagKeys.ENCHANTABLE_MACE, ItemTypeTagKeys.ENCHANTABLE_WEAPON, ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING),
    // Both bows and crossbows
    BOW(ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING),
    // Some combat focused, some harvest focused
    AXE(ItemTypeTagKeys.ENCHANTABLE_WEAPON, ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON, ItemTypeTagKeys.ENCHANTABLE_MINING, ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING),
    // Anything that can be worn
    HELMET(ItemTypeTagKeys.ENCHANTABLE_HEAD_ARMOR, ItemTypeTagKeys.ENCHANTABLE_ARMOR, ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING),
    CHESTPLATE(ItemTypeTagKeys.ENCHANTABLE_CHEST_ARMOR, ItemTypeTagKeys.ENCHANTABLE_ARMOR, ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING),
    LEGGINGS(ItemTypeTagKeys.ENCHANTABLE_LEG_ARMOR, ItemTypeTagKeys.ENCHANTABLE_ARMOR, ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING),
    BOOTS(ItemTypeTagKeys.ENCHANTABLE_FOOT_ARMOR, ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING, ItemTypeTagKeys.ENCHANTABLE_ARMOR),
    // Various fishing rods
    FISHING_ROD(ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_FISHING, ItemTypeTagKeys.ENCHANTABLE_VANISHING),
    // Pickaxes, Hoes, Shovels
    TOOL(ItemTypeTagKeys.ENCHANTABLE_DURABILITY, ItemTypeTagKeys.ENCHANTABLE_VANISHING, ItemTypeTagKeys.ENCHANTABLE_MINING, ItemTypeTagKeys.ENCHANTABLE_MINING_LOOT),
    // Misc stuff, shields, totems, things meant to be held and not worn
    EQUIPMENT(ItemTypeTagKeys.ENCHANTABLE_VANISHING),
    // Stuff used for crafting mainly, like ores and stuff
    MATERIAL,
    // Stuff that can be eaten
    CONSUMABLE,
    // Literally everything else
    ITEM,
    ;

    private final Collection<TagKey<ItemType>> keys;

    @SafeVarargs
    ItemClassification(TagKey<ItemType>...keys) {
        this.keys = List.of(keys);
    }


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
            case TRIDENT -> TRIDENT;
            case BOW, CROSSBOW -> BOW;
            case DIAMOND_AXE, GOLDEN_AXE, IRON_AXE, NETHERITE_AXE, WOODEN_AXE, STONE_AXE -> AXE;
            case CHAINMAIL_HELMET, NETHERITE_HELMET, DIAMOND_HELMET, GOLDEN_HELMET, IRON_HELMET, LEATHER_HELMET, TURTLE_HELMET -> HELMET;
            case DIAMOND_CHESTPLATE, CHAINMAIL_CHESTPLATE, NETHERITE_CHESTPLATE, GOLDEN_CHESTPLATE, IRON_CHESTPLATE, LEATHER_CHESTPLATE, ELYTRA -> CHESTPLATE;
            case LEATHER_LEGGINGS, NETHERITE_LEGGINGS, DIAMOND_LEGGINGS, CHAINMAIL_LEGGINGS, GOLDEN_LEGGINGS, IRON_LEGGINGS -> LEGGINGS;
            case NETHERITE_BOOTS, DIAMOND_BOOTS, CHAINMAIL_BOOTS, GOLDEN_BOOTS, IRON_BOOTS, LEATHER_BOOTS -> BOOTS;
            case FISHING_ROD -> FISHING_ROD;
            case SHIELD, TOTEM_OF_UNDYING -> EQUIPMENT;
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

    public boolean reforgeable() {
        return switch (this) {
            case TRIDENT, SWORD, BOW, AXE, HELMET, CHESTPLATE, LEGGINGS, BOOTS, EQUIPMENT, TOOL -> true;
            default -> false;
        };
    }

    public boolean isArmor() {
        return switch (this) {
            case HELMET, CHESTPLATE, LEGGINGS, BOOTS -> true;
            default -> false;
        };
    }

    public Collection<TagKey<ItemType>> getItemTagKeys() {
        return keys;
    }
}

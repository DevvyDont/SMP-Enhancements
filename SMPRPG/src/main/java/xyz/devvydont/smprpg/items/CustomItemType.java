package xyz.devvydont.smprpg.items;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.blueprints.debug.EntityAnalyzer;
import xyz.devvydont.smprpg.items.blueprints.economy.CustomItemCoin;
import xyz.devvydont.smprpg.items.blueprints.equipment.GrapplingHook;
import xyz.devvydont.smprpg.items.blueprints.resources.EmptyBlueprint;
import xyz.devvydont.smprpg.items.blueprints.resources.mining.*;
import xyz.devvydont.smprpg.items.blueprints.resources.mob.*;
import xyz.devvydont.smprpg.items.blueprints.sets.copper.*;
import xyz.devvydont.smprpg.items.blueprints.sets.diamond.DiamondBow;
import xyz.devvydont.smprpg.items.blueprints.sets.infinity.*;
import xyz.devvydont.smprpg.items.blueprints.sets.iron.IronBow;
import xyz.devvydont.smprpg.items.blueprints.sets.singularity.SingularityChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.singularity.SingularityHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.singularity.SingularityLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.special.SpaceHelmet;

public enum CustomItemType {

    // COINS
    COPPER_COIN(   "Copper Coin",    Material.FIREWORK_STAR, CustomItemCoin.class),                                // 1 coin
    SILVER_COIN(   "Silver Coin",    Material.FIREWORK_STAR, ItemRarity.UNCOMMON,     CustomItemCoin.class),       // 10 coins
    GOLD_COIN(     "Gold Coin",      Material.FIREWORK_STAR, ItemRarity.RARE,         CustomItemCoin.class),       // 100 coins
    PLATINUM_COIN( "Platinum Coin",  Material.FIREWORK_STAR, ItemRarity.EPIC,         CustomItemCoin.class),       // 1k coins
    EMERALD_COIN(  "Emerald Coin",   Material.FIREWORK_STAR, ItemRarity.LEGENDARY,    CustomItemCoin.class),       // 10k coins
    AMETHYST_COIN( "Amethyst Coin",  Material.FIREWORK_STAR, ItemRarity.MYTHIC,       CustomItemCoin.class),       // 100k coins
    ENCHANTED_COIN("Enchanted Coin", Material.FIREWORK_STAR, ItemRarity.DIVINE, true, CustomItemCoin.class), // 1M coins

    // COPPER SET
    COPPER_PICKAXE("Copper Pickaxe",  Material.WOODEN_PICKAXE, CopperPickaxe.class),
    COPPER_AXE(    "Copper Axe",      Material.WOODEN_AXE,     CopperAxe.class),
    COPPER_HOE(    "Copper Hoe",      Material.WOODEN_HOE,     CopperHoe.class),
    COPPER_SHOVEL( "Copper Shovel",   Material.WOODEN_SHOVEL,  CopperShovel.class),
    COPPER_SWORD(  "Copper Sword",    Material.WOODEN_SWORD,   CopperSword.class),
    COPPER_BOW(    "Copper Bow",      Material.BOW,            CopperBow.class),

    COPPER_HELMET(    "Copper Helmet",     Material.HONEY_BLOCK,        CopperHelmet.class),
    COPPER_CHESTPLATE("Copper Chestplate", Material.LEATHER_CHESTPLATE, CopperChestplate.class),
    COPPER_LEGGINGS(  "Copper Leggings",   Material.LEATHER_LEGGINGS,   CopperLeggings.class),
    COPPER_BOOTS(     "Copper Boots",      Material.LEATHER_BOOTS,      CopperBoots.class),

    // BOWS
    DIAMOND_BOW("Diamond Bow", Material.BOW, ItemRarity.UNCOMMON,  DiamondBow.class),
    IRON_BOW(   "Iron Bow",    Material.BOW, ItemRarity.COMMON,  IronBow.class),

    // MISC TOOLS
    GRAPPLING_HOOK("Grappling Hook", Material.FISHING_ROD, ItemRarity.RARE, GrapplingHook.class),

    // COMPRESSED MINING MATERIALS

    // COAL
    ENCHANTED_COAL("Enchanted Coal",                Material.COAL,       ItemRarity.RARE,      true, CoalFamilyBlueprint.class),
    ENCHANTED_COAL_BLOCK("Enchanted Block of Coal", Material.COAL_BLOCK, ItemRarity.EPIC,      true, CoalFamilyBlueprint.class),
    COAL_SINGULARITY("Coal Singularity",            Material.COAL,       ItemRarity.LEGENDARY, true, CoalFamilyBlueprint.class),

    // CHARCOAL
    COMPRESSED_CHARCOAL("Compressed Charcoal", Material.CHARCOAL, ItemRarity.UNCOMMON, true, CharcoalFamilyBlueprint.class),
    ENCHANTED_CHARCOAL( "Enchanted Charcoal",  Material.CHARCOAL, ItemRarity.RARE,     true, CharcoalFamilyBlueprint.class),

    // FLINT
    COMPRESSED_FLINT("Compressed Flint", Material.FLINT, ItemRarity.UNCOMMON, true, FlintFamilyBlueprint.class),
    ENCHANTED_FLINT( "Enchanted Flint",  Material.FLINT, ItemRarity.RARE,     true, FlintFamilyBlueprint.class),

    // COPPER
    ENCHANTED_COPPER(      "Enchanted Copper",          Material.COPPER_INGOT, ItemRarity.RARE,      true, CopperFamilyBlueprint.class),
    ENCHANTED_COPPER_BLOCK("Enchanted Block of Copper", Material.COPPER_BLOCK, ItemRarity.EPIC,      true, CopperFamilyBlueprint.class),
    COPPER_SINGULARITY(    "Copper Singularity",        Material.COPPER_INGOT, ItemRarity.LEGENDARY, true, CopperFamilyBlueprint.class),

    // IRON
    ENCHANTED_IRON(      "Enchanted Iron",          Material.IRON_INGOT, ItemRarity.RARE,      true, IronFamilyBlueprint.class),
    ENCHANTED_IRON_BLOCK("Enchanted Block of Iron", Material.IRON_BLOCK, ItemRarity.EPIC,      true, IronFamilyBlueprint.class),
    IRON_SINGULARITY(    "Iron Singularity",        Material.IRON_INGOT, ItemRarity.LEGENDARY, true, IronFamilyBlueprint.class),

    // LAPIS
    ENCHANTED_LAPIS(      "Enchanted Lapis Lazuli",          Material.LAPIS_LAZULI, ItemRarity.RARE,      true, LapisFamilyBlueprint.class),
    ENCHANTED_LAPIS_BLOCK("Enchanted Block of Lapis Lazuli", Material.LAPIS_BLOCK,  ItemRarity.EPIC,      true, LapisFamilyBlueprint.class),
    LAPIS_SINGULARITY(    "Lapis Lazuli Singularity",        Material.LAPIS_LAZULI, ItemRarity.LEGENDARY, true, LapisFamilyBlueprint.class),

    // REDSTONE
    ENCHANTED_REDSTONE(      "Enchanted Redstone",          Material.REDSTONE,       ItemRarity.EPIC,      true, RedstoneFamilyBlueprint.class),
    ENCHANTED_REDSTONE_BLOCK("Enchanted Block of Redstone", Material.REDSTONE_BLOCK, ItemRarity.LEGENDARY, true, RedstoneFamilyBlueprint.class),
    REDSTONE_SINGULARITY(    "Redstone Singularity",        Material.REDSTONE,       ItemRarity.MYTHIC,    true, RedstoneFamilyBlueprint.class),

    // GLOWSTONE
    ENCHANTED_GLOWSTONE(      "Enchanted Glowstone",          Material.GLOWSTONE_DUST, ItemRarity.RARE,      true, GlowstoneFamilyBlueprint.class),
    ENCHANTED_GLOWSTONE_BLOCK("Enchanted Block of Glowstone", Material.GLOWSTONE,      ItemRarity.EPIC,      true, GlowstoneFamilyBlueprint.class),
    GLOWSTONE_SINGULARITY(    "Glowstone Singularity",        Material.GLOWSTONE_DUST, ItemRarity.LEGENDARY, true, GlowstoneFamilyBlueprint.class),

    // AMETHYST
    ENCHANTED_AMETHYST(      "Enchanted Amethyst",          Material.AMETHYST_SHARD, ItemRarity.RARE,      true, AmethystFamilyBlueprint.class),
    ENCHANTED_AMETHYST_BLOCK("Enchanted Block of Amethyst", Material.AMETHYST_BLOCK, ItemRarity.EPIC,      true, AmethystFamilyBlueprint.class),
    AMETHYST_SINGULARITY(    "Amethyst Singularity",        Material.AMETHYST_SHARD, ItemRarity.LEGENDARY, true, AmethystFamilyBlueprint.class),

    // GOLD
    ENCHANTED_GOLD(      "Enchanted Gold",          Material.GOLD_INGOT, ItemRarity.RARE,      true, GoldFamilyBlueprint.class),
    ENCHANTED_GOLD_BLOCK("Enchanted Block of Gold", Material.GOLD_BLOCK, ItemRarity.EPIC,      true, GoldFamilyBlueprint.class),
    GOLD_SINGULARITY(    "Gold Singularity",        Material.GOLD_INGOT, ItemRarity.LEGENDARY, true, GoldFamilyBlueprint.class),

    // DIAMOND
    ENCHANTED_DIAMOND(      "Enchanted Diamond",          Material.DIAMOND,       ItemRarity.EPIC,      true, DiamondFamilyBlueprint.class),
    ENCHANTED_DIAMOND_BLOCK("Enchanted Block of Diamond", Material.DIAMOND_BLOCK, ItemRarity.LEGENDARY, true, DiamondFamilyBlueprint.class),
    DIAMOND_SINGULARITY(    "Diamond Singularity",        Material.DIAMOND,       ItemRarity.MYTHIC,    true, DiamondFamilyBlueprint.class),

    // EMERALD
    ENCHANTED_EMERALD(      "Enchanted Emerald",          Material.EMERALD,       ItemRarity.EPIC,      true, EmeraldFamilyBlueprint.class),
    ENCHANTED_EMERALD_BLOCK("Enchanted Block of Emerald", Material.EMERALD_BLOCK, ItemRarity.LEGENDARY, true, EmeraldFamilyBlueprint.class),
    EMERALD_SINGULARITY(    "Emerald Singularity",        Material.EMERALD,       ItemRarity.MYTHIC,    true, EmeraldFamilyBlueprint.class),

    // QUARTZ
    ENCHANTED_QUARTZ(      "Enchanted Quartz",          Material.QUARTZ,       ItemRarity.RARE,      true, QuartzFamilyBlueprint.class),
    ENCHANTED_QUARTZ_BLOCK("Enchanted Block of Quartz", Material.QUARTZ_BLOCK, ItemRarity.EPIC,      true, QuartzFamilyBlueprint.class),
    QUARTZ_SINGULARITY(    "Quartz Singularity",        Material.QUARTZ,       ItemRarity.LEGENDARY, true, QuartzFamilyBlueprint.class),

    // NETHERITE
    ENCHANTED_NETHERITE(      "Enchanted Netherite",          Material.NETHERITE_INGOT, ItemRarity.LEGENDARY, true, NetheriteFamilyBlueprint.class),
    ENCHANTED_NETHERITE_BLOCK("Enchanted Block of Netherite", Material.NETHERITE_BLOCK, ItemRarity.MYTHIC,    true, NetheriteFamilyBlueprint.class),
    NETHERITE_SINGULARITY(    "Netherite Singularity",        Material.NETHERITE_INGOT, ItemRarity.DIVINE,    true, NetheriteFamilyBlueprint.class),

    // MOB DROPS (OVERWORLD)

    // ROTTEN FLESH
    PREMIUM_FLESH("Premium Flesh", Material.ROTTEN_FLESH, ItemRarity.UNCOMMON, true, FleshFamilyBlueprint.class),
    ENCHANTED_FLESH("Enchanted Flesh", Material.ROTTEN_FLESH, ItemRarity.RARE, true, FleshFamilyBlueprint.class),

    // BONE
    PREMIUM_BONE("Premium Bone", Material.BONE, ItemRarity.UNCOMMON, true, BoneFamilyBlueprint.class),
    ENCHANTED_BONE("Enchanted Bone", Material.BONE, ItemRarity.RARE, true, BoneFamilyBlueprint.class),

    // STRING
    PREMIUM_STRING("Premium String", Material.STRING, ItemRarity.UNCOMMON, true, StringFamilyBlueprint.class),
    ENCHANTED_STRING("Enchanted String", Material.STRING, ItemRarity.RARE, true, StringFamilyBlueprint.class),

    // SPIDER EYE
    PREMIUM_SPIDER_EYE("Premium Spider Eye", Material.SPIDER_EYE, ItemRarity.UNCOMMON, true, SpiderEyeFamilyBlueprint.class),
    ENCHANTED_SPIDER_EYE("Enchanted Spider Eye", Material.SPIDER_EYE, ItemRarity.RARE, true, SpiderEyeFamilyBlueprint.class),

    // SLIME BALL
    PREMIUM_SLIME("Premium Slime Ball", Material.SLIME_BALL, ItemRarity.UNCOMMON, true, SlimeFamilyBlueprint.class),
    ENCHANTED_SLIME("Enchanted Slime Ball", Material.SLIME_BALL, ItemRarity.RARE, true, SlimeFamilyBlueprint.class),

    // GUNPOWDER
    PREMIUM_GUNPOWDER("Premium Gunpowder", Material.GUNPOWDER, ItemRarity.UNCOMMON, true, GunpowderFamilyBlueprint.class),
    ENCHANTED_GUNPOWDER("Enchanted Gunpowder", Material.GUNPOWDER, ItemRarity.RARE, true, GunpowderFamilyBlueprint.class),

    // PRISMARINE SHARD
    PREMIUM_PRISMARINE_SHARD("Premium Prismarine Shard", Material.PRISMARINE_SHARD, ItemRarity.UNCOMMON, true, PrismarineShardFamilyBlueprint.class),
    ENCHANTED_PRISMARINE_SHARD("Enchanted Prismarine Shard", Material.PRISMARINE_SHARD, ItemRarity.RARE, true, PrismarineShardFamilyBlueprint.class),

    // PRISMARINE CRYSTAL
    PREMIUM_PRISMARINE_CRYSTAL("Premium Prismarine Crystals", Material.PRISMARINE_CRYSTALS, ItemRarity.UNCOMMON, true, PrismarineCrystalsFamilyBlueprint.class),
    ENCHANTED_PRISMARINE_CRYSTAL("Enchanted Prismarine Crystals", Material.PRISMARINE_CRYSTALS, ItemRarity.RARE, true, PrismarineCrystalsFamilyBlueprint.class),

    // NAUTILUS SHELL
    PREMIUM_NAUTILUS_SHELL("Premium Nautilus Shell", Material.NAUTILUS_SHELL, ItemRarity.RARE, true, NautilisShellFamilyBlueprint.class),
    ENCHANTED_NAUTILUS_SHELL("Enchanted Nautilus Shell", Material.NAUTILUS_SHELL, ItemRarity.EPIC, true, NautilisShellFamilyBlueprint.class),

    // ECHO SHARD
    PREMIUM_ECHO_SHARD("Premium Echo Shard", Material.ECHO_SHARD, ItemRarity.EPIC, true, EchoShardFamilyBlueprint.class),
    ENCHANTED_ECHO_SHARD("Enchanted Echo Shard", Material.ECHO_SHARD, ItemRarity.LEGENDARY, true, EchoShardFamilyBlueprint.class),

    // NETHER MOBS

    // BLAZE
    PREMIUM_BLAZE_ROD("Premium Blaze Rod", Material.BLAZE_ROD, ItemRarity.UNCOMMON, true, BlazeRodFamilyBlueprint.class),
    ENCHANTED_BLAZE_ROD("Enchanted Blaze Rod", Material.BLAZE_ROD, ItemRarity.RARE, true, BlazeRodFamilyBlueprint.class),

    // NETHER STAR
    PREMIUM_NETHER_STAR("Premium Nether Star", Material.NETHER_STAR, ItemRarity.LEGENDARY, true, NetherStarFamilyBlueprint.class),
    ENCHANTED_NETHER_STAR("Enchanted Nether Star", Material.NETHER_STAR, ItemRarity.MYTHIC, true, NetherStarFamilyBlueprint.class),

    // MAGMA
    PREMIUM_MAGMA_CREAM("Premium Magma Cream", Material.MAGMA_CREAM, ItemRarity.UNCOMMON, true, MagmaCreamFamilyBlueprint.class),
    ENCHANTED_MAGMA_CREAM("Enchanted Magma Cream", Material.MAGMA_CREAM, ItemRarity.RARE, true, MagmaCreamFamilyBlueprint.class),

    // END MOBS

    // ENDER PEARL
    PREMIUM_ENDER_PEARL("Premium Ender Pearl", Material.ENDER_PEARL, ItemRarity.RARE, true, EnderPearlFamilyBlueprint.class),
    ENCHANTED_ENDER_PEARL("Enchanted Ender Pearl", Material.ENDER_PEARL, ItemRarity.EPIC, true, EnderPearlFamilyBlueprint.class),

    // SHULKER
    PREMIUM_SHULKER_SHELL("Premium Shulker Shell", Material.SHULKER_SHELL, ItemRarity.RARE, true, ShulkerFamilyBlueprint.class),
    ENCHANTED_SHULKER_SHELL("Enchanted Shulker Shell", Material.SHULKER_SHELL, ItemRarity.EPIC, true, ShulkerFamilyBlueprint.class),

    // PASSIVE MOBS
    PREMIUM_PORKCHOP("Premium Porkchop", Material.COOKED_PORKCHOP, ItemRarity.UNCOMMON, true, PorkchopFamilyBlueprint.class),
    ENCHANTED_PORKCHOP("Enchanted Porkchop", Material.COOKED_PORKCHOP, ItemRarity.RARE, true, PorkchopFamilyBlueprint.class),

    PREMIUM_STEAK("Premium Steak", Material.COOKED_BEEF, ItemRarity.UNCOMMON, true, SteakFamilyBlueprint.class),
    ENCHANTED_STEAK("Enchanted Steak", Material.COOKED_BEEF, ItemRarity.RARE, true, SteakFamilyBlueprint.class),

    PREMIUM_LEATHER("Premium Leather", Material.LEATHER, ItemRarity.UNCOMMON, true, LeatherFamilyBlueprint.class),
    ENCHANTED_LEATHER("Enchanted Leather", Material.LEATHER, ItemRarity.RARE, true, LeatherFamilyBlueprint.class),

    PREMIUM_RABBIT_HIDE("Premium Rabbit Hide", Material.RABBIT_HIDE, ItemRarity.UNCOMMON, true, RabbitHideFamilyBlueprint.class),
    ENCHANTED_RABBIT_HIDE("Enchanted Rabbit Hide", Material.RABBIT_HIDE, ItemRarity.RARE, true, RabbitHideFamilyBlueprint.class),

    PREMIUM_MUTTON("Premium Mutton", Material.COOKED_MUTTON, ItemRarity.UNCOMMON, true, MuttonFamilyBlueprint.class),
    ENCHANTED_MUTTON("Enchanted Mutton", Material.COOKED_MUTTON, ItemRarity.RARE, true, MuttonFamilyBlueprint.class),

    PREMIUM_CHICKEN("Premium Chicken", Material.COOKED_CHICKEN, ItemRarity.UNCOMMON, true, ChickenFamilyBlueprint.class),
    ENCHANTED_CHICKEN("Enchanted Chicken", Material.COOKED_CHICKEN, ItemRarity.RARE, true, ChickenFamilyBlueprint.class),

    PREMIUM_FEATHER("Premium Feather", Material.FEATHER, ItemRarity.UNCOMMON, true, FeatherFamilyBlueprint.class),
    ENCHANTED_FEATHER("Enchanted Feather", Material.FEATHER, ItemRarity.RARE, true, FeatherFamilyBlueprint.class),

    // SINGULARITY SET
    SINGULARITY_HELMET(    "Singularity Helmet",     Material.NETHERITE_HELMET,     ItemRarity.MYTHIC, SingularityHelmet.class),
    SINGULARITY_CHESTPLATE("Singularity Chestplate", Material.NETHERITE_CHESTPLATE, ItemRarity.MYTHIC, SingularityChestplate.class),
    SINGULARITY_LEGGINGS(  "Singularity Leggings",   Material.NETHERITE_LEGGINGS,   ItemRarity.MYTHIC, SingularityLeggings.class),
    SINGULARITY_BOOTS(     "Singularity Boots",      Material.NETHERITE_BOOTS,      ItemRarity.MYTHIC, SingularityLeggings.class),

    // DEBUG
    // Admin armor, makes you invincible basically
    INFINITY_HELMET(    "Infinity Helmet",     Material.NETHERITE_HELMET,     ItemRarity.TRANSCENDENT, InfinityHelmet.class),
    INFINITY_CHESTPLATE("Infinity Chestplate", Material.NETHERITE_CHESTPLATE, ItemRarity.TRANSCENDENT, InfinityChestplate.class),
    INFINITY_LEGGINGS(  "Infinity Leggings",   Material.NETHERITE_LEGGINGS,   ItemRarity.TRANSCENDENT, InfinityLeggings.class),
    INFINITY_BOOTS(     "Infinity Boots",      Material.NETHERITE_BOOTS,      ItemRarity.TRANSCENDENT, InfinityBoots.class),
    INFINITY_SWORD(     "Infinity Sword",      Material.NETHERITE_SWORD,      ItemRarity.TRANSCENDENT, InfinitySword.class),

    SPACE_HELMET("Space Helmet", Material.RED_STAINED_GLASS, ItemRarity.SPECIAL, true, SpaceHelmet.class),
    SPIDER_REPELLENT("Spider Repellent", Material.POTION, ItemRarity.SPECIAL, true),

    ENTITY_ANALYZER("Entity Analyzer", Material.CLOCK, ItemRarity.SPECIAL, true, EntityAnalyzer.class),
    ENTITY_ANALYZER_REPORT("Entity Analyzer Report", Material.PAPER, ItemRarity.SPECIAL, true),
    ;

    public final String name;
    public final Material material;
    public final ItemRarity rarity;
    public final boolean glow;
    public final Class<? extends CustomItemBlueprint> handler;

    public static final int MODEL_DATA_OFFSET = 0x849FFB;

    CustomItemType(String name, Material material) {
        this.name = name;
        this.material = material;
        this.rarity = ItemRarity.COMMON;
        this.glow = false;
        this.handler = EmptyBlueprint.class;
    }

    CustomItemType(String name, Material material, Class<? extends CustomItemBlueprint> handler) {
        this.name = name;
        this.material = material;
        this.rarity = ItemRarity.COMMON;
        this.glow = false;
        this.handler = handler;
    }

    CustomItemType(String name, Material material, ItemRarity rarity) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.glow = false;
        this.handler = EmptyBlueprint.class;
    }

    CustomItemType(String name, Material material, boolean glow) {
        this.name = name;
        this.material = material;
        this.rarity = ItemRarity.COMMON;
        this.glow = glow;
        this.handler = EmptyBlueprint.class;
    }

    CustomItemType(String name, Material material, ItemRarity rarity, boolean glow) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.glow = glow;
        this.handler = EmptyBlueprint.class;
    }

    CustomItemType(String name, Material material, ItemRarity rarity, Class<? extends CustomItemBlueprint> handler) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.glow = false;
        this.handler = handler;
    }

    CustomItemType(String name, Material material, ItemRarity rarity, boolean glow, Class<? extends CustomItemBlueprint> handler) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.glow = glow;
        this.handler = handler;
    }

    public String getKey() {
        return this.toString().toLowerCase();
    }

    public int getModelData() {
        return MODEL_DATA_OFFSET + this.ordinal();
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public boolean isGlowing() {
        return glow;
    }

    public Class<? extends CustomItemBlueprint> getHandler() {
        return handler;
    }
}

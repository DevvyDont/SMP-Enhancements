package xyz.devvydont.smprpg.items;

import org.bukkit.Material;

public enum CustomItemType {

    // COINS
    COPPER_COIN(   "Copper Coin",    Material.FIREWORK_STAR),                                // 1 coin
    SILVER_COIN(   "Silver Coin",    Material.FIREWORK_STAR, ItemRarity.UNCOMMON),           // 10 coins
    GOLD_COIN(     "Gold Coin",      Material.FIREWORK_STAR, ItemRarity.RARE),               // 100 coins
    PLATINUM_COIN( "Platinum Coin",  Material.FIREWORK_STAR, ItemRarity.EPIC),               // 1k coins
    EMERALD_COIN(  "Emerald Coin",   Material.FIREWORK_STAR, ItemRarity.LEGENDARY),          // 10k coins
    AMETHYST_COIN( "Amethyst Coin",  Material.FIREWORK_STAR, ItemRarity.MYTHIC),             // 100k coins
    ENCHANTED_COIN("Enchanted Coin", Material.FIREWORK_STAR, ItemRarity.DIVINE, true), // 1M coins

    // Admin armor, makes you invincible basically
    INFINITY_HELMET(    "Infinity Helmet",     Material.NETHERITE_HELMET,     ItemRarity.TRANSCENDENT),
    INFINITY_CHESTPLATE("Infinity Chestplate", Material.NETHERITE_CHESTPLATE, ItemRarity.TRANSCENDENT),
    INFINITY_LEGGINGS(  "Infinity Leggings",   Material.NETHERITE_LEGGINGS,   ItemRarity.TRANSCENDENT),
    INFINITY_BOOTS(     "Infinity Boots",      Material.NETHERITE_BOOTS,      ItemRarity.TRANSCENDENT),
    INFINITY_SWORD(     "Infinity Sword",      Material.NETHERITE_SWORD,      ItemRarity.TRANSCENDENT),

    // SINGULARITY SET
    SINGULARITY_HELMET(    "Singularity Helmet",     Material.NETHERITE_HELMET,     ItemRarity.MYTHIC),
    SINGULARITY_CHESTPLATE("Singularity Chestplate", Material.NETHERITE_CHESTPLATE, ItemRarity.MYTHIC),
    SINGULARITY_LEGGINGS(  "Singularity Leggings",   Material.NETHERITE_LEGGINGS,   ItemRarity.MYTHIC),
    SINGULARITY_BOOTS(     "Singularity Boots",      Material.NETHERITE_BOOTS,      ItemRarity.MYTHIC),

    // BOWS
    DIAMOND_BOW("Diamond Bow", Material.BOW, ItemRarity.RARE),
    IRON_BOW(   "Iron Bow",    Material.BOW, ItemRarity.RARE),

    // COMPRESSED MINING MATERIALS

    // COAL
    COMPRESSED_COAL("Compressed Coal", Material.COAL, ItemRarity.UNCOMMON, true),
    COMPRESSED_COAL_BLOCK("Compressed Block of Coal", Material.COAL_BLOCK, ItemRarity.RARE, true),
    ENCHANTED_COAL("Enchanted Coal", Material.COAL, ItemRarity.EPIC, true),
    ENCHANTED_COAL_BLOCK("Enchanted Block of Coal", Material.COAL_BLOCK, ItemRarity.LEGENDARY, true),
    COAL_SINGULARITY("Coal Singularity", Material.COAL, ItemRarity.MYTHIC, true),

    // CHARCOAL
    COMPRESSED_CHARCOAL("Compressed Charcoal", Material.CHARCOAL, ItemRarity.UNCOMMON, true),
    ENCHANTED_CHARCOAL("Enchanted Charcoal", Material.CHARCOAL, ItemRarity.RARE, true),

    // FLINT
    COMPRESSED_FLINT("Compressed Flint", Material.FLINT, ItemRarity.UNCOMMON, true),
    ENCHANTED_FLINT("Enchanted Flint", Material.FLINT, ItemRarity.RARE, true),

    // COPPER
    COMPRESSED_COPPER("Compressed Copper", Material.COPPER_INGOT, ItemRarity.UNCOMMON, true),
    COMPRESSED_COPPER_BLOCK("Compressed Block of Copper", Material.COPPER_BLOCK, ItemRarity.RARE, true),
    ENCHANTED_COPPER("Enchanted Copper", Material.COPPER_INGOT, ItemRarity.EPIC, true),
    ENCHANTED_COPPER_BLOCK("Enchanted Block of Copper", Material.COPPER_BLOCK, ItemRarity.LEGENDARY, true),
    COPPER_SINGULARITY("Copper Singularity", Material.COPPER_INGOT, ItemRarity.MYTHIC, true),

    // IRON
    COMPRESSED_IRON("Compressed Iron", Material.IRON_INGOT, ItemRarity.UNCOMMON, true),
    COMPRESSED_IRON_BLOCK("Compressed Block of Iron", Material.IRON_BLOCK, ItemRarity.RARE, true),
    ENCHANTED_IRON("Enchanted Iron", Material.IRON_INGOT, ItemRarity.EPIC, true),
    ENCHANTED_IRON_BLOCK("Enchanted Block of Iron", Material.IRON_BLOCK, ItemRarity.LEGENDARY, true),
    IRON_SINGULARITY("Iron Singularity", Material.IRON_INGOT, ItemRarity.MYTHIC, true),

    // LAPIS
    COMPRESSED_LAPIS("Compressed Lapis Lazuli", Material.LAPIS_LAZULI, ItemRarity.UNCOMMON, true),
    COMPRESSED_LAPIS_BLOCK("Compressed Block of Lapis Lazuli", Material.LAPIS_BLOCK, ItemRarity.RARE, true),
    ENCHANTED_LAPIS("Enchanted Lapis Lazuli", Material.LAPIS_LAZULI, ItemRarity.EPIC, true),
    ENCHANTED_LAPIS_BLOCK("Enchanted Block of Lapis Lazuli", Material.LAPIS_BLOCK, ItemRarity.LEGENDARY, true),
    LAPIS_SINGULARITY("Lapis Lazuli Singularity", Material.LAPIS_LAZULI, ItemRarity.MYTHIC, true),

    // REDSTONE
    COMPRESSED_REDSTONE("Compressed Redstone", Material.REDSTONE, ItemRarity.UNCOMMON, true),
    COMPRESSED_REDSTONE_BLOCK("Compressed Block of Redstone", Material.REDSTONE_BLOCK, ItemRarity.RARE, true),
    ENCHANTED_REDSTONE("Enchanted Redstone", Material.REDSTONE, ItemRarity.EPIC, true),
    ENCHANTED_REDSTONE_BLOCK("Enchanted Block of Redstone", Material.REDSTONE_BLOCK, ItemRarity.LEGENDARY, true),
    REDSTONE_SINGULARITY("Redstone Singularity", Material.REDSTONE, ItemRarity.MYTHIC, true),

    // GLOWSTONE
    COMPRESSED_GLOWSTONE("Compressed Glowstone", Material.GLOWSTONE_DUST, ItemRarity.UNCOMMON, true),
    COMPRESSED_GLOWSTONE_BLOCK("Compressed Block of Glowstone", Material.GLOWSTONE, ItemRarity.RARE, true),
    ENCHANTED_GLOWSTONE("Enchanted Glowstone", Material.GLOWSTONE_DUST, ItemRarity.EPIC, true),
    ENCHANTED_GLOWSTONE_BLOCK("Enchanted Block of Glowstone", Material.GLOWSTONE, ItemRarity.LEGENDARY, true),
    GLOWSTONE_SINGULARITY("Glowstone Singularity", Material.GLOWSTONE_DUST, ItemRarity.MYTHIC, true),

    // AMETHYST
    COMPRESSED_AMETHYST("Compressed Amethyst", Material.AMETHYST_SHARD, ItemRarity.UNCOMMON, true),
    COMPRESSED_AMETHYST_BLOCK("Compressed Block of Amethyst", Material.AMETHYST_BLOCK, ItemRarity.RARE, true),
    ENCHANTED_AMETHYST("Enchanted Amethyst", Material.AMETHYST_SHARD, ItemRarity.EPIC, true),
    ENCHANTED_AMETHYST_BLOCK("Enchanted Block of Amethyst", Material.AMETHYST_BLOCK, ItemRarity.LEGENDARY, true),
    AMETHYST_SINGULARITY("Amethyst Singularity", Material.AMETHYST_SHARD, ItemRarity.MYTHIC, true),

    // GOLD
    COMPRESSED_GOLD("Compressed Gold", Material.GOLD_INGOT, ItemRarity.UNCOMMON, true),
    COMPRESSED_GOLD_BLOCK("Compressed Block of Gold", Material.GOLD_BLOCK, ItemRarity.RARE, true),
    ENCHANTED_GOLD("Enchanted Gold", Material.GOLD_INGOT, ItemRarity.EPIC, true),
    ENCHANTED_GOLD_BLOCK("Enchanted Block of Gold", Material.GOLD_BLOCK, ItemRarity.LEGENDARY, true),
    GOLD_SINGULARITY("Gold Singularity", Material.GOLD_INGOT, ItemRarity.MYTHIC, true),

    // DIAMOND
    COMPRESSED_DIAMOND("Compressed Diamond", Material.DIAMOND, ItemRarity.RARE, true),
    COMPRESSED_DIAMOND_BLOCK("Compressed Block of Diamond", Material.DIAMOND_BLOCK, ItemRarity.EPIC, true),
    ENCHANTED_DIAMOND("Enchanted Diamond", Material.DIAMOND, ItemRarity.LEGENDARY, true),
    ENCHANTED_DIAMOND_BLOCK("Enchanted Block of Diamond", Material.DIAMOND_BLOCK, ItemRarity.MYTHIC, true),
    DIAMOND_SINGULARITY("Diamond Singularity", Material.DIAMOND, ItemRarity.DIVINE, true),

    // EMERALD
    COMPRESSED_EMERALD("Compressed Emerald", Material.EMERALD, ItemRarity.RARE, true),
    COMPRESSED_EMERALD_BLOCK("Compressed Block of Emerald", Material.EMERALD_BLOCK, ItemRarity.EPIC, true),
    ENCHANTED_EMERALD("Enchanted Emerald", Material.EMERALD, ItemRarity.LEGENDARY, true),
    ENCHANTED_EMERALD_BLOCK("Enchanted Block of Emerald", Material.EMERALD_BLOCK, ItemRarity.MYTHIC, true),
    EMERALD_SINGULARITY("Emerald Singularity", Material.EMERALD, ItemRarity.DIVINE, true),

    // QUARTZ
    COMPRESSED_QUARTZ("Compressed Quartz", Material.QUARTZ, ItemRarity.UNCOMMON, true),
    COMPRESSED_QUARTZ_BLOCK("Compressed Block of Quartz", Material.QUARTZ_BLOCK, ItemRarity.RARE, true),
    ENCHANTED_QUARTZ("Enchanted Quartz", Material.QUARTZ, ItemRarity.EPIC, true),
    ENCHANTED_QUARTZ_BLOCK("Enchanted Block of Quartz", Material.QUARTZ_BLOCK, ItemRarity.LEGENDARY, true),
    QUARTZ_SINGULARITY("Quartz Singularity", Material.QUARTZ, ItemRarity.MYTHIC, true),

    // NETHERITE
    COMPRESSED_NETHERITE("Compressed Netherite", Material.NETHERITE_INGOT, ItemRarity.EPIC, true),
    COMPRESSED_NETHERITE_BLOCK("Compressed Block of Netherite", Material.NETHERITE_BLOCK, ItemRarity.LEGENDARY, true),
    ENCHANTED_NETHERITE("Enchanted Netherite", Material.NETHERITE_INGOT, ItemRarity.MYTHIC, true),
    ENCHANTED_NETHERITE_BLOCK("Enchanted Block of Netherite", Material.NETHERITE_BLOCK, ItemRarity.DIVINE, true),
    NETHERITE_SINGULARITY("Netherite Singularity", Material.NETHERITE_INGOT, ItemRarity.TRANSCENDENT, true),

    // MOB DROPS (OVERWORLD)

    // ROTTEN FLESH
    PREMIUM_FLESH("Premium Flesh", Material.ROTTEN_FLESH, ItemRarity.UNCOMMON, true),
    ENCHANTED_FLESH("Premium Flesh", Material.ROTTEN_FLESH, ItemRarity.RARE, true),

    // BONE
    PREMIUM_BONE("Premium Bone", Material.BONE, ItemRarity.UNCOMMON, true),
    ENCHANTED_BONE("Enchanted Bone", Material.BONE, ItemRarity.RARE, true),

    // STRING
    PREMIUM_STRING("Premium String", Material.STRING, ItemRarity.UNCOMMON, true),
    ENCHANTED_STRING("Enchanted String", Material.STRING, ItemRarity.RARE, true),

    // SPIDER EYE
    PREMIUM_SPIDER_EYE("Premium Spider Eye", Material.SPIDER_EYE, ItemRarity.UNCOMMON, true),
    ENCHANTED_SPIDER_EYE("Enchanted Spider Eye", Material.SPIDER_EYE, ItemRarity.RARE, true),

    // SLIME BALL
    PREMIUM_SLIME("Premium Slime Ball", Material.SLIME_BALL, ItemRarity.UNCOMMON, true),
    ENCHANTED_SLIME("Enchanted Slime Ball", Material.SLIME_BALL, ItemRarity.RARE, true),

    // GUNPOWDER
    PREMIUM_GUNPOWDER("Premium Gunpowder", Material.GUNPOWDER, ItemRarity.UNCOMMON, true),
    ENCHANTED_GUNPOWDER("Enchanted Gunpowder", Material.GUNPOWDER, ItemRarity.RARE, true),

    // PRISMARINE SHARD
    PREMIUM_PRISMARINE_SHARD("Premium Prismarine Shard", Material.PRISMARINE_SHARD, ItemRarity.UNCOMMON, true),
    ENCHANTED_PRISMARINE_SHARD("Enchanted Prismarine Shard", Material.PRISMARINE_SHARD, ItemRarity.RARE, true),

    // PRISMARINE CRYSTAL
    PREMIUM_PRISMARINE_CRYSTAL("Premium Prismarine Crystals", Material.PRISMARINE_CRYSTALS, ItemRarity.UNCOMMON, true),
    ENCHANTED_PRISMARINE_CRYSTAL("Enchanted Prismarine Crystals", Material.PRISMARINE_CRYSTALS, ItemRarity.RARE, true),

    // NAUTILUS SHELL
    PREMIUM_NAUTILUS_SHELL("Premium Nautilus Shell", Material.NAUTILUS_SHELL, ItemRarity.RARE, true),
    ENCHANTED_NAUTILUS_SHELL("Enchanted Nautilus Shell", Material.NAUTILUS_SHELL, ItemRarity.EPIC, true),

    // ECHO SHARD
    PREMIUM_ECHO_SHARD("Premium Echo Shard", Material.ECHO_SHARD, ItemRarity.EPIC, true),
    ENCHANTED_ECHO_SHARD("Enchanted Echo Shard", Material.ECHO_SHARD, ItemRarity.LEGENDARY, true),

    // NETHER MOBS

    // BLAZE
    PREMIUM_BLAZE_ROD("Premium Blaze Rod", Material.BLAZE_ROD, ItemRarity.UNCOMMON, true),
    ENCHANTED_BLAZE_ROD("Enchanted Blaze Rod", Material.BLAZE_ROD, ItemRarity.RARE, true),

    // NETHER STAR
    PREMIUM_NETHER_STAR("Premium Nether Star", Material.NETHER_STAR, ItemRarity.LEGENDARY, true),
    ENCHANTED_NETHER_STAR("Enchanted Nether Star", Material.NETHER_STAR, ItemRarity.MYTHIC, true),

    // MAGMA
    PREMIUM_MAGMA_CREAM("Premium Magma Cream", Material.MAGMA_CREAM, ItemRarity.UNCOMMON, true),
    ENCHANTED_MAGMA_CREAM("Enchanted Magma Cream", Material.MAGMA_CREAM, ItemRarity.RARE, true),

    // END MOBS

    // ENDER PEARL
    PREMIUM_ENDER_PEARL("Premium Ender Pearl", Material.ENDER_PEARL, ItemRarity.RARE, true),
    ENCHANTED_ENDER_PEARL("Enchanted Ender Pearl", Material.ENDER_PEARL, ItemRarity.EPIC, true),

    // SHULKER
    PREMIUM_SHULKER_SHELL("Premium Shulker Shell", Material.SHULKER_SHELL, ItemRarity.RARE, true),
    ENCHANTED_SHULKER_SHELL("Enchanted Shulker Shell", Material.SHULKER_SHELL, ItemRarity.EPIC, true),

    // PASSIVE MOBS
    PREMIUM_PORKCHOP("Premium Porkchop", Material.COOKED_PORKCHOP, ItemRarity.UNCOMMON, true),
    ENCHANTED_PORKCHOP("Enchanted Porkchop", Material.COOKED_PORKCHOP, ItemRarity.RARE, true),

    PREMIUM_STEAK("Premium Steak", Material.COOKED_BEEF, ItemRarity.UNCOMMON, true),
    ENCHANTED_STEAK("Enchanted Steak", Material.COOKED_BEEF, ItemRarity.RARE, true),

    PREMIUM_LEATHER("Premium Leather", Material.LEATHER, ItemRarity.UNCOMMON, true),
    ENCHANTED_LEATHER("Enchanted Leather", Material.LEATHER, ItemRarity.RARE, true),

    PREMIUM_RABBIT_HIDE("Premium Rabbit Hide", Material.RABBIT_HIDE, ItemRarity.UNCOMMON, true),
    ENCHANTED_RABBIT_HIDE("Enchanted Rabbit Hide", Material.RABBIT_HIDE, ItemRarity.RARE, true),

    PREMIUM_MUTTON("Premium Mutton", Material.COOKED_MUTTON, ItemRarity.UNCOMMON, true),
    ENCHANTED_MUTTON("Enchanted Mutton", Material.COOKED_MUTTON, ItemRarity.RARE, true),

    PREMIUM_CHICKEN("Premium Chicken", Material.COOKED_CHICKEN, ItemRarity.UNCOMMON, true),
    ENCHANTED_CHICKEN("Enchanted Chicken", Material.COOKED_CHICKEN, ItemRarity.RARE, true),

    PREMIUM_FEATHER("Premium Feather", Material.FEATHER, ItemRarity.UNCOMMON, true),
    ENCHANTED_FEATHER("Enchanted Feather", Material.FEATHER, ItemRarity.RARE, true),

    SPACE_HELMET("Space Helmet", Material.RED_STAINED_GLASS, ItemRarity.SPECIAL, true),
    SPIDER_REPELLENT("Spider Repellent", Material.POTION, ItemRarity.SPECIAL, true),

    // DEBUG
    ENTITY_ANALYZER("Entity Analyzer", Material.CLOCK, ItemRarity.SPECIAL, true)
    ;

    public final String name;
    public final Material material;
    public final ItemRarity rarity;
    public final boolean glow;

    public static final int MODEL_DATA_OFFSET = 0x849FFB;

    CustomItemType(String name, Material material) {
        this.name = name;
        this.material = material;
        this.rarity = ItemRarity.COMMON;
        this.glow = false;
    }

    CustomItemType(String name, Material material, ItemRarity rarity) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.glow = false;
    }

    CustomItemType(String name, Material material, boolean glow) {
        this.name = name;
        this.material = material;
        this.rarity = ItemRarity.COMMON;
        this.glow = glow;
    }

    CustomItemType(String name, Material material, ItemRarity rarity, boolean glow) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.glow = glow;
    }

    public String getKey() {
        return this.toString().toLowerCase();
    }

    public int getModelData() {
        return MODEL_DATA_OFFSET + this.ordinal();
    }



}

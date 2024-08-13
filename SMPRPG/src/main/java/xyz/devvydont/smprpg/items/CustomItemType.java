package xyz.devvydont.smprpg.items;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.base.CustomHeadBlueprint;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.blueprints.debug.EntityAnalyzer;
import xyz.devvydont.smprpg.items.blueprints.debug.SpawnerEditorBlueprint;
import xyz.devvydont.smprpg.items.blueprints.debug.SpiderRepellentBlueprint;
import xyz.devvydont.smprpg.items.blueprints.economy.CustomItemCoin;
import xyz.devvydont.smprpg.items.blueprints.equipment.GrapplingHook;
import xyz.devvydont.smprpg.items.blueprints.resources.EmptyBlueprint;
import xyz.devvydont.smprpg.items.blueprints.resources.crafting.DraconicCrystal;
import xyz.devvydont.smprpg.items.blueprints.resources.mining.*;
import xyz.devvydont.smprpg.items.blueprints.resources.mob.*;
import xyz.devvydont.smprpg.items.blueprints.sets.amethyst.AmethystBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.amethyst.AmethystChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.amethyst.AmethystHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.amethyst.AmethystLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.araxys.AraxysBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.araxys.AraxysChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.araxys.AraxysHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.araxys.AraxysLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.bedrock.BedrockBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.bedrock.BedrockChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.bedrock.BedrockHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.bedrock.BedrockLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.bone.BoneBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.bone.BoneChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.bone.BoneHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.bone.BoneLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.cobblestone.CobblestoneBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.cobblestone.CobblestoneChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.cobblestone.CobblestoneHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.cobblestone.CobblestoneLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.copper.*;
import xyz.devvydont.smprpg.items.blueprints.sets.diamond.DiamondBow;
import xyz.devvydont.smprpg.items.blueprints.sets.elderflame.ElderflameBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.elderflame.ElderflameChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.elderflame.ElderflameHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.elderflame.ElderflameLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.emerald.EmeraldBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.emerald.EmeraldChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.emerald.EmeraldHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.emerald.EmeraldLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.forsaken.ForsakenBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.forsaken.ForsakenChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.forsaken.ForsakenHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.forsaken.ForsakenLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.imperium.ImperiumBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.imperium.ImperiumChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.imperium.ImperiumHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.imperium.ImperiumLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.inferno.InfernoBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.inferno.InfernoChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.inferno.InfernoHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.inferno.InfernoLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.infinity.*;
import xyz.devvydont.smprpg.items.blueprints.sets.iron.IronBow;
import xyz.devvydont.smprpg.items.blueprints.sets.mystbloom.MystbloomBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.mystbloom.MystbloomChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.mystbloom.MystbloomHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.mystbloom.MystbloomLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.mystic.LuxeBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.mystic.LuxeChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.mystic.LuxeHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.mystic.LuxeLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.neofrontier.NeoFrontierBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.neofrontier.NeoFrontierChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.neofrontier.NeoFrontierHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.neofrontier.NeoFrontierLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.neptune.*;
import xyz.devvydont.smprpg.items.blueprints.sets.netherite.NetheriteBow;
import xyz.devvydont.smprpg.items.blueprints.sets.prelude.PreludeBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.prelude.PreludeChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.prelude.PreludeHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.prelude.PreludeLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.quartz.QuartzBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.quartz.QuartzChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.quartz.QuartzHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.quartz.QuartzLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.radiant.RadiantBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.radiant.RadiantChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.radiant.RadiantHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.radiant.RadiantLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.reaver.ReaverBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.reaver.ReaverChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.reaver.ReaverHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.reaver.ReaverLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.redstone.RedstoneBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.redstone.RedstoneChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.redstone.RedstoneHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.redstone.RedstoneLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.sakura.SakuraBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.sakura.SakuraChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.sakura.SakuraHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.sakura.SakuraLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.singularity.SingularityBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.singularity.SingularityChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.singularity.SingularityHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.singularity.SingularityLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.slimy.SlimyBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.slimy.SlimyChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.slimy.SlimyHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.slimy.SlimyLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.smite.SmiteBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.smite.SmiteChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.smite.SmiteHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.smite.SmiteLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.snowfall.SnowfallBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.snowfall.SnowfallChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.snowfall.SnowfallHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.snowfall.SnowfallLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.special.SpaceHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.undead.UndeadBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.undead.UndeadChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.undead.UndeadHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.undead.UndeadLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.unstable.UnstableBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.unstable.UnstableChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.unstable.UnstableHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.unstable.UnstableLeggings;
import xyz.devvydont.smprpg.items.blueprints.sets.valiant.ValiantBoots;
import xyz.devvydont.smprpg.items.blueprints.sets.valiant.ValiantChestplate;
import xyz.devvydont.smprpg.items.blueprints.sets.valiant.ValiantHelmet;
import xyz.devvydont.smprpg.items.blueprints.sets.valiant.ValiantLeggings;

public enum CustomItemType {

    // COINS
    COPPER_COIN(   "Copper Coin",    Material.FIREWORK_STAR, CustomItemCoin.class),                                // 1 coin
    SILVER_COIN(   "Silver Coin",    Material.FIREWORK_STAR, ItemRarity.UNCOMMON,     CustomItemCoin.class),       // 10 coins
    GOLD_COIN(     "Gold Coin",      Material.FIREWORK_STAR, ItemRarity.RARE,         CustomItemCoin.class),       // 100 coins
    PLATINUM_COIN( "Platinum Coin",  Material.FIREWORK_STAR, ItemRarity.EPIC,         CustomItemCoin.class),       // 1k coins
    EMERALD_COIN(  "Emerald Coin",   Material.FIREWORK_STAR, ItemRarity.LEGENDARY,    CustomItemCoin.class),       // 10k coins
    AMETHYST_COIN( "Amethyst Coin",  Material.FIREWORK_STAR, ItemRarity.MYTHIC,       CustomItemCoin.class),       // 100k coins
    ENCHANTED_COIN("Enchanted Coin", Material.FIREWORK_STAR, ItemRarity.DIVINE, true, CustomItemCoin.class), // 1M coins

    // COBBLESTONE SET
    COBBLESTONE_HELMET("Cobblestone Helmet",         Material.COBBLESTONE,        ItemRarity.UNCOMMON, CobblestoneHelmet.class),
    COBBLESTONE_CHESTPLATE("Cobblestone Chestplate", Material.LEATHER_CHESTPLATE, ItemRarity.UNCOMMON, CobblestoneChestplate.class),
    COBBLESTONE_LEGGINGS("Cobblestone Leggings",     Material.LEATHER_LEGGINGS,   ItemRarity.UNCOMMON, CobblestoneLeggings.class),
    COBBLESTONE_BOOTS("Cobblestone Boots",           Material.LEATHER_BOOTS,      ItemRarity.UNCOMMON, CobblestoneBoots.class),

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

    // UNDEAD SET
    UNDEAD_HELMET(    "Undead Helmet",         Material.LEATHER_HELMET,     UndeadHelmet.class),
    UNDEAD_CHESTPLATE("Undead Chestplate",     Material.LEATHER_CHESTPLATE, UndeadChestplate.class),
    UNDEAD_LEGGINGS(  "Undead Leggings",       Material.LEATHER_LEGGINGS,   UndeadLeggings.class),
    UNDEAD_BOOTS(     "Undead Boots",          Material.LEATHER_BOOTS,      UndeadBoots.class),

    // BONE SET
    BONE_HELMET(    "Bone Helmet",         Material.CHAINMAIL_HELMET,   BoneHelmet.class),
    BONE_CHESTPLATE("Bone Chestplate",     Material.LEATHER_CHESTPLATE, BoneChestplate.class),
    BONE_LEGGINGS(  "Bone Leggings",       Material.CHAINMAIL_LEGGINGS, BoneLeggings.class),
    BONE_BOOTS(     "Bone Boots",          Material.LEATHER_BOOTS,      BoneBoots.class),

    // SMITE SET
    SMITE_HELMET(    "Smite Helmet",         Material.CHAINMAIL_HELMET,     ItemRarity.RARE, SmiteHelmet.class),
    SMITE_CHESTPLATE("Smite Chestplate",     Material.CHAINMAIL_CHESTPLATE, ItemRarity.RARE, SmiteChestplate.class),
    SMITE_LEGGINGS(  "Smite Leggings",       Material.CHAINMAIL_LEGGINGS,   ItemRarity.RARE, SmiteLeggings.class),
    SMITE_BOOTS(     "Smite Boots",          Material.CHAINMAIL_BOOTS,      ItemRarity.RARE, SmiteBoots.class),

    // SAKURA SET
    SAKURA_HELMET("Sakura Helmet",         Material.CHERRY_LEAVES,      SakuraHelmet.class),
    SAKURA_CHESTPLATE("Sakura Chestplate", Material.LEATHER_CHESTPLATE, SakuraChestplate.class),
    SAKURA_LEGGINGS("Sakura Leggings",     Material.IRON_LEGGINGS,      SakuraLeggings.class),
    SAKURA_BOOTS("Sakura Boots",           Material.LEATHER_BOOTS,      SakuraBoots.class),

    // MYSTBLOOM SET
    MYSTBLOOM_HELMET("Mystbloom Helmet",         Material.LEATHER_HELMET,     MystbloomHelmet.class),
    MYSTBLOOM_CHESTPLATE("Mystbloom Chestplate", Material.LEATHER_CHESTPLATE, MystbloomChestplate.class),
    MYSTBLOOM_LEGGINGS("Mystbloom Leggings",     Material.LEATHER_LEGGINGS,   MystbloomLeggings.class),
    MYSTBLOOM_BOOTS("Mystbloom Boots",           Material.LEATHER_BOOTS,      MystbloomBoots.class),

    // NEO_FRONTIER SET
    NEO_FRONTIER_HELMET("Neo Frontier Helmet",         Material.IRON_HELMET,        NeoFrontierHelmet.class),
    NEO_FRONTIER_CHESTPLATE("Neo Frontier Chestplate", Material.LEATHER_CHESTPLATE, NeoFrontierChestplate.class),
    NEO_FRONTIER_LEGGINGS("Neo Frontier Leggings",     Material.IRON_LEGGINGS,      NeoFrontierLeggings.class),
    NEO_FRONTIER_BOOTS("Neo Frontier Boots",           Material.LEATHER_BOOTS,      NeoFrontierBoots.class),

    // SLIMY SET
    SLIMY_HELMET("Slimy Helmet",         Material.SLIME_BLOCK   ,     ItemRarity.RARE, SlimyHelmet.class),
    SLIMY_CHESTPLATE("Slimy Chestplate", Material.LEATHER_CHESTPLATE, ItemRarity.RARE, SlimyChestplate.class),
    SLIMY_LEGGINGS("Slimy Leggings",     Material.DIAMOND_LEGGINGS,   ItemRarity.RARE, SlimyLeggings.class),
    SLIMY_BOOTS("Slimy Boots",           Material.LEATHER_BOOTS,      ItemRarity.RARE, SlimyBoots.class),

    // LUXE
    LUXE_HELMET("Luxe Helmet",         Material.IRON_HELMET   ,     ItemRarity.RARE, LuxeHelmet.class),
    LUXE_CHESTPLATE("Luxe Chestplate", Material.LEATHER_CHESTPLATE, ItemRarity.RARE, LuxeChestplate.class),
    LUXE_LEGGINGS("Luxe Leggings",     Material.IRON_LEGGINGS,      ItemRarity.RARE, LuxeLeggings.class),
    LUXE_BOOTS("Luxe Boots",           Material.LEATHER_BOOTS,      ItemRarity.RARE, LuxeBoots.class),

    // AMETHYST
    AMETHYST_HELMET(    "Amethyst Helmet",     Material.IRON_HELMET   ,  ItemRarity.RARE, AmethystHelmet.class),
    AMETHYST_CHESTPLATE("Amethyst Chestplate", Material.IRON_CHESTPLATE, ItemRarity.RARE, AmethystChestplate.class),
    AMETHYST_LEGGINGS(  "Amethyst Leggings",   Material.IRON_LEGGINGS,   ItemRarity.RARE, AmethystLeggings.class),
    AMETHYST_BOOTS(     "Amethyst Boots",      Material.IRON_BOOTS,      ItemRarity.RARE, AmethystBoots.class),

    // EMERALD SET
    EMERALD_HELMET(    "Emerald Helmet",         Material.DIAMOND_HELMET,     EmeraldHelmet.class),
    EMERALD_CHESTPLATE("Emerald Chestplate",     Material.DIAMOND_CHESTPLATE, EmeraldChestplate.class),
    EMERALD_LEGGINGS(  "Emerald Leggings",       Material.DIAMOND_LEGGINGS,   EmeraldLeggings.class),
    EMERALD_BOOTS(     "Emerald Boots",          Material.DIAMOND_BOOTS,      EmeraldBoots.class),

    // REDSTONE
    REDSTONE_HELMET(    "Redstone Helmet",     Material.TARGET,             ItemRarity.RARE, RedstoneHelmet.class),
    REDSTONE_CHESTPLATE("Redstone Chestplate", Material.LEATHER_CHESTPLATE, ItemRarity.RARE, RedstoneChestplate.class),
    REDSTONE_LEGGINGS(  "Redstone Leggings",   Material.LEATHER_LEGGINGS,   ItemRarity.RARE, RedstoneLeggings.class),
    REDSTONE_BOOTS(     "Redstone Boots",      Material.LEATHER_BOOTS,      ItemRarity.RARE, RedstoneBoots.class),

    // BEDROCK
    BEDROCK_HELMET("Bedrock Helmet",         Material.BEDROCK,            ItemRarity.EPIC, BedrockHelmet.class),
    BEDROCK_CHESTPLATE("Bedrock Chestplate", Material.LEATHER_CHESTPLATE, ItemRarity.EPIC, BedrockChestplate.class),
    BEDROCK_LEGGINGS("Bedrock Leggings",     Material.NETHERITE_LEGGINGS, ItemRarity.EPIC, BedrockLeggings.class),
    BEDROCK_BOOTS("Bedrock Boots",           Material.LEATHER_BOOTS,      ItemRarity.EPIC, BedrockBoots.class),

    // ARAXYS SET
    ARAXYS_HELMET("Araxys Helmet",         Material.SPAWNER,              ItemRarity.EPIC, AraxysHelmet.class),
    ARAXYS_CHESTPLATE("Araxys Chestplate", Material.CHAINMAIL_CHESTPLATE, ItemRarity.EPIC, AraxysChestplate.class),
    ARAXYS_LEGGINGS("Araxys Leggings",     Material.CHAINMAIL_LEGGINGS,   ItemRarity.EPIC, AraxysLeggings.class),
    ARAXYS_BOOTS("Araxys Boots",           Material.CHAINMAIL_BOOTS,      ItemRarity.EPIC, AraxysBoots.class),

    // NEPTUNE SET
    NEPTUNE_HELMET("Neptune Helmet",         Material.ICE,                ItemRarity.LEGENDARY, NeptuneHelmet.class),
    NEPTUNE_CHESTPLATE("Neptune Chestplate", Material.LEATHER_CHESTPLATE, ItemRarity.LEGENDARY, NeptuneChestplate.class),
    NEPTUNE_LEGGINGS("Neptune Leggings",     Material.LEATHER_LEGGINGS,   ItemRarity.LEGENDARY, NeptuneLeggings.class),
    NEPTUNE_BOOTS("Neptune Boots",           Material.LEATHER_BOOTS,      ItemRarity.LEGENDARY, NeptuneBoots.class),
    NEPTUNE_TRIDENT("Neptune's Trident",     Material.TRIDENT,            ItemRarity.LEGENDARY, NeptuneTrident.class),

    // QUARTZ
    QUARTZ_HELMET("Quartz Helmet",         Material.IRON_HELMET,        ItemRarity.RARE, QuartzHelmet.class),
    QUARTZ_CHESTPLATE("Quartz Chestplate", Material.LEATHER_CHESTPLATE, ItemRarity.RARE, QuartzChestplate.class),
    QUARTZ_LEGGINGS("Quartz Leggings",     Material.IRON_LEGGINGS,      ItemRarity.RARE, QuartzLeggings.class),
    QUARTZ_BOOTS("Quartz Boots",           Material.LEATHER_BOOTS,      ItemRarity.RARE, QuartzBoots.class),

    // RADIANT
    RADIANT_HELMET(    "Radiant Helmet",     Material.IRON_HELMET,      ItemRarity.RARE, RadiantHelmet.class),
    RADIANT_CHESTPLATE("Radiant Chestplate", Material.IRON_CHESTPLATE,  ItemRarity.RARE, RadiantChestplate.class),
    RADIANT_LEGGINGS(  "Radiant Leggings",   Material.IRON_LEGGINGS,    ItemRarity.RARE, RadiantLeggings.class),
    RADIANT_BOOTS(     "Radiant Boots",      Material.IRON_BOOTS,       ItemRarity.RARE, RadiantBoots.class),

    // INFERNO
    INFERNO_HELMET("Inferno Helmet",         Material.GOLDEN_HELMET,     ItemRarity.EPIC, InfernoHelmet.class),
    INFERNO_CHESTPLATE("Inferno Chestplate", Material.GOLDEN_CHESTPLATE, ItemRarity.EPIC, InfernoChestplate.class),
    INFERNO_LEGGINGS("Inferno Leggings",     Material.GOLDEN_LEGGINGS,   ItemRarity.EPIC, InfernoLeggings.class),
    INFERNO_BOOTS("Inferno Boots",           Material.GOLDEN_BOOTS,      ItemRarity.EPIC, InfernoBoots.class),

    // REAVER
    REAVER_HELMET("Reaver Helmet",         Material.BLACK_STAINED_GLASS, ItemRarity.EPIC, ReaverHelmet.class),
    REAVER_CHESTPLATE("Reaver Chestplate", Material.LEATHER_CHESTPLATE,  ItemRarity.EPIC, ReaverChestplate.class),
    REAVER_LEGGINGS("Reaver Leggings",     Material.NETHERITE_LEGGINGS,  ItemRarity.EPIC, ReaverLeggings.class),
    REAVER_BOOTS("Reaver Boots",           Material.LEATHER_BOOTS,       ItemRarity.EPIC, ReaverBoots.class),

    // FORSAKEN
    FORSAKEN_HELMET(    "Forsaken Helmet",     Material.NETHERITE_HELMET,    ItemRarity.LEGENDARY, ForsakenHelmet.class),
    FORSAKEN_CHESTPLATE("Forsaken Chestplate", Material.LEATHER_CHESTPLATE,  ItemRarity.LEGENDARY, ForsakenChestplate.class),
    FORSAKEN_LEGGINGS(  "Forsaken Leggings",   Material.NETHERITE_LEGGINGS,  ItemRarity.LEGENDARY, ForsakenLeggings.class),
    FORSAKEN_BOOTS(     "Forsaken Boots",      Material.LEATHER_BOOTS,       ItemRarity.LEGENDARY, ForsakenBoots.class),

    // UNSTABLE
    UNSTABLE_HELMET(    "Unstable Helmet",     Material.NETHERITE_HELMET,    ItemRarity.EPIC, UnstableHelmet.class),
    UNSTABLE_CHESTPLATE("Unstable Chestplate", Material.LEATHER_CHESTPLATE,  ItemRarity.EPIC, UnstableChestplate.class),
    UNSTABLE_LEGGINGS(  "Unstable Leggings",   Material.NETHERITE_LEGGINGS,  ItemRarity.EPIC, UnstableLeggings.class),
    UNSTABLE_BOOTS(     "Unstable Boots",      Material.LEATHER_BOOTS,       ItemRarity.EPIC, UnstableBoots.class),

    // IMPERIUM
    IMPERIUM_HELMET(    "Imperium Helmet",     Material.IRON_HELMET,      ItemRarity.EPIC, ImperiumHelmet.class),
    IMPERIUM_CHESTPLATE("Imperium Chestplate", Material.IRON_CHESTPLATE,  ItemRarity.EPIC, ImperiumChestplate.class),
    IMPERIUM_LEGGINGS(  "Imperium Leggings",   Material.IRON_LEGGINGS,    ItemRarity.EPIC, ImperiumLeggings.class),
    IMPERIUM_BOOTS(     "Imperium Boots",      Material.IRON_BOOTS,       ItemRarity.EPIC, ImperiumBoots.class),

    // ELDERFLAME
    ELDERFLAME_HELMET(    "Elderflame Helmet",     Material.NETHERITE_HELMET,    ItemRarity.LEGENDARY, ElderflameHelmet.class),
    ELDERFLAME_CHESTPLATE("Elderflame Wings",      Material.ELYTRA,              ItemRarity.LEGENDARY, ElderflameChestplate.class),
    ELDERFLAME_LEGGINGS(  "Elderflame Leggings",   Material.NETHERITE_LEGGINGS,  ItemRarity.LEGENDARY, ElderflameLeggings.class),
    ELDERFLAME_BOOTS(     "Elderflame Boots",      Material.LEATHER_BOOTS,       ItemRarity.LEGENDARY, ElderflameBoots.class),

    // SNOWFALL SET
    SNOWFALL_HELMET(    "Snowfall Helmet",         Material.NETHERITE_HELMET,     ItemRarity.EPIC,  SnowfallHelmet.class),
    SNOWFALL_CHESTPLATE("Snowfall Chestplate",     Material.NETHERITE_CHESTPLATE, ItemRarity.EPIC,  SnowfallChestplate.class),
    SNOWFALL_LEGGINGS(  "Snowfall Leggings",       Material.NETHERITE_LEGGINGS,   ItemRarity.EPIC,  SnowfallLeggings.class),
    SNOWFALL_BOOTS(     "Snowfall Boots",          Material.NETHERITE_BOOTS,      ItemRarity.EPIC,  SnowfallBoots.class),

    // VALIANT SET
    VALIANT_HELMET(    "Valiant Helmet",         Material.NETHERITE_HELMET,     ItemRarity.EPIC,  ValiantHelmet.class),
    VALIANT_CHESTPLATE("Valiant Chestplate",     Material.NETHERITE_CHESTPLATE, ItemRarity.EPIC,  ValiantChestplate.class),
    VALIANT_LEGGINGS(  "Valiant Leggings",       Material.NETHERITE_LEGGINGS,   ItemRarity.EPIC,  ValiantLeggings.class),
    VALIANT_BOOTS(     "Valiant Boots",          Material.NETHERITE_BOOTS,      ItemRarity.EPIC,  ValiantBoots.class),

    // PRELUDE SET
    PRELUDE_HELMET(    "Prelude to Chaos Helmet",         Material.NETHERITE_HELMET,     ItemRarity.LEGENDARY,  PreludeHelmet.class),
    PRELUDE_CHESTPLATE("Prelude to Chaos Chestplate",     Material.NETHERITE_CHESTPLATE, ItemRarity.LEGENDARY,  PreludeChestplate.class),
    PRELUDE_LEGGINGS(  "Prelude to Chaos Leggings",       Material.NETHERITE_LEGGINGS,   ItemRarity.LEGENDARY,  PreludeLeggings.class),
    PRELUDE_BOOTS(     "Prelude to Chaos Boots",          Material.NETHERITE_BOOTS,      ItemRarity.LEGENDARY,  PreludeBoots.class),

    // BOWS
    NETHERITE_BOW("Netherite Bow", Material.BOW, ItemRarity.RARE,      NetheriteBow.class),
    DIAMOND_BOW("Diamond Bow",     Material.BOW, ItemRarity.UNCOMMON,  DiamondBow.class),
    IRON_BOW(   "Iron Bow",        Material.BOW, ItemRarity.COMMON,    IronBow.class),

    // MISC TOOLS
    GRAPPLING_HOOK("Grappling Hook", Material.FISHING_ROD, ItemRarity.RARE, GrapplingHook.class),

    // COMPRESSED MINING MATERIALS

    // Cobblestone
    COMPRESSED_COBBLESTONE("Compressed Cobblestone", Material.COBBLESTONE, ItemRarity.COMMON, true, CobblestoneFamilyBlueprint.class),
    DOUBLE_COMPRESSED_COBBLESTONE("Double Compressed Cobblestone", Material.COBBLESTONE, ItemRarity.UNCOMMON, true, CobblestoneFamilyBlueprint.class),
    ENCHANTED_COBBLESTONE("Enchanted Cobblestone", Material.COBBLESTONE, ItemRarity.UNCOMMON, true, CobblestoneFamilyBlueprint.class),
    COBBLESTONE_SINGULARITY("Cobblestone Singularity", Material.BEDROCK, ItemRarity.RARE, true, CobblestoneFamilyBlueprint.class),

    // Deepslate
    COMPRESSED_DEEPSLATE( "Compressed Deepslate",                Material.COBBLED_DEEPSLATE, ItemRarity.COMMON,  true, DeepslateFamilyBlueprint.class),
    DOUBLE_COMPRESSED_DEEPSLATE( "Double Compressed Deepslate",  Material.COBBLED_DEEPSLATE, ItemRarity.UNCOMMON,      true, DeepslateFamilyBlueprint.class),
    ENCHANTED_DEEPSLATE(  "Enchanted Deepslate",                 Material.COBBLED_DEEPSLATE, ItemRarity.UNCOMMON,      true, DeepslateFamilyBlueprint.class),
    DEEPSLATE_SINGULARITY("Deepslate Singularity",               Material.BEDROCK,           ItemRarity.RARE, true, DeepslateFamilyBlueprint.class),

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

    // DRAGON
    DRAGON_SCALES(   "Dragon Scales",    Material.PHANTOM_MEMBRANE, ItemRarity.RARE, true),
    DRACONIC_CRYSTAL("Draconic Crystal", "c145dd7e0a35db50c9a4bdc465ca1235356fc5dd470737d2a74ea99cc1525bdb", ItemRarity.EPIC, DraconicCrystal.class),

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
    SINGULARITY_HELMET(    "Singularity Helmet",     Material.CRYING_OBSIDIAN,      ItemRarity.MYTHIC, SingularityHelmet.class),
    SINGULARITY_CHESTPLATE("Singularity Chestplate", Material.NETHERITE_CHESTPLATE, ItemRarity.MYTHIC, SingularityChestplate.class),
    SINGULARITY_LEGGINGS(  "Singularity Leggings",   Material.NETHERITE_LEGGINGS,   ItemRarity.MYTHIC, SingularityLeggings.class),
    SINGULARITY_BOOTS(     "Singularity Boots",      Material.NETHERITE_BOOTS,      ItemRarity.MYTHIC, SingularityBoots.class),

    // DEBUG
    // Admin armor, makes you invincible basically
    INFINITY_HELMET(    "Infinity Helmet",     Material.NETHERITE_HELMET,     ItemRarity.TRANSCENDENT, InfinityHelmet.class),
    INFINITY_CHESTPLATE("Infinity Chestplate", Material.NETHERITE_CHESTPLATE, ItemRarity.TRANSCENDENT, InfinityChestplate.class),
    INFINITY_LEGGINGS(  "Infinity Leggings",   Material.NETHERITE_LEGGINGS,   ItemRarity.TRANSCENDENT, InfinityLeggings.class),
    INFINITY_BOOTS(     "Infinity Boots",      Material.NETHERITE_BOOTS,      ItemRarity.TRANSCENDENT, InfinityBoots.class),
    INFINITY_SWORD(     "Infinity Sword",      Material.NETHERITE_SWORD,      ItemRarity.TRANSCENDENT, InfinitySword.class),

    BURGER("Burger", "545440bd8a551aea344d81bf398c9f7cfbaaad582b184785abf0ac1d1d78bb26", ItemRarity.SPECIAL),

    SPACE_HELMET("Space Helmet", Material.RED_STAINED_GLASS, ItemRarity.SPECIAL, true, SpaceHelmet.class),
    SPIDER_REPELLENT("Spider Repellent", Material.POTION, ItemRarity.SPECIAL, true, SpiderRepellentBlueprint.class),

    ENTITY_ANALYZER("Entity Analyzer", Material.CLOCK, ItemRarity.SPECIAL, true, EntityAnalyzer.class),
    ENTITY_ANALYZER_REPORT("Entity Analyzer Report", Material.PAPER, ItemRarity.SPECIAL, true),

    SPAWNER_EDITING_WAND("Spawner Editor Wand", Material.BREEZE_ROD, ItemRarity.SPECIAL, true, SpawnerEditorBlueprint.class),

    DUMMY_SMITHING_RESULT("DUMMY SMITHING RESULT", Material.BARRIER, ItemRarity.SPECIAL)
    ;

    public final String name;
    public String url = "";
    public final Material material;
    public final ItemRarity rarity;
    public final boolean glow;
    public final Class<? extends CustomItemBlueprint> handler;

    public static final int MODEL_DATA_OFFSET = 0x849FFB;

    CustomItemType(String name, String URL, ItemRarity rarity) {
        this.name = name;
        this.url = URL;
        this.material = Material.PLAYER_HEAD;
        this.rarity = rarity;
        this.glow = false;
        this.handler = CustomHeadBlueprint.class;
    }

    CustomItemType(String name, String URL, ItemRarity rarity, Class<? extends CustomHeadBlueprint> handler) {
        this.name = name;
        this.url = URL;
        this.material = Material.PLAYER_HEAD;
        this.rarity = rarity;
        this.glow = false;
        this.handler = handler;
    }

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

    public String getUrl() {
        return url;
    }

    public boolean hasCustomHeadTexture() {
        return !getUrl().isEmpty();
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

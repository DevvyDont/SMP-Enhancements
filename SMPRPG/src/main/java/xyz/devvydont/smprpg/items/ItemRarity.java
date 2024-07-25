package xyz.devvydont.smprpg.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

public enum ItemRarity {

    COMMON(NamedTextColor.WHITE),
    UNCOMMON(NamedTextColor.DARK_GREEN),
    RARE(NamedTextColor.BLUE),
    EPIC(NamedTextColor.DARK_PURPLE),
    LEGENDARY(NamedTextColor.GOLD),
    MYTHIC(NamedTextColor.LIGHT_PURPLE),
    DIVINE(NamedTextColor.AQUA),
    TRANSCENDENT(NamedTextColor.RED),
    SPECIAL(NamedTextColor.DARK_RED)
    ;

    public final NamedTextColor color;

    ItemRarity(NamedTextColor color) {
        this.color = color;
    }

    /**
     * Given a vanilla material, determine a rarity for it. Most vanilla items will be uncommon or common
     * @param material
     * @return
     */
    public static ItemRarity ofVanillaMaterial(Material material) {
        return switch (material) {

            case DIAMOND_SWORD, DIAMOND_HOE, DIAMOND_PICKAXE, DIAMOND_SHOVEL, DIAMOND_AXE,
                 DIAMOND_BOOTS, DIAMOND_CHESTPLATE, DIAMOND_HELMET, DIAMOND_LEGGINGS,
                 EMERALD, EMERALD_ORE, QUARTZ_BLOCK, OBSIDIAN,
                 COAL_BLOCK, COPPER_BLOCK, IRON_BLOCK, GLOWSTONE, AMETHYST_BLOCK, GOLD_BLOCK,
                 GOLDEN_APPLE,
                 DIAMOND, DIAMOND_ORE, DIAMOND_HORSE_ARMOR,
                 NETHERITE_SCRAP, ANCIENT_DEBRIS, HEART_OF_THE_SEA, END_CRYSTAL, NAUTILUS_SHELL,
                 ENDER_PEARL, SHULKER_SHELL,
                 DRAGON_BREATH, EXPERIENCE_BOTTLE, ENCHANTED_BOOK,
                 TRIAL_KEY, BREEZE_ROD, POTION, SPLASH_POTION -> UNCOMMON;

            case NETHERITE_SWORD, NETHERITE_HOE, NETHERITE_PICKAXE, NETHERITE_SHOVEL, NETHERITE_AXE,
                 NETHERITE_BOOTS, NETHERITE_CHESTPLATE, NETHERITE_HELMET, NETHERITE_LEGGINGS,
                 NETHERITE_INGOT, NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                 DIAMOND_BLOCK, EMERALD_BLOCK,
                 TOTEM_OF_UNDYING, ELYTRA, TRIDENT, ENCHANTED_GOLDEN_APPLE, WITHER_SKELETON_SKULL,
                 CONDUIT, LINGERING_POTION,
                 ECHO_SHARD, OMINOUS_BOTTLE, OMINOUS_TRIAL_KEY -> RARE;

            case NETHERITE_BLOCK, NETHER_STAR, HEAVY_CORE -> EPIC;

            case BEACON,
                 MUSIC_DISC_5, MUSIC_DISC_11, MUSIC_DISC_13, MUSIC_DISC_CAT, MUSIC_DISC_BLOCKS, MUSIC_DISC_CHIRP,
                 MUSIC_DISC_CREATOR, MUSIC_DISC_CREATOR_MUSIC_BOX, MUSIC_DISC_FAR, MUSIC_DISC_MALL, MUSIC_DISC_MELLOHI,
                 MUSIC_DISC_OTHERSIDE, MUSIC_DISC_PIGSTEP, MUSIC_DISC_PRECIPICE, MUSIC_DISC_RELIC, MUSIC_DISC_STAL,
                 MUSIC_DISC_STRAD, MUSIC_DISC_WAIT, MUSIC_DISC_WARD -> LEGENDARY;

            case DRAGON_EGG -> TRANSCENDENT;

            case COMMAND_BLOCK, COMMAND_BLOCK_MINECART, BARRIER, LIGHT, JIGSAW, STRUCTURE_BLOCK, STRUCTURE_VOID,
                 CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK, DEBUG_STICK, KNOWLEDGE_BOOK -> SPECIAL;

            default -> COMMON;
        };

    }

    public Component applyDecoration(Component component) {

        // Legendary and lower only has a color
        if (this.ordinal() <= LEGENDARY.ordinal())
            return component.color(this.color);

        // > Legendary adds the magic text on both sides, and then whole component is wrapped in the color.
        return Component.text("o ").decoration(TextDecoration.OBFUSCATED, true)
                .append(component.decoration(TextDecoration.OBFUSCATED, false))
                .append(Component.text(" o").decoration(TextDecoration.OBFUSCATED, true))
                .color(this.color);

    }
}

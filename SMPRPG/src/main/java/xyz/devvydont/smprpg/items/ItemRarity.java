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
                 EMERALD, EMERALD_BLOCK, EMERALD_ORE,
                 GOLDEN_APPLE,
                 DIAMOND, DIAMOND_ORE, DIAMOND_BLOCK, DIAMOND_HORSE_ARMOR,
                 NETHERITE_SCRAP, ANCIENT_DEBRIS, HEART_OF_THE_SEA, END_CRYSTAL, NAUTILUS_SHELL,
                 TRIAL_KEY, BREEZE_ROD -> UNCOMMON;

            case NETHERITE_SWORD, NETHERITE_HOE, NETHERITE_PICKAXE, NETHERITE_SHOVEL, NETHERITE_AXE,
                 NETHERITE_BOOTS, NETHERITE_CHESTPLATE, NETHERITE_HELMET, NETHERITE_LEGGINGS,
                 NETHERITE_INGOT, NETHERITE_BLOCK, NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                 TOTEM_OF_UNDYING, ELYTRA, TRIDENT, ENCHANTED_GOLDEN_APPLE, WITHER_SKELETON_SKULL,
                 ECHO_SHARD, HEAVY_CORE, OMINOUS_BOTTLE, OMINOUS_TRIAL_KEY -> RARE;

            case NETHER_STAR -> EPIC;

            case DRAGON_EGG -> SPECIAL;

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

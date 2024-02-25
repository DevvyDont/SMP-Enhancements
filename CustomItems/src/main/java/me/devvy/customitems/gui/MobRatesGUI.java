package me.devvy.customitems.gui;

import me.devvy.customitems.listeners.MobKillListeners;
import me.devvy.customitems.listeners.OreMineListeners;
import me.devvy.customitems.util.FormatUtil;
import me.devvy.customitems.util.RNGRoller;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MobRatesGUI extends RatesViewerGUI {

    public Material getMaterialFromEntity(EntityType entityType) {
        switch (entityType) {

            case WARDEN:
                return Material.ECHO_SHARD;
            case ENDER_DRAGON:
                return Material.DRAGON_EGG;
            case WITHER:
                return Material.NETHER_STAR;

            case ELDER_GUARDIAN:
                return Material.SPONGE;

            case ENDERMAN:
                return Material.ENDER_PEARL;

            case ZOMBIFIED_PIGLIN:
                return Material.GOLD_NUGGET;

            case ZOMBIE:
                return Material.ROTTEN_FLESH;
            case SKELETON:
                return Material.BONE;
            case SPIDER:
                return Material.SPIDER_EYE;
            case CREEPER:
                return Material.GUNPOWDER;
            case STRAY:
                return Material.SNOWBALL;
            case HUSK:
                return Material.SAND;
            case DROWNED:
                return Material.TRIDENT;

            case CAVE_SPIDER:
                return Material.COBWEB;
            case WITCH:
                return Material.GLASS_BOTTLE;
            case BLAZE:
                return Material.BLAZE_ROD;
            case WITHER_SKELETON:
                return Material.COAL;
            case MAGMA_CUBE:
                return Material.MAGMA_CREAM;
            case SLIME:
                return Material.SLIME_BALL;
            case HOGLIN:
                return Material.COOKED_PORKCHOP;
            case ZOGLIN:
                return Material.PORKCHOP;
            case GUARDIAN:
                return Material.PRISMARINE_CRYSTALS;
            case PIGLIN_BRUTE:
                return Material.GOLDEN_AXE;
            case ZOMBIE_VILLAGER:
                return Material.EMERALD;

            case SHULKER:
                return Material.SHULKER_SHELL;
            case SILVERFISH:
                return Material.ENDER_EYE;

            case GHAST:
                return Material.GHAST_TEAR;
            case PHANTOM:
                return Material.PHANTOM_MEMBRANE;
            case PILLAGER:
                return Material.CROSSBOW;
            case VINDICATOR:
                return Material.IRON_AXE;

            case ENDERMITE:
                return Material.DISC_FRAGMENT_5;
            case VEX:
                return Material.IRON_SWORD;

            case EVOKER:
                return Material.TOTEM_OF_UNDYING;
            case RAVAGER:
                return Material.SADDLE;

        }

        return Material.CLAY_BALL;
    }

    public ItemStack getMobDisplayItem(Player player, EntityType entity) {

        ItemStack item = new ItemStack(getMaterialFromEntity(entity));
        ItemMeta meta = item.getItemMeta();
        RNGRoller rng = new RNGRoller(player, MobKillListeners.getBaseDropChance(entity), Enchantment.LOOT_BONUS_MOBS);

        meta.setDisplayName(ChatColor.GOLD + FormatUtil.getTitledString(entity.name().toLowerCase().replace("_", " ")));
        meta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + "Every time you " + ChatColor.AQUA + "kill this mob " + ChatColor.GRAY + "there is",
                ChatColor.GRAY + "a chance it will drop a " + ChatColor.LIGHT_PURPLE + "treasure item!",
                "",
                ChatColor.DARK_GRAY + "Luck Stat: " + ChatColor.DARK_PURPLE + player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_LUCK).getValue(),
                ChatColor.DARK_GRAY + "Looting:    " + ChatColor.DARK_PURPLE + rng.getEnchantLevels(),
                ChatColor.GRAY + "Current drop luck boost: " + ChatColor.GREEN + rng.getLuckMultiplierString(),
                "",

                ChatColor.GRAY + "Treasure item rates from killing mobs:",
                ChatColor.GRAY + "- Base chance: " + ChatColor.YELLOW + rng.getRatioOdds(false) + ChatColor.DARK_GRAY + " (" + rng.getPercentOdds(false) + ")",
                ChatColor.GRAY + "- Your chance: " + ChatColor.GREEN + rng.getRatioOdds(true) + ChatColor.DARK_GRAY + " (" + rng.getPercentOdds(true) + ")"
        ));

        item.setItemMeta(meta);
        return item;
    }


    @Override
    public Inventory construct(Player player) {
        Inventory inventory = super.construct(player);

        // Loop through every block in the game, and add a button if there is a chance for it to drop a treasure item
        for (EntityType entityType : EntityType.values()) {
            if (MobKillListeners.getBaseDropChance(entityType) <= 0 )
                continue;

            inventory.addItem(getMobDisplayItem(player, entityType));
        }

        return inventory;
    }


}

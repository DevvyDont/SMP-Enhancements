package xyz.devvydont.treasureitems.gui;

import org.bukkit.attribute.Attribute;
import xyz.devvydont.treasureitems.listeners.MobKillListeners;
import xyz.devvydont.treasureitems.listeners.OreMineListeners;
import xyz.devvydont.treasureitems.util.FormatUtil;
import xyz.devvydont.treasureitems.util.RNGRoller;
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
        return switch (entityType) {
            case WARDEN -> Material.ECHO_SHARD;
            case ENDER_DRAGON -> Material.DRAGON_EGG;
            case WITHER -> Material.NETHER_STAR;
            case ELDER_GUARDIAN -> Material.SPONGE;
            case ENDERMAN -> Material.ENDER_PEARL;
            case ZOMBIFIED_PIGLIN -> Material.GOLD_NUGGET;
            case ZOMBIE -> Material.ROTTEN_FLESH;
            case SKELETON -> Material.BONE;
            case SPIDER -> Material.SPIDER_EYE;
            case CREEPER -> Material.GUNPOWDER;
            case STRAY -> Material.SNOWBALL;
            case HUSK -> Material.SAND;
            case DROWNED -> Material.TRIDENT;
            case CAVE_SPIDER -> Material.COBWEB;
            case WITCH -> Material.GLASS_BOTTLE;
            case BLAZE -> Material.BLAZE_ROD;
            case WITHER_SKELETON -> Material.COAL;
            case MAGMA_CUBE -> Material.MAGMA_CREAM;
            case SLIME -> Material.SLIME_BALL;
            case HOGLIN -> Material.COOKED_PORKCHOP;
            case ZOGLIN -> Material.PORKCHOP;
            case GUARDIAN -> Material.PRISMARINE_CRYSTALS;
            case PIGLIN_BRUTE -> Material.GOLDEN_AXE;
            case ZOMBIE_VILLAGER -> Material.EMERALD;
            case SHULKER -> Material.SHULKER_SHELL;
            case SILVERFISH -> Material.ENDER_EYE;
            case GHAST -> Material.GHAST_TEAR;
            case PHANTOM -> Material.PHANTOM_MEMBRANE;
            case PILLAGER -> Material.CROSSBOW;
            case VINDICATOR -> Material.IRON_AXE;
            case ENDERMITE -> Material.DISC_FRAGMENT_5;
            case VEX -> Material.IRON_SWORD;
            case EVOKER -> Material.TOTEM_OF_UNDYING;
            case RAVAGER -> Material.SADDLE;
            default -> Material.CLAY_BALL;
        };

    }

    public ItemStack getMobDisplayItem(Player player, EntityType entity) {

        ItemStack item = new ItemStack(getMaterialFromEntity(entity));
        ItemMeta meta = item.getItemMeta();
        RNGRoller rng = new RNGRoller(player, MobKillListeners.getBaseDropChance(entity), Enchantment.LOOTING);

        meta.setDisplayName(ChatColor.GOLD + FormatUtil.getTitledString(entity.name().toLowerCase().replace("_", " ")));
        meta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + "Every time you " + ChatColor.AQUA + "kill this mob " + ChatColor.GRAY + "there is",
                ChatColor.GRAY + "a chance it will drop a " + ChatColor.LIGHT_PURPLE + "treasure item!",
                "",
                ChatColor.DARK_GRAY + "Luck Stat: " + ChatColor.DARK_PURPLE + player.getAttribute(Attribute.LUCK).getValue(),
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

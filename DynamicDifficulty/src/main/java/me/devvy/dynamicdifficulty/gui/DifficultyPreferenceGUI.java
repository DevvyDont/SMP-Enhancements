package me.devvy.dynamicdifficulty.gui;

import me.devvy.dynamicdifficulty.listeners.ExperienceGainListener;
import me.devvy.dynamicdifficulty.storage.DifficultyVoteReport;
import me.devvy.dynamicdifficulty.DynamicDifficulty;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DifficultyPreferenceGUI implements Listener {

    private final int INFO_SLOT = 0;

    private final int EASY_SLOT = 3;
    private final int NORMAL_SLOT = 4;
    private final int HARD_SLOT = 5;

    private final int CLOSE_SLOT = 8;

    private Inventory inventory;

    public DifficultyPreferenceGUI() {

        DynamicDifficulty.getInstance().getServer().getPluginManager().registerEvents(this, DynamicDifficulty.getInstance());
        inventory = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Difficulty Preferences");
        update();

    }

    private ItemStack getInfoItem() {

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.AQUA + "Info");
        itemMeta.setLore(Arrays.asList(
                        "",
                        ChatColor.GRAY + "Current server difficulty: " + DynamicDifficulty.getInstance().difficultyToColor(DynamicDifficulty.getInstance().getCurrentDifficulty()) + DynamicDifficulty.getInstance().getCurrentDifficulty().name(),
                        "",
                        ChatColor.GRAY + "You can set your preferred difficulty here.",
                        ChatColor.GRAY + "The server will try to set the difficulty to",
                        ChatColor.GRAY + "the most voted difficulty.",
                        ChatColor.GRAY + "If you don't have a preference, the server",
                        ChatColor.GRAY + "will default to the lowest difficulty."
                ));

        item.setItemMeta(itemMeta);
        return item;

    }

    private Material difficultyToMaterial(Difficulty difficulty) {
        switch (difficulty) {
            case PEACEFUL:
                return Material.APPLE;
            case EASY:
                return Material.CORNFLOWER;
            case NORMAL:
                return Material.IRON_SWORD;
            case HARD:
                return Material.NETHER_STAR;
            default:
                return Material.BARRIER;
        }
    }

    private ItemStack getDifficultyItem(Difficulty difficulty) {

        DifficultyVoteReport voteReport = DynamicDifficulty.getInstance().getDifficultyPreferenceStorageManager().getDifficultyVoteReport();

        ItemStack item = new ItemStack(difficultyToMaterial(difficulty));
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(DynamicDifficulty.getInstance().difficultyToColor(difficulty) + difficulty.name() + ChatColor.GRAY + " (" + voteReport.getVotesFor(difficulty) + " vote(s))");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + "Click to set preferred difficulty to " + DynamicDifficulty.getInstance().difficultyToColor(difficulty) + difficulty.name() + "!");

        // Add difficulty specific information
        float diffMultiplier = ExperienceGainListener.getExperienceMultiplier(difficulty);
        if (diffMultiplier != 1)
            lore.add(ChatColor.GRAY + "- " + (diffMultiplier > 1 ? ChatColor.GREEN : ChatColor.RED) + "+" + (int) ((diffMultiplier-1) * 100) + "% XP gains");

        if (difficulty.equals(Difficulty.EASY))
            lore.add(ChatColor.GRAY + "- " + ChatColor.GREEN + "-50% mob damage");

        if (difficulty.equals(Difficulty.HARD)) {
            lore.add(ChatColor.GRAY + "- " + ChatColor.GREEN + "+1 Permanent Luck");
            lore.add(ChatColor.GRAY + "- " + ChatColor.RED + "+50% mob damage");
        }

        lore.add("");

        if (voteReport.getVotesFor(difficulty) > 0)
            lore.add(ChatColor.GRAY + "The following people have voted for this difficulty:");
        else
            lore.add(ChatColor.GRAY + "No one has voted for this difficulty yet!");

        for (String voter : voteReport.getVotersFor(difficulty))
            lore.add(ChatColor.GRAY + "- " + ChatColor.AQUA + voter);

        itemMeta.setLore(lore);

        if (voteReport.getVotesFor(difficulty) > 0)
            item.setAmount(voteReport.getVotesFor(difficulty));

        if (difficulty.equals(DynamicDifficulty.getInstance().getCurrentDifficulty()))
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(itemMeta);

        return item;

    }

    private ItemStack getCloseItem() {

        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ChatColor.RED + "Close");
        itemMeta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + "Click to close this menu!"
        ));

        item.setItemMeta(itemMeta);
        return item;

    }

    public void update() {

        inventory.clear();

        inventory.setItem(INFO_SLOT, getInfoItem());

        inventory.setItem(EASY_SLOT, getDifficultyItem(Difficulty.EASY));
        inventory.setItem(NORMAL_SLOT, getDifficultyItem(Difficulty.NORMAL));
        inventory.setItem(HARD_SLOT, getDifficultyItem(Difficulty.HARD));

        inventory.setItem(CLOSE_SLOT, getCloseItem());
    }

    public void open(Player p) {
        update();
        p.openInventory(inventory);
    }

    @EventHandler
    public void onOpenShop(InventoryOpenEvent event) {
        if (event.getInventory().equals(inventory))
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_BELL_USE, 1, 3f);
    }

    @EventHandler
    public void onCloseShop(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_BELL_USE, 1, 1.8f);

        DynamicDifficulty.getInstance().checkForNewDifficulty();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        update();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskLater(DynamicDifficulty.getInstance(), 1);
    }

    @EventHandler
    public void onClickedInShop(InventoryClickEvent event) {

        // If a click happened while this inventory was open, cancel whatever the action was, otherwise ignore this event
        if (inventory.getViewers().contains(event.getWhoClicked()))
            event.setCancelled(true);
        else
            return;

        // Only listen to normal left clicks
        if (!event.getAction().equals(InventoryAction.PICKUP_ALL))
            return;

        if (event.getClickedInventory() == null)
            return;

        if (!event.getClickedInventory().equals(inventory))
            return;

        // Figure out which button they clicked
        switch (event.getSlot()) {

            case EASY_SLOT:
                DynamicDifficulty.getInstance().getDifficultyPreferenceStorageManager().setPreferredDifficulty((Player) event.getWhoClicked(), Difficulty.EASY);
                break;

            case NORMAL_SLOT:
                DynamicDifficulty.getInstance().getDifficultyPreferenceStorageManager().setPreferredDifficulty((Player) event.getWhoClicked(), Difficulty.NORMAL);
                break;

            case HARD_SLOT:
                DynamicDifficulty.getInstance().getDifficultyPreferenceStorageManager().setPreferredDifficulty((Player) event.getWhoClicked(), Difficulty.HARD);
                break;

            case CLOSE_SLOT:
                event.getWhoClicked().closeInventory();
                return;

            default:
                break;
        }

        ((Player)event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        update();
    }

    public void cleanup() {
        for (HumanEntity human : inventory.getViewers())
            human.closeInventory();
    }




}

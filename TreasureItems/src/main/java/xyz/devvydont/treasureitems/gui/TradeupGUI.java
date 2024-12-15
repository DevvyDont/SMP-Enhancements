package xyz.devvydont.treasureitems.gui;

import xyz.devvydont.treasureitems.TreasureItems;
import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.events.RareItemDropEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TradeupGUI extends RatesViewerGUI {

    public static final int AVERAGE_SCORE_BOOST = 10;

    public int INFO_SLOT = 0;
    public int[] INPUT_SLOTS = new int[]{1*9+3, 2*9+2, 2*9+3, 2*9+4, 3*9+3};
    public int OUTPUT_SLOT = 2*9+6;

    public Set<Inventory> guisLocked = new HashSet<>();

    public void setLocked(Inventory inventory, boolean locked) {
        if (locked)
            guisLocked.add(inventory);
        else
            guisLocked.remove(inventory);
    }

    public boolean isLocked(Inventory inventory) {
        return guisLocked.contains(inventory);
    }

    public Map<CustomItemBlueprint, Double> calculateBlueprintChances(Inventory inventory) {

        Map<CustomItemBlueprint, Integer> count = new HashMap<>();
        Map<CustomItemBlueprint, Double> chances = new HashMap<>();

        for (ItemStack itemStack : getItemsInInput(inventory)) {

            if (itemStack == null)
                continue;

            CustomItemBlueprint blueprint = TreasureItems.getInstance().getCustomItemManager().getBlueprint(itemStack);
            if (blueprint == null)
                continue;

            count.put(blueprint, count.getOrDefault(blueprint, 0) + 1);
        }

        for (Map.Entry<CustomItemBlueprint, Integer> entry : count.entrySet())
            chances.put(entry.getKey(), (double)entry.getValue()/INPUT_SLOTS.length);

        return chances;
    }

    public CustomItemBlueprint pickCustomBlueprintInInput(Inventory inventory) {

        // Pick a random index from the input slots
        int randomIndex = (int)(Math.random()*INPUT_SLOTS.length);
        return TreasureItems.getInstance().getCustomItemManager().getBlueprint(inventory.getItem(INPUT_SLOTS[randomIndex]));
    }

    public int countValidItemsInInput(Inventory inventory) {

        int numItems = 0;

        for (ItemStack item : getItemsInInput(inventory)) {

            CustomItemBlueprint blueprint = TreasureItems.getInstance().getCustomItemManager().getBlueprint(item);
            if (blueprint == null)
                continue;

            numItems++;
        }

        return numItems;
    }

    public int calculateAverageBlueprintScore(Inventory inventory) {

        int numItems = 0;
        float avgScore = 0;

        for (ItemStack item : getItemsInInput(inventory)) {

            CustomItemBlueprint blueprint = TreasureItems.getInstance().getCustomItemManager().getBlueprint(item);
            if (blueprint == null)
                continue;

            numItems++;
            avgScore += TreasureItems.getInstance().getCustomItemManager().getBlueprint(item).calculateScore(item);
        }

        if (numItems <= 0)
            return 0;

        return (int)avgScore/numItems;
    }

    /**
     * return a list of items in the input, can be null
     *
     * @param inventory pass in the gui that is being worked with
     * @return
     */
    public ItemStack[] getItemsInInput(Inventory inventory) {
        ItemStack[] items =  new ItemStack[]{null, null, null, null, null};
        for (int i = 0; i < INPUT_SLOTS.length; i++)
            items[i] = inventory.getItem(INPUT_SLOTS[i]);
        return items;
    }

    public boolean isRerollableItem(ItemStack item) {

        // Make sure the item is a custom item
        CustomItemBlueprint blueprint = TreasureItems.getInstance().getCustomItemManager().getBlueprint(item);
        if (blueprint == null)
            return false;

        return true;
    }

    public void resetOutputSlot(Inventory inventory) {
        inventory.setItem(OUTPUT_SLOT, createButton(ChatColor.GOLD + "Output", Material.GRAY_DYE, ChatColor.GRAY + "Insert items on the left to re-roll!"));
    }

    /**
     * Based on the state of the gui, update the output slot
     *
     * @param inventory
     */
    public void update(Inventory inventory) {

        int numItems = countValidItemsInInput(inventory);
        int guaranteedScore = calculateAverageBlueprintScore(inventory) + AVERAGE_SCORE_BOOST;
        String scoreColor = CustomItemBlueprint.chooseScoreColor(guaranteedScore);

        if (guaranteedScore > 100)
            guaranteedScore = 100;

        // If more items are needed, add this item
        if (numItems < 5)
            inventory.setItem(
                    OUTPUT_SLOT,
                    createButton(ChatColor.GOLD + "Output",
                    Material.GRAY_DYE,
                    ChatColor.GRAY + "Insert " + ChatColor.LIGHT_PURPLE + (5-numItems) + " more item(s)" + ChatColor.GRAY + " to re-roll!",
                    "",
                    ChatColor.GRAY + "The item will have a guaranteed gear rating of: " + scoreColor + guaranteedScore));
        else
            inventory.setItem(
                    OUTPUT_SLOT,
                    createButton(ChatColor.GOLD + "Re-roll!",
                    Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                    "",
                    ChatColor.GRAY + "Click to " + ChatColor.LIGHT_PURPLE + "RE-ROLL" + ChatColor.GRAY + " your items!",
                    "",
                    ChatColor.GRAY + "New item guaranteed gear rating: " + scoreColor + guaranteedScore,
                    ChatColor.GRAY + "Items inputted are " + ChatColor.RED + ChatColor.BOLD + "PERMANENTLY LOST" + ChatColor.GRAY + " when re-rolled!"
            ));

        inventory.getItem(OUTPUT_SLOT).addUnsafeEnchantment(Enchantment.MENDING, 1);
        inventory.getItem(OUTPUT_SLOT).getItemMeta().addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

        if (numItems == 0)
            resetOutputSlot(inventory);
    }

    public void delayUpdate(Inventory inventory) {
        new BukkitRunnable() {
            @Override
            public void run() {
                update(inventory);
            }
        }.runTaskLater(TreasureItems.getInstance(), 0);
    }

    public void attemptToReroll(Inventory inventory) {

        int numItems = countValidItemsInInput(inventory);
        float guaranteedRewardScore = calculateAverageBlueprintScore(inventory);

        // If there are not enough items then don't do anything
        if (numItems != INPUT_SLOTS.length)
            return;

        // Calculate the score we should give the new item and boost it by 10
        guaranteedRewardScore += AVERAGE_SCORE_BOOST;

        // If the score is above 100, lower it to 100
        if (guaranteedRewardScore > 100)
            guaranteedRewardScore = 100;

        // Pick a random blueprint from the input
        CustomItemBlueprint blueprint = pickCustomBlueprintInInput(inventory);
        // If we fucked up then cancel
        if (blueprint == null)
            return;

        // successfully started a reroll
        setLocked(inventory, true);

        // Keep generating blueprints until we get one that we want, try this no more than 1000 times
        int attempts = 0;
        ItemStack reward = null;
        while (attempts < 1000) {

            reward = blueprint.get();

            // If the score is good enough break out
            if (blueprint.calculateScore(reward) >= guaranteedRewardScore)
                break;

            attempts++;
        }

        if (reward == null)
            reward = blueprint.get(1f);

        // Now we can clear the input and give the item
        inventory.setItem(OUTPUT_SLOT, reward);
        for (int slot : INPUT_SLOTS)
            inventory.setItem(slot, null);

        for (HumanEntity p : inventory.getViewers())
            p.getWorld().playSound(p, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);

    }

    @Override
    public Inventory construct(Player player) {
        Inventory inventory =  super.construct(player);

        // fill empty spaces with gray glass
        for (int i = 0; i < inventory.getSize(); i++)
            if (inventory.getItem(i) == null)
                inventory.setItem(i, createButton("", Material.GRAY_STAINED_GLASS_PANE));

        // clear the input slots
        for (int slot : INPUT_SLOTS)
            inventory.setItem(slot, null);

        // clear the output slot
        resetOutputSlot(inventory);

        // set the info slot
        inventory.setItem(INFO_SLOT, createButton(ChatColor.GOLD + "Info", Material.PAPER,
                "",
                ChatColor.GRAY + "Insert 5 treasure items to re-roll them into a new item!",
                "",
                ChatColor.GRAY + "The new treasure item received will have a guaranteed gear ",
                ChatColor.GRAY + "rating that is slightly better than the average of items that are trashed",
                ChatColor.GRAY + "The type of treasure item is also dependent on which ",
                ChatColor.GRAY + "items are input, meaning you can affect your chances of getting a preferred item!",
                "",
                ChatColor.GRAY + "Note: Items inputted are " + ChatColor.RED + ChatColor.BOLD + "PERMANENTLY LOST" + ChatColor.GRAY + " when re-rolled!"));

        inventory.getItem(INFO_SLOT).addUnsafeEnchantment(Enchantment.MENDING, 1);
        inventory.getItem(INFO_SLOT).getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        return inventory;

    }

    @Override
    public void handleClick(InventoryClickEvent event) {

        // If the clicked slot was the output slot...
        if (event.getClickedInventory().equals(playerToGUI.get(event.getWhoClicked().getUniqueId())) && event.getSlot() == OUTPUT_SLOT) {

            // If the item in this slot is a gear item then drop it and announce it
            ItemStack itemInOutput = event.getInventory().getItem(OUTPUT_SLOT);
            if (TreasureItems.getInstance().getCustomItemManager().getBlueprint(itemInOutput) != null) {

                Item item = event.getWhoClicked().getWorld().dropItemNaturally(event.getWhoClicked().getLocation(), itemInOutput);
                event.getInventory().setItem(OUTPUT_SLOT, null);
                RareItemDropEvent newEvent = new RareItemDropEvent((Player)event.getWhoClicked(), item, 1f, "trading up");
                TreasureItems.getInstance().getServer().getPluginManager().callEvent(newEvent);
                for (HumanEntity p : new ArrayList<>(event.getInventory().getViewers()))
                    p.closeInventory();

                return;
            } else {
                attemptToReroll(event.getInventory());
                return;
            }
        }

        if (isLocked(event.getInventory())) {
            super.handleClick(event);
            return;
        }

        // If the clicked slot is anything that is not an input slot and in the gui then handle as normal
        int clickedSlot = event.getSlot();
        boolean inputInteraction = false;

        if (event.getClickedInventory().equals(playerToGUI.get(event.getWhoClicked().getUniqueId()))) {

            for (int slot : INPUT_SLOTS)
                if (slot == clickedSlot) {
                    inputInteraction = true;
                    break;
                }
        }

        // If an input slot was clicked, always let them do whatever to it
        if (inputInteraction) {
            event.setCancelled(false);
            delayUpdate(event.getInventory());
            return;
        }

        // If the player clicked on their own inventory, only let the event happen if the slot being clicked is
        // either empty or a custom item that can be rerolled
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER))
            if (event.getClickedInventory().getItem(event.getSlot()) == null || isRerollableItem(event.getClickedInventory().getItem(event.getSlot()))) {
                event.setCancelled(false);
                delayUpdate(event.getInventory());
                return;
            }

        // let the player do what they want with the input slots
        super.handleClick(event);
    }

    @Override
    public void handleClose(InventoryCloseEvent event) {

        super.handleClose(event);

        // Spit out all the items
        for (ItemStack item : getItemsInInput(event.getInventory()))
            if (item != null)
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), item);

        if (TreasureItems.getInstance().getCustomItemManager().getBlueprint(event.getInventory().getItem(OUTPUT_SLOT)) != null) {
            Item item = event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), event.getInventory().getItem(OUTPUT_SLOT));
            event.getInventory().setItem(OUTPUT_SLOT, null);
            RareItemDropEvent newEvent = new RareItemDropEvent((Player) event.getPlayer(), item, 1f, "trading up");
            TreasureItems.getInstance().getServer().getPluginManager().callEvent(newEvent);
        }
    }
}

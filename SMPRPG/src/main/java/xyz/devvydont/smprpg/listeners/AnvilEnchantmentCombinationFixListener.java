package xyz.devvydont.smprpg.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.calculator.EnchantmentCalculator;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint;

import java.util.*;

public class AnvilEnchantmentCombinationFixListener implements Listener {

    private final SMPRPG plugin;

    public AnvilEnchantmentCombinationFixListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public record EnchantmentCombination(ItemStack result, int cost){}

    /**
     * Combines the enchantments of a 2nd item stack on to the first. Respects limits such as ItemRarity as well as
     * returns a cost to use if this was an anvil event.
     *
     * @return
     */
    public EnchantmentCombination combineEnchantments(@NotNull ItemStack input, @NotNull ItemStack combine) {

        // Retrieve the blueprints of both items
        SMPItemBlueprint inputBlueprint = SMPRPG.getInstance().getItemService().getBlueprint(input);
        SMPItemBlueprint combineBlueprint = SMPRPG.getInstance().getItemService().getBlueprint(combine);

        // Grab the enchantments we are trying to apply to the first item
        List<CustomEnchantment> toApply = new ArrayList<>();

        Map<Enchantment, Integer> combineEnchantMap;
        // If the item is a book, we have to retrieve the enchants differently
        if (!combineBlueprint.isCustom() && combine.getType().equals(Material.ENCHANTED_BOOK))
            combineEnchantMap = ((EnchantmentStorageMeta) combine.getItemMeta()).getStoredEnchants();
        else
            combineEnchantMap = combine.getEnchantments();

        // Transform vanilla enchants to our enchant wrapper
        for (Map.Entry<Enchantment, Integer> entry : combineEnchantMap.entrySet())
            toApply.add(SMPRPG.getInstance().getEnchantmentService().getEnchantment(entry.getKey()).build(entry.getValue()));

        // Now we actually do the enchantment application. First, we need to figure out how many new enchants
        // this item is allowed to have so it doesn't go over its limit.
        int newEnchantmentSlots = EnchantmentCalculator.getMaxAllowedEnchantments(inputBlueprint.getRarity(input)) - input.getEnchantments().size();

        // Attempt to apply all the enchantments from the 2nd item.
        int cost = 0;
        ItemStack result = input.clone();
        Collections.shuffle(toApply);
        for (CustomEnchantment enchantment : toApply) {

            int levelPresent = result.getEnchantmentLevel(enchantment.getEnchantment());
            int levelToUse = combine.getEnchantmentLevel(enchantment.getEnchantment());

            // Fix for book logic
            if (result.getItemMeta() instanceof EnchantmentStorageMeta meta)
                levelPresent = meta.getStoredEnchantLevel(enchantment.getEnchantment());
            if (combine.getItemMeta() instanceof EnchantmentStorageMeta meta)
                levelToUse = meta.getStoredEnchantLevel(enchantment.getEnchantment());

            // Skip this enchantment if a higher level is already present.
            if (levelPresent > levelToUse)
                continue;

            // Skip this enchantment if we don't have room for another enchantment and we don't have it.
            if (levelPresent <= 0 && newEnchantmentSlots <= 0)
                continue;

            // Skip this enchantment if the two levels are equal and also the max level
            if (levelPresent == levelToUse && levelToUse >= enchantment.getEnchantment().getMaxLevel())
                continue;

            // If this enchantment is new, decrement the slots we have
            if (levelPresent <= 0)
                newEnchantmentSlots--;

            // If the enchantment levels are equal, we should use a level higher if the level can go higher
            if (levelPresent == levelToUse && levelToUse < enchantment.getEnchantment().getMaxLevel())
                levelToUse++;

            // Apply the enchantment to the result, again depending on what item we have we have to do it differently
            if (result.getItemMeta() instanceof EnchantmentStorageMeta meta) {
                meta.addStoredEnchant(enchantment.getEnchantment(), levelToUse, true);
                result.setItemMeta(meta);
            } else
                result.addUnsafeEnchantment(enchantment.getEnchantment(), levelToUse);

            cost += enchantment.getMinimumCost().additionalPerLevelCost() * levelToUse + enchantment.getMinimumCost().baseCost();
        }

        // Now add on the repair cost from the item
        if (result.getItemMeta() instanceof Repairable repairable)
            cost += repairable.getRepairCost();

        SMPItemBlueprint resultBlueprint = SMPRPG.getInstance().getItemService().getBlueprint(result);
        resultBlueprint.updateMeta(result);
        return new EnchantmentCombination(result, cost);
    }

    /**
     * Allow anvil combinations to go as far as we want
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAnvilPreperation(PrepareAnvilEvent event) {
        event.getInventory().setMaximumRepairCost(999999);
    }

    /**
     * Completely override how enchantment combination behavior works when trying to combine two similar items.
     * Allow two items of the same type to be combined
     *
     * @param event
     */
    @EventHandler
    public void onAnvilCombination(PrepareAnvilEvent event) {

        AnvilInventory anvil = event.getInventory();
        ItemStack firstItemStack = anvil.getFirstItem();
        ItemStack secondItemStack = anvil.getSecondItem();

        // If either item is null, we don't care about this event
        if (firstItemStack == null || secondItemStack == null)
            return;

        // We know we have items in both slots, let's set result to null to be safe and prevent unwanted behavior
        event.setResult(null);

        // We have to support books being supplied to us.
        SMPItemBlueprint firstBlueprint = plugin.getItemService().getBlueprint(firstItemStack);
        SMPItemBlueprint secondBlueprint = plugin.getItemService().getBlueprint(secondItemStack);
        boolean inputIsBook = EnchantmentCalculator.isBook(firstBlueprint, firstItemStack);
        boolean combineItemIsBook = EnchantmentCalculator.isBook(secondBlueprint, secondItemStack);

        // If the first item is a book and the second item is a non book, we can't do anything
        if (inputIsBook && !combineItemIsBook)
            return;

        // If both items are non books and the items are not of the same type, we can't do anything. (Trying to combine a emerald chestplate with a diamond chestplate)
        if (!inputIsBook && !combineItemIsBook && !firstBlueprint.isItemOfType(secondItemStack))
            return;

        // Perform the combination!!!
        EnchantmentCombination combination = combineEnchantments(firstItemStack, secondItemStack);
        // If the cost is 0, that means this enchantment combination got nothing done
        if (combination.cost() <= 0)
            return;

        event.setResult(combination.result());
        event.getInventory().setRepairCostAmount(combination.cost());
    }

}

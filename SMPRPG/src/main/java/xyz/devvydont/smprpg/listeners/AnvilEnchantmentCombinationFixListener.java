package xyz.devvydont.smprpg.listeners;

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
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.calculator.EnchantmentCalculator;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint;

import java.util.HashMap;
import java.util.Map;

public class AnvilEnchantmentCombinationFixListener implements Listener {

    private final SMPRPG plugin;

    public AnvilEnchantmentCombinationFixListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Allow anvil combinations to go as far as we want
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAnvilPreperation(PrepareAnvilEvent event) {
        event.getInventory().setMaximumRepairCost(999);
    }

    /**
     * Completely override how enchantment combination behavior works when trying to combine enchantments with items.
     * If a book is allowed to go on an item, then allow it to happen
     *
     * @param event
     */
    @EventHandler
    public void onCombineEnchantments(PrepareAnvilEvent event) {


        AnvilInventory anvil = event.getInventory();
        ItemStack firstItemStack = anvil.getFirstItem();
        ItemStack secondItemStack = anvil.getSecondItem();

        // If either item is null, we don't care about this event
        if (firstItemStack == null || secondItemStack == null)
            return;

        SMPItemBlueprint firstBlueprint = plugin.getItemService().getBlueprint(firstItemStack);
        SMPItemBlueprint secondBlueprint = plugin.getItemService().getBlueprint(secondItemStack);

        // If the first item is an enchanted book, we don't care about this even
        if (firstBlueprint instanceof VanillaItemBlueprint && firstItemStack.getType().equals(Material.ENCHANTED_BOOK))
            return;

        // If the second item isn't an enchanted book, we don't care about this event
        if (!(secondBlueprint instanceof VanillaItemBlueprint) || !secondItemStack.getType().equals(Material.ENCHANTED_BOOK))
            return;

        // Now we are actually going to make modifications.
        // We have some non-enchanted book item in the first slot, and an enchanted book in the second slot.
        // Find out what enchantments can apply
        EnchantmentCalculator calculator = new EnchantmentCalculator(firstItemStack, 100, 100, 0);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) secondItemStack.getItemMeta();
        Map<Enchantment, Integer> enchantments = new HashMap<>();

        // Loop through all the enchants stored on this book and find valid enchantments
        for (Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {

            // Consider the old level
            int level = firstItemStack.getEnchantmentLevel(entry.getKey());

            // If a higher level is already present, skip this one
            if (level > entry.getValue())
                continue;

            CustomEnchantment customEnchantment = plugin.getEnchantmentService().getEnchantment(entry.getKey());

            // If the same level is already present, increment enchantment by one
            int newLevel = entry.getValue();
            if (level == newLevel && newLevel != customEnchantment.getMaxLevel())
                newLevel++;

            // If the enchantment is allowed, apply it
            if (calculator.enchantmentIsAllowed(customEnchantment, enchantments.keySet()))
                enchantments.put(entry.getKey(), newLevel);
        }

        int cost = 0;
        Repairable repairable = (Repairable) firstItemStack.getItemMeta();
        cost += repairable.getRepairCost();
        repairable = (Repairable) firstItemStack.getItemMeta();
        cost += repairable.getRepairCost();

        // Set the result!
        ItemStack result = firstItemStack.clone();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            // Clear the old enchantment and add the new one
            result.removeEnchantment(entry.getKey());
            result.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            cost += entry.getKey().getMinModifiedCost(entry.getValue());
        }

        if (cost <= 0) {
            event.setResult(null);
            return;
        }

        SMPItemBlueprint resultBlueprint = plugin.getItemService().getBlueprint(result);
        repairable = (Repairable) result.getItemMeta();
        repairable.setRepairCost(cost);
        result.setItemMeta(repairable);
        resultBlueprint.updateLore(result);
        event.setResult(result);
        anvil.setRepairCost(cost);
    }
}

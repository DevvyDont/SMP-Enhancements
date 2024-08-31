package xyz.devvydont.smprpg.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemEnchantedBook;
import xyz.devvydont.smprpg.items.interfaces.ReforgeApplicator;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

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

        // Edge case, do we have two different types of enchanted books? We can't do this
        if (inputBlueprint instanceof ItemEnchantedBook inputBook && combineBlueprint instanceof ItemEnchantedBook combineBook) {
            Enchantment inputEnchantment = inputBook.getEnchantment(input.getItemMeta());
            Enchantment combineEnchantment = combineBook.getEnchantment(combine.getItemMeta());
            if (inputEnchantment != null && !inputEnchantment.equals(combineEnchantment))
                return new EnchantmentCombination(input, 0);
        }

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
        int newEnchantmentSlots = inputBlueprint.getMaxAllowedEnchantments(input.getItemMeta()) - input.getEnchantments().size();

        // Attempt to apply all the enchantments from the 2nd item.
        int cost = 0;
        ItemStack result = input.clone();
        Collections.shuffle(toApply);
        boolean resultIsDifferent = false;
        for (CustomEnchantment enchantment : toApply) {

            int levelPresent = result.getEnchantmentLevel(enchantment.getEnchantment());
            int levelToUse = combine.getEnchantmentLevel(enchantment.getEnchantment());

            // Fix for book logic
            if (result.getItemMeta() instanceof EnchantmentStorageMeta meta)
                levelPresent = meta.getStoredEnchantLevel(enchantment.getEnchantment());
            if (combine.getItemMeta() instanceof EnchantmentStorageMeta meta)
                levelToUse = meta.getStoredEnchantLevel(enchantment.getEnchantment());

            // Skip this enchantment if it is not valid for the input item if it is not a book.
            if (!(inputBlueprint instanceof ItemEnchantedBook) && !inputBlueprint.getItemClassification().getItemTagKeys().contains(enchantment.getItemTypeTag()))
                continue;

            // Skip this enchantment if the input already contains a conflicting enchant.
            boolean conflicts = false;
            for (Enchantment appliedEnchantment : result.getEnchantments().keySet())
                if (!appliedEnchantment.equals(enchantment.getEnchantment()) && appliedEnchantment.conflictsWith(enchantment.getEnchantment()))
                    conflicts = true;
            if (conflicts)
                continue;

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

            resultIsDifferent = true;
            cost += enchantment.getMinimumCost().additionalPerLevelCost() * levelToUse + enchantment.getMinimumCost().baseCost();
        }

        // Did we even make a change? mark this combo as invalid
        if (!resultIsDifferent)
            return new EnchantmentCombination(result, 0);

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
        event.getView().setMaximumRepairCost(999_999);
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
        event.getView().setRepairCost(1);
        event.setResult(null);

        // We have to support books being supplied to us.
        SMPItemBlueprint firstBlueprint = plugin.getItemService().getBlueprint(firstItemStack);
        SMPItemBlueprint secondBlueprint = plugin.getItemService().getBlueprint(secondItemStack);
        boolean inputIsBook = EnchantmentCalculator.isEnchantedBook(firstBlueprint, firstItemStack);
        boolean combineItemIsBook = EnchantmentCalculator.isEnchantedBook(secondBlueprint, secondItemStack);

        // If the first item is a book and the second item is a non book, we can't do anything
        if (inputIsBook && !combineItemIsBook)
            return;

        // If both items are non books and the items are not of the same type, we can't do anything. (Trying to combine a emerald chestplate with a diamond chestplate)
        if (!inputIsBook && !combineItemIsBook && !firstBlueprint.isItemOfType(secondItemStack))
            return;

        // Perform the combination!!!
        EnchantmentCombination combination = combineEnchantments(firstItemStack, secondItemStack);
        // If the cost is 0, that means this enchantment combination got nothing done
        if (combination.cost() <= 0) {
            event.getView().setRepairCost(1);
            event.setResult(null);
            return;
        }

        // Is the player allowed to perform this combination? Check if all the enchants are unlocked by them.
        List<Component> information = new ArrayList<>();
        information.add(Component.empty());
        boolean allowed = true;
        int magicLevel = plugin.getEntityService().getPlayerInstance((Player)event.getView().getPlayer()).getMagicSkill().getLevel();

        Set<Map.Entry<Enchantment, Integer>> enchantmentsToAnalyze = plugin.getItemService().getBlueprint(combination.result()) instanceof ItemEnchantedBook ? ((EnchantmentStorageMeta)combination.result().getItemMeta()).getStoredEnchants().entrySet() : combination.result().getEnchantments().entrySet();
        for (Map.Entry<Enchantment, Integer> entries : enchantmentsToAnalyze) {

            // Magic skill req met?
            CustomEnchantment enchantment = plugin.getEnchantmentService().getEnchantment(entries.getKey());
            int requirement = enchantment.getSkillRequirement();
            if (requirement > magicLevel) {
                information.add(ComponentUtils.getColoredComponent("- Need Magic " + requirement + " to apply locked enchantment ", NamedTextColor.RED).append(enchantment.getDisplayName().color(enchantment.getEnchantColor())));
                allowed = false;
                continue;
            }

            // Level of enchant is too high?
            // Determine how many levels we have over the requirement and what magic level would give us the max level
            double percentage = (double) (entries.getValue()-1) / enchantment.getMaxLevel();
            int enchantLevelRequirement = (int) (percentage * (99-requirement) + requirement);
            enchantLevelRequirement = Math.min(99, enchantLevelRequirement);
            if (enchantLevelRequirement > magicLevel){
                information.add(ComponentUtils.getColoredComponent("- Need Magic " + enchantLevelRequirement + " to apply high level enchantment ", NamedTextColor.RED).append(enchantment.getEnchantment().displayName(entries.getValue()).color(enchantment.getEnchantColor())));
                allowed = false;
            }

        }

        information.add(Component.empty());
        information.add(ComponentUtils.getDefaultText("Experience Cost: ").append(ComponentUtils.getColoredComponent(combination.cost() + " Levels", NamedTextColor.GREEN)));

        if (!allowed)
            event.getView().setRepairCost(999_999);
        else
            event.getView().setRepairCost(combination.cost);

        combination.result().editMeta(meta -> {
            List<Component> newMeta = meta.lore();
            newMeta.addAll(information);
            meta.lore(ComponentUtils.cleanItalics(newMeta));
            if (meta instanceof Repairable repairable)
                repairable.setRepairCost(repairable.getRepairCost()+2);
        });
        event.setResult(combination.result());
    }

    /*
     * Listen for when we are trying to apply a reforge to an item. This should calculate AFTER we do our enchantment
     * combining shenanigans since this case is much simpler.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onCombineReforgeWithItem(PrepareAnvilEvent event) {

        // First, we need to make sure we are trying to do something valid before we can proceed.
        // Are both item slots filled?
        if (event.getInventory().getFirstItem() == null || event.getInventory().getSecondItem() == null)
            return;

        // Is the second slot a reforge stone?
        if (!(plugin.getItemService().getBlueprint(event.getInventory().getSecondItem()) instanceof ReforgeApplicator reforgeApplicator))
            return;

        // Can the first item have the reforge applied to it? And is the reforge actually valid?
        ReforgeType reforgeType = reforgeApplicator.getReforgeType();
        ReforgeBase reforge = plugin.getItemService().getReforge(reforgeType);
        if (reforge == null)
            return;

        SMPItemBlueprint input = plugin.getItemService().getBlueprint(event.getInventory().getFirstItem());
        // Can this reforge type be applied to this type of item?
        if (!reforgeType.isAllowed(input.getItemClassification()))
            return;

        // The item input is allowed to have this reforge stone's reforge. Let's set a result for them to choose if
        // they want.
        ItemStack result = event.getInventory().getFirstItem().clone();
        reforge.apply(result);
        event.setResult(result);
        event.getView().setRepairCost(reforgeApplicator.getExperienceCost());
    }

}

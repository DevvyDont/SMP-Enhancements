package xyz.devvydont.smprpg.enchantments.calculator;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint;

import java.util.*;

public class EnchantmentCalculator {

    public enum EnchantmentSlot {
        CHEAP(0),
        MID(1),
        EXPENSIVE(2)

        ;

        private final int index;

        EnchantmentSlot(int index) {
            this.index = index;
        }

        public static EnchantmentSlot fromButton(int button) {
            return EnchantmentSlot.values()[button];
        }

    }

    public static int MAX_BOOKSHELF_BONUS = 32;

    private ItemStack item;
    private int powerLevel;
    private int magicLevel;

    private final Random rng;

    /**
     * Construct this enchantment calculator using an item, the slot to use, and a bonus rating
     *
     * @param item ItemStack to enchant
     * @param bookshelfBonus Bookshelves used for enchants from 0-32, used to improve the power of enchants
     * @param magicLevel determines what enchantments are allowed to be rolled based on every enchantments skill req.
     */
    public EnchantmentCalculator(ItemStack item, int bookshelfBonus, int magicLevel, int seed) {
        this.item = item;
        this.powerLevel = bookshelfBonus;
        this.magicLevel = magicLevel;

        this.rng = new Random(seed);
    }


    public ItemStack getItem() {
        return item;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public int getMagicLevel() {
        return magicLevel;
    }

    /**
     * The number of allowed enchantments on an item depends on the rarity of it.
     * Common = 1
     * Uncommon = 3
     * Rare = 5
     * Epic = 7
     * Legendary = 9
     * Mythic = 11
     * Divine = 13
     * Transcendent = 15
     * Special = 17
     *
     * @return
     */
    public int getMaxAllowedEnchantments() {
        int rarityScore = SMPRPG.getInstance().getItemService().getBlueprint(item).getRarity(item).ordinal();
        return rarityScore * 2 + 1;
    }

    public boolean enchantmentIsAllowed(CustomEnchantment enchantment, List<Enchantment> previous) {

        // Is the enchantment a low enough skill requirement to apply?
        if (enchantment.getSkillRequirement() > getMagicLevel())
            return false;

        // Are we ignoring this enchantment? (Curse)
        if (getMagicLevel() >= enchantment.getSkillRequirementToAvoid())
            return false;


        SMPItemBlueprint blueprint = SMPRPG.getInstance().getItemService().getBlueprint(item);
        // Can this enchantment apply to this item according to minecraft's vanilla logic (if vanilla item)?
        if (blueprint instanceof VanillaItemBlueprint && !enchantment.getEnchantment().canEnchantItem(getItem()))
            return false;

        // Can this enchantment apply to this item according to our logic (if custom item?
        if (blueprint instanceof CustomItemBlueprint && !blueprint.getItemClassification().getItemTagKeys().contains(enchantment.getItemTypeTag()))
            return false;

        // Is there already a conflicting enchant?
        for (Enchantment prev : previous)
            if (enchantment.getEnchantment().conflictsWith(prev))
                return false;

        return true;
    }

    public boolean enchantmentIsAllowed(CustomEnchantment enchantment) {
        return enchantmentIsAllowed(enchantment, Collections.emptyList());
    }

    /**
     * Returns a new list of allowed enchantments based on the magic level of this calculation
     *
     * @return
     */
    public List<CustomEnchantment> getAllowedEnchantments() {
        List<CustomEnchantment> enchantments = new ArrayList<>();
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments())
            if (enchantmentIsAllowed(enchantment))
                enchantments.add(enchantment.build(1));

        return enchantments;
    }

    /**
     * We may want to use the bookshelf power as a percentage for math reasons
     *
     * @return
     */
    public float getPowerAsPercentage() {
        return (float)getPowerLevel() / MAX_BOOKSHELF_BONUS;
    }

    public int calculateSlotCost(EnchantmentSlot slot) {

        float power = getPowerAsPercentage();
        int maxCost = (int)(power * getMagicLevel());

        // Max is always magic level + 1 * power
        if (slot.equals(EnchantmentSlot.EXPENSIVE))
            return maxCost + 1;

        // Cheap is 33%
        if (slot.equals(EnchantmentSlot.CHEAP))
            return (int) (maxCost * 0.33) + 1;

        // mid is 66%
        return (int) (maxCost * 0.66) + 1;
    }

    public int calculateSuitableEnchantmentLevel(CustomEnchantment enchantment, EnchantmentSlot slot) {

        // Use the cost of the slot as a metric
        int cost = calculateSlotCost(slot);

        // Determine how many levels we have over the requirement and what magic level would give us the max level
        int magicLevelsOverRequirement = cost - enchantment.getSkillRequirement();
        int maxMagicLevels = 99 - enchantment.getSkillRequirement();

        float percentage = (float)magicLevelsOverRequirement / maxMagicLevels;
        int level = Math.round(enchantment.getMaxLevel() * percentage);

        // 20% chance to increase by one, 15% chance to decrease by one
        if (rng.nextInt(100) < 20)
            level++;
        else if (rng.nextInt(100) < 15)
            level--;

        // Bound the enchantment
        return Math.min(Math.max(1, level), enchantment.getMaxLevel());
    }

    /**
     * Using the cost of 1-100 determine what enchantments to put on this piece of gear.
     * The power of enchantments is determined by bookshelf strength + magic level
     *
     * @param slot
     * @return
     */
    public List<EnchantmentOffer> rollEnchantments(EnchantmentSlot slot) {
        List<EnchantmentOffer> enchants = new ArrayList<>();

        // First generate a list of enchantments we are allowed to receive
        List<CustomEnchantment> allowed = getAllowedEnchantments();

        // Now generate a pool of enchantments to randomly choose from
        List<CustomEnchantment> pool = new ArrayList<>();
        // Using the weight of the enchantment, add it multiple times to the pool
        for (CustomEnchantment enchantment : allowed)
            for (int i = 0; i < Math.max(1, enchantment.getWeight()); i++)
                pool.add(enchantment.build(calculateSuitableEnchantmentLevel(enchantment, slot)));

        int cost = calculateSlotCost(slot);
        int numEnchantsWanted = Math.min(getMaxAllowedEnchantments(), cost / 10 + 1);

        // While we have enchants to give, add to the item
        while (numEnchantsWanted > 0) {

            // Out of choices? abort
            if (pool.isEmpty())
                break;

            int choiceIndex = rng.nextInt(pool.size());
            CustomEnchantment choice = pool.get(choiceIndex);

            // Already going to add this enchant or it is not allowed? remove it
            boolean invalid = false;
            for (EnchantmentOffer enchantment : enchants)
                if (enchantment.getEnchantment().equals(choice.getEnchantment()))
                    invalid = true;

            List<Enchantment> flattenedEnchantments = new ArrayList<>();
            for (EnchantmentOffer prev : enchants)
                flattenedEnchantments.add(prev.getEnchantment());

            if (!enchantmentIsAllowed(choice, flattenedEnchantments))
                invalid = true;

            if (invalid) {
                pool.remove(choice);
                continue;
            }

            // New enchant offer!
            EnchantmentOffer offer = new EnchantmentOffer(choice.getEnchantment(), choice.getLevel(), cost);
            enchants.add(offer);
            numEnchantsWanted--;
        }

        return enchants;
    }

    /**
     * Returns a list with 3 elements. Each element is another list that is a list of all enchantment offers
     *
     * @return
     */
    public Map<EnchantmentSlot, List<EnchantmentOffer>> calculate() {

        Map<EnchantmentSlot, List<EnchantmentOffer>> offers = new HashMap<>();
        offers.put(EnchantmentSlot.CHEAP, rollEnchantments(EnchantmentSlot.CHEAP));
        offers.put(EnchantmentSlot.MID, rollEnchantments(EnchantmentSlot.MID));
        offers.put(EnchantmentSlot.EXPENSIVE, rollEnchantments(EnchantmentSlot.EXPENSIVE));
        return offers;

    }
}

package xyz.devvydont.smprpg.util.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeType;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.attribute.IAttributeContainer;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IAttributeItem;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.*;

public class AttributeUtil {

    /**
     * Returns a component to display the number portion of an attribute
     *
     * @param result
     * @return
     */
    public static Component getAttributeNumber(AttributeWrapper wrapper, AttributeUtil.AttributeCalculationResult result, boolean forcePercent) {

        TextColor color = NamedTextColor.GREEN;

        if (wrapper.Type.equals(AttributeType.HELPFUL)) {
            if (result.total() < 0 || (result.percentage() && result.getTotal() < 1))
                color = NamedTextColor.RED;
        } else if (wrapper.Type.equals(AttributeType.PUNISHING)) {
            color = NamedTextColor.RED;
            if (result.total() < 0 || (result.percentage() && result.getTotal() < 1))
                color = NamedTextColor.GREEN;
        }

        if (wrapper.Type.equals(AttributeType.SPECIAL))
            color = NamedTextColor.LIGHT_PURPLE;

        // Some attributes are weird and are always percents
        return ComponentUtils.create(result.formatTotal(forcePercent), color);
    }

    public static Component formatAttribute(AttributeWrapper wrapper, AttributeUtil.AttributeCalculationResult result, boolean forcePercent) {

        return ComponentUtils.create(wrapper.DisplayName + ": ")
                .append(getAttributeNumber(wrapper, result, forcePercent));
    }

    public static Component formatBonus(NamedTextColor color, AttributeUtil.AttributeCalculationResult result, boolean forcePercent) {
        return ComponentUtils.create(" (" + result.formatTotal(forcePercent) + ")", color);
    }

    /*
     * Some attributes should force display as percents since that is how minecraft treats them. An example of this
     * is fall damage multiplier.
     */
    public static boolean forceAttributePercentage(AttributeWrapper wrapper) {
        return wrapper.equals(AttributeWrapper.KNOCKBACK_RESISTANCE) || wrapper.equals(AttributeWrapper.EXPLOSION_KNOCKBACK_RESISTANCE) || wrapper.equals(AttributeWrapper.SWEEPING) || wrapper.equals(AttributeWrapper.MINING_EFFICIENCY) || wrapper.equals(AttributeWrapper.UNDERWATER_MINING);
    }

    /**
     * Analyzes an item, its blueprint, and its associated data to determine what modifiers it SHOULD have. Note, that
     * it is possible that the item may have modifiers that don't reflect the result of this method, but ideally we
     * apply modifiers to the item ONLY using this method.
     * @param blueprint The blueprint of the item.
     * @param item The item.
     * @return The modifiers it should have.
     */
    public static Multimap<AttributeWrapper, SourcedAttributeModifier> queryExpectedAttributeModifiers(SMPItemBlueprint blueprint, ItemStack item) {

        // This doesn't actually check what's ON the item already, but instead calculates what we SHOULD put on the item.
        // We do this by analyzing the item's state alongside the blueprint it represents.
        Multimap<AttributeWrapper, SourcedAttributeModifier> modifiers = HashMultimap.create();

        if (!(blueprint instanceof IAttributeItem attributeItem))
            return modifiers;

        var nameKey = attributeItem.getUniqueModifierKey();

        // Query base attributes...
        for (var baseAttribute : attributeItem.getAttributeModifiers(item)) {
            var key = AttributeModifierType.BASE.keyForItem(nameKey);
            modifiers.put(
                    baseAttribute.getAttribute(),
                    new SourcedAttributeModifier(baseAttribute.asModifier(key, attributeItem.getActiveSlot()), AttributeModifierType.BASE)
            );
        }

        // Query reforge attributes...
        var reforge = SMPRPG.getInstance().getItemService().getReforge(item);
        ItemRarity rarity = blueprint.getRarity(item);
        if (reforge != null)
            for (var reforgeAttribute : reforge.getAttributeModifiersWithRarity(rarity))
                modifiers.put(
                        reforgeAttribute.getAttribute(),
                        new SourcedAttributeModifier(reforgeAttribute.asModifier(AttributeModifierType.REFORGE.keyForItem(nameKey), attributeItem.getActiveSlot()), AttributeModifierType.REFORGE)
                );

        // Then enchantments.
        for (var enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments(item.getItemMeta())) {

            // Filter out non attribute enchantments. They don't apply modifiers.
            if (!(enchantment instanceof IAttributeContainer attributeContainer))
                continue;

            // Add the modifiers.
            for (var enchantmentAttribute : attributeContainer.getHeldAttributes())
                modifiers.put(
                        enchantmentAttribute.getAttribute(),
                        new SourcedAttributeModifier(enchantmentAttribute.asModifier(AttributeModifierType.ENCHANTMENT.keyForItem(nameKey), attributeItem.getActiveSlot()), AttributeModifierType.ENCHANTMENT)
                );
        }

        return modifiers;
    }

    public static void applyModifiers(SMPItemBlueprint blueprint, ItemStack item) {

        // Remove all modifiers off of the item.
        AttributeService.getInstance().clearAttributeModifiers(item);

        // Find out what modifiers should be on this item.
        var modifiers = queryExpectedAttributeModifiers(blueprint, item);

        // Apply them all.
        for (var modifier : modifiers.entries())
            AttributeService.getInstance().addAttributeModifier(item, modifier.getKey(), modifier.getValue());
    }

    /**
     * Returns a component section to display in the lore of the item.
     * This section will contain the data that displays item stats based on the attributes that are applied to the item.
     * @return The attribute component section for an item.
     */
    public static List<Component> getAttributeLore(SMPItemBlueprint blueprint, ItemStack item) {

        var modifiers = queryExpectedAttributeModifiers(blueprint, item);
        if (modifiers.isEmpty())
            return Collections.emptyList();

        // Now we can actually construct the attributes we are going to display.
        ArrayList<Component> lines = new ArrayList<>();

        // Loop through all the attributes in the order we want to display them.
        for (var attribute : AttributeWrapper.values()) {

            // Filter out the modifiers for this attribute only.
            if (!modifiers.containsKey(attribute))
                continue;

            var modifiersForAttribute = modifiers.get(attribute);
            if (modifiersForAttribute.isEmpty())
                continue;

            // The item no matter what is going to display the FINAL total of the attribute. Calculate that now.
            int base = 1;
            // If there are no additive bonuses in the pool, use a base of 1.
            for (var modifier : modifiersForAttribute)
                if (modifier.getOperation().equals(AttributeModifier.Operation.ADD_NUMBER))
                    base = 0;

            // Run the math for what we are going to display.
            var result = AttributeUtil.calculateAttributeBonus(modifiersForAttribute, base);
            boolean forcePercent = forceAttributePercentage(attribute);
            var line = AttributeUtil.formatAttribute(attribute, result, forcePercent);

            // Now filter out reforge only...
            var modifiersForReforge = modifiersForAttribute.stream().filter(m -> m.getSource().equals(AttributeModifierType.REFORGE)).toList();
            var reforgeResult = calculateAttributeBonus(modifiersForReforge, 0);
            if (!reforgeResult.empty())
                line = line.append(formatBonus(NamedTextColor.BLUE, reforgeResult, forcePercent));

            // Enchantment only...
            var modifiersForEnchant = modifiersForAttribute.stream().filter(m -> m.getSource().equals(AttributeModifierType.ENCHANTMENT)).toList();
            var enchantResult = calculateAttributeBonus(modifiersForEnchant, 0);
            if (!enchantResult.empty())
                line = line.append(formatBonus(NamedTextColor.LIGHT_PURPLE, enchantResult, forcePercent));

            // Done! Add the line :)
            lines.add(line);
        }

        return lines;
    }

    /**
     * Data class that stores the data calculated from a collection of attribute modifiers
     *
     * @param base
     * @param bonus
     * @param total
     */
    public record AttributeCalculationResult (double base, double bonus, double total, Set<AttributeModifier.Operation> operations) {

        public boolean empty() {
            return operations.isEmpty();
        }

        public String percent() {
            long num = Math.round(total * 100);
            return String.format("%d%%", num);
        }

        /**
         * Performs multiplication on the total to make certain attributes display better.
         */
        public AttributeCalculationResult multiply(int factor) {
            return new AttributeCalculationResult(base * factor, bonus * factor, total * factor, operations);
        }

        public int getBase() {
            return (int) base;
        }

        public int getBonus() {
            return (int) bonus;
        }

        public int getTotal() {
            return (int) total;
        }

        /**
         * Determines if this calculation result is a percentage modifier only or if contains raw value changes.
         *
         * @return
         */
        public boolean percentage() {
            return !operations.contains(AttributeModifier.Operation.ADD_NUMBER);
        }

        /**
         * This should be called when displaying on a piece of gear. Formats the number to look prettier.
         * If no additive bonuses were present, presents this stat as a percentage instead.
         *
         * @return
         */
        public String formatTotal(boolean forcePercent) {

            // If normal addition operations occurred, This should just be a flat number.
            if (!percentage() && forcePercent)
                return (total > 0 ? "+" : "") + String.format("%d%%", Math.round(total*100));
            if (!percentage())
                return (getTotal() > 0 ? "+" : "") + getTotal();

            // Only multiplication occurred, This is a percentage operation.
            // Due to this only being a percentage, we should subtract 1 for what we are actually displaying if bad
            double percentageDisplay = total < 1 ? 1 - total : total - 1;
            return (total > 1 ? "+" : (total > 0 ? "-" : "")) + String.format("%d%%", Math.round(percentageDisplay*100));
        }

    }

    /**
     * Attempt to guess what bonus an attribute is going to have based on a collection of modifiers.
     * Uses the following logic:
     * <a href="https://minecraft.fandom.com/wiki/Attribute">...</a>.
     *
     * @param modifiers The collection of modifiers to calculate a modified final value for given a base value.
     * @return A result of what the final value should be.
     */
    public static AttributeCalculationResult calculateAttributeBonus(Collection<? extends AttributeModifier> modifiers, double base) {

        double sum = base;

        // Collect all the modifiers and group them by modifier operation.
        Map<AttributeModifier.Operation, List<AttributeModifier>> operationToModifier = new HashMap<>();
        for (AttributeModifier modifier : modifiers) {
            List<AttributeModifier> thisOperationsModifiers = operationToModifier.getOrDefault(modifier.getOperation(), new ArrayList<>());
            thisOperationsModifiers.add(modifier);
            operationToModifier.put(modifier.getOperation(), thisOperationsModifiers);
        }

        // Process normal add operations, these simply just add into the sum
        for (AttributeModifier addModifier : operationToModifier.getOrDefault(AttributeModifier.Operation.ADD_NUMBER, new ArrayList<>()))
            sum += addModifier.getAmount();

        // Process the additive multiply modifiers, these will dump into the sum after we find a factor
        double scalarSum = 0;
        for (AttributeModifier addModifier : operationToModifier.getOrDefault(AttributeModifier.Operation.ADD_SCALAR, new ArrayList<>()))
            scalarSum += addModifier.getAmount();

        if (sum == 0)
            sum = 1;
        sum *= (scalarSum + 1.0);

        // Process the multiplicative modifiers, these simply multiplicatively stack on the sum
        for (AttributeModifier addModifier : operationToModifier.getOrDefault(AttributeModifier.Operation.MULTIPLY_SCALAR_1, new ArrayList<>()))
            sum *= (1 + addModifier.getAmount());

        // We now have our result!
        return new AttributeCalculationResult(base, sum - base, sum, operationToModifier.keySet());
    }

    /**
     * Given item meta, determine how much of a power bonus there is from enchantments and reforging
     *
     * @return
     */
    public static int getPowerBonus(ItemMeta meta) {

        int sum = 0;

        // This can happen when something tries to update too fast
        if (SMPRPG.getInstance() == null || SMPRPG.getInstance().getEnchantmentService() == null)
            return sum;

        // Get the reforge on the item and see if it has a power rating
        ReforgeBase reforge = SMPRPG.getInstance().getItemService().getReforge(meta);
        if (reforge != null)
            sum += reforge.getPowerRating();

        // Loop through all the enchantments on the item and determine if it has a power rating
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments(meta))
            if (enchantment instanceof AttributeEnchantment attribute)
                sum += attribute.getPowerRating();

        return sum;
    }

    public static int calculateValue(int power, ItemRarity rarity, boolean isCraftable) {
        int rarityMultiplier = (int) Math.pow(rarity.ordinal()+1, 2);
        int powerMultiplier = (int) Math.pow(power, 2);
        return powerMultiplier * rarityMultiplier / (isCraftable ? 8 : 1);
    }

}

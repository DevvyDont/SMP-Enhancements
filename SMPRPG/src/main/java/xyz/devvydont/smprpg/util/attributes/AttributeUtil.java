package xyz.devvydont.smprpg.util.attributes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Attr;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

import java.util.*;

public class AttributeUtil {

    public static Collection<AttributeModifier> getAllModifiers(Attribute attribute, ItemMeta meta) {

        if (meta == null || meta.getAttributeModifiers() == null)
            return Collections.emptyList();

        return meta.getAttributeModifiers(attribute);
    }

    /**
     * Given an entity returns the total of a certain attribute on an entity
     *
     * @param attribute
     * @param entity
     * @return
     */
    public static int getTotalArmorAttributes(Attribute attribute, LivingEntity entity) {

        if (entity.getEquipment() == null)
            return 0;

        ItemStack[] armor = entity.getEquipment().getArmorContents();
        int sum = 0;
        for (ItemStack stack : armor)
            if (stack != null && stack.getType() != Material.AIR)
                sum += calculateAttributeBonus(getAllModifiers(attribute, stack.getItemMeta()), 0).getTotal();

        // Also take into consideration their attributes on the player
        sum += calculateAttributeBonus(entity.getAttribute(attribute).getModifiers(), 0).getTotal();

        return sum;
    }

    /**
     * Used to properly retrieve the value of an attribute of an entity. Some attributes are weird and require us
     * to manually check for them because of limitations in the base game
     *
     * @param entity
     * @return
     */
    public static double getAttributeValue(Attribute attribute, LivingEntity entity) {

        // Armor toughness is capped at 20, so we need to actually manually check for it by analyzing armor
        if (attribute.equals(Attribute.GENERIC_ARMOR_TOUGHNESS))
            return getTotalArmorAttributes(attribute, entity);

        // Otherwise just return the normal value
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null)
            return 0;

        return instance.getValue();
    }


    /**
     * Returns a component to display the number portion of an attribute
     *
     * @param result
     * @return
     */
    public static Component getAttributeNumber(AttributeWrapper wrapper, AttributeUtil.AttributeCalculationResult result) {

        TextColor color = NamedTextColor.GREEN;

        if (wrapper.getAttributeType().equals(AttributeWrapper.AttributeType.POSITIVE)) {
            if (result.total() < 0 || (result.percentage() && result.getTotal() < 1))
                color = NamedTextColor.RED;
        } else if (wrapper.getAttributeType().equals(AttributeWrapper.AttributeType.NEGATIVE)) {
            color = NamedTextColor.RED;
            if (result.total() < 0 || (result.percentage() && result.getTotal() < 1))
                color = NamedTextColor.GREEN;
        }

        if (wrapper.getAttributeType().equals(AttributeWrapper.AttributeType.SPECIAL))
            color = NamedTextColor.LIGHT_PURPLE;

        return Component.text(result.formatTotal()).color(color);
    }

    public static Component formatAttribute(AttributeWrapper wrapper, AttributeUtil.AttributeCalculationResult result) {

        AttributeUtil.AttributeCalculationResult finalResult = result;

        // Knockback resistance for some reason calculates as decimals but displays as integers.....
        if (wrapper.getAttribute().equals(Attribute.GENERIC_KNOCKBACK_RESISTANCE))
            finalResult = result.multiply(10);

        return Component.text(wrapper.getCleanName() + ": ").color(NamedTextColor.GRAY)
                .append(getAttributeNumber(wrapper, finalResult));
    }

    public static Component formatBonus(NamedTextColor color, AttributeUtil.AttributeCalculationResult result) {
        return Component.text(" (" + result.formatTotal() + ")").color(color);
    }

    /**
     * Returns a component section to display in the lore of the item.
     * This section will contain the data that displays item stats based on the attributes that are applied to the item.
     *
     * @return
     */
    public static List<Component> getAttributeLore(Attributeable blueprint, ItemMeta meta) {

        ArrayList<Component> lines = new ArrayList<>();
        if (meta == null || !meta.hasAttributeModifiers() || meta.getAttributeModifiers() == null)
            return lines;

        // Since we are displaying attributes ourselves, hide the vanilla mc one
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        // Loop through every single attribute modifier stored on this item in the order that we defined them
        for (AttributeWrapper wrapper : AttributeWrapper.values()) {

            // Is this attribute present on this item? If not skip it
            Collection<AttributeModifier> modifers = meta.getAttributeModifiers(wrapper.getAttribute());
            if (modifers == null)
                continue;

            // Perform a total calculation to display on the stat
            AttributeUtil.AttributeCalculationResult result = AttributeUtil.calculateAttributeBonus(modifers, 0);
            Component line = AttributeUtil.formatAttribute(wrapper, result);

            // Perform a calculation on only enchantment attribute modifiers
            AttributeModifierType.AttributeSession enchants = blueprint.getAttributeSession(AttributeModifierType.ENCHANTMENT, meta);
            AttributeCalculationResult enchantResult = AttributeUtil.calculateAttributeBonus(enchants.getAttributeModifiers(wrapper.getAttribute()), 0);
            if (!enchantResult.empty())
                line = line.append(formatBonus(NamedTextColor.LIGHT_PURPLE, enchantResult));

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
        public String formatTotal() {

            // If normal addition operations occurred, This should just be a flat number.
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
     * https://minecraft.fandom.com/wiki/Attribute#:~:text=add%20(amount%20%2B%2F-),)%3A%20Saved%20as%20operation%201.
     *
     * @param modifiers
     * @return
     */
    public static AttributeCalculationResult calculateAttributeBonus(Collection<AttributeModifier> modifiers, double base) {

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

    public static void applyModifiers(Attributeable blueprint, ItemMeta meta) {

        // First base modifiers...
        AttributeModifierType.AttributeSession baseAttributes = blueprint.getAttributeSession(blueprint.getAttributeModifierType(), meta);
        baseAttributes.removeAttributeModifiers();
        for (AttributeEntry entry : blueprint.getAttributeModifiers())
            baseAttributes.addAttributeModifier(entry, blueprint.getActiveSlot());

        // Todo reforge....

        // enchantments....
        if (SMPRPG.getInstance().getEnchantmentService() == null)
            return;

        AttributeModifierType.AttributeSession enchantAttributes = blueprint.getAttributeSession(AttributeModifierType.ENCHANTMENT, meta);
        enchantAttributes.removeAttributeModifiers();
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments(meta))
            if (enchantment instanceof AttributeEnchantment attributeEnchantment)
                for (AttributeEntry entry : attributeEnchantment.getAttributeModifiers())
                    enchantAttributes.addAttributeModifier(entry, blueprint.getActiveSlot());
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

        // Loop through all the enchantments on the item and determine if it has a power rating
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments(meta))
            if (enchantment instanceof AttributeEnchantment attribute)
                sum += attribute.getPowerRating();

        return sum;
    }


}

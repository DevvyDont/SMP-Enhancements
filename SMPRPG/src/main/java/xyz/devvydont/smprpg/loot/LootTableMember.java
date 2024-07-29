package xyz.devvydont.smprpg.loot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.enchantments.calculator.EnchantmentCalculator;
import xyz.devvydont.smprpg.events.CustomChancedItemDropSuccessEvent;
import xyz.devvydont.smprpg.events.CustomItemDropRollEvent;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.items.LootSource;

import java.util.List;
import java.util.Random;

/**
 * Represents something that may appear in a loot table.
 * The "chance" is the percent chance that this item is rolled FOR EVERY SLOT in an inventory.
 * The "rolls" is how many times to roll for this member, meaning it can show up multiple times when being rolled.
 * The min and max constraints allow you to define a range of the item stack quantity that it can spawn in
 */
public class LootTableMember implements LootSource {

    private final ItemStack item;
    private int min = 1;
    private int max = 1;
    private int rolls = 1;
    private float chance = 1;
    private boolean influencedByLuck = true;
    private boolean wantEnchants = false;
    private int enchantPower = 15;

    public LootTableMember(ItemStack item) {
        this.item = item;
    }

    public static LootTableMember builder(ItemStack item) {
        LootTableMember instance = new LootTableMember(item);
        return instance;
    }

    public LootTableMember withMin(int min) {
        this.min = min;
        return this;
    }

    public LootTableMember withMax(int max) {
        this.max = max;
        return this;
    }

    public LootTableMember withChance(float chance) {
        this.chance = chance;
        return this;
    }

    public LootTableMember withRolls(int rolls) {
        this.rolls = rolls;
        return this;
    }

    public LootTableMember useLuck(boolean influenced) {
        this.influencedByLuck = influenced;
        return this;
    }

    public LootTableMember withEnchants(boolean wantEnchants, int power) {
        this.wantEnchants = wantEnchants;
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getRolls() {
        return rolls;
    }

    public float getChance() {
        return chance;
    }

    public boolean isInfluencedByLuck() {
        return influencedByLuck;
    }

    public boolean isWantEnchants() {
        return wantEnchants;
    }

    public void enchantItem(ItemStack item) {

        // Make an enchantment calculator and use the best option possible with the desired enchanting level
        EnchantmentCalculator calculator = new EnchantmentCalculator(item, EnchantmentCalculator.MAX_BOOKSHELF_BONUS, enchantPower, new Random().nextInt(Integer.MAX_VALUE));
        List<EnchantmentOffer> results = calculator.calculate().get(EnchantmentCalculator.EnchantmentSlot.EXPENSIVE);

        // Apply them!
        for (EnchantmentOffer offer : results)
            item.addUnsafeEnchantment(offer.getEnchantment(), offer.getEnchantmentLevel());
    }

    @Nullable
    public ItemStack roll(Player player) {

        float luck = (float) player.getAttribute(Attribute.GENERIC_LUCK).getValue();
        float chance = getChance();
        if (isInfluencedByLuck())
            chance *= (luck) / 100.0f + 1.0f;

        ItemStack reward = getItem().clone();
        reward.setAmount((int)(Math.random() * getMax()) + getMin());

        if (isWantEnchants())
            enchantItem(reward);

        CustomItemDropRollEvent rollEvent = new CustomItemDropRollEvent(player, player.getInventory().getItemInMainHand(), chance, reward);
        rollEvent.callEvent();

        boolean success = Math.random() < rollEvent.getChance();
        if (!success)
            return null;

        new CustomChancedItemDropSuccessEvent(player, rollEvent.getChance(), reward, this).callEvent();
        return reward;
    }

    /**
     * Component that displays in chat when a rare drop is obtained
     *
     * @return
     */
    @Override
    public Component getAsComponent() {
        return ComponentUtil.getDefaultText("looting a ").append(ComponentUtil.getColoredComponent("chest!", NamedTextColor.GOLD));
    }

}

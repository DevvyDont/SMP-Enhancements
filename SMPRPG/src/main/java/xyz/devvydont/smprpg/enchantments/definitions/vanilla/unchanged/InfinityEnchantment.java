package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;

public class InfinityEnchantment extends VanillaEnchantment implements Listener {

    public static int getNonconsumeChance(int level) {
        return level * 10;
    }


    public InfinityEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Infinity");
    }

    @Override
    public Component getDescription() {
        return Component.text("Provides a ").color(NamedTextColor.GRAY)
                .append(Component.text(getNonconsumeChance(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" chance to not consume arrows").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_BOW;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getWeight() {
        return 4;
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 1);
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 3);
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 20;
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {

        int infinityLevel = EnchantmentUtil.getHoldingEnchantLevel(getEnchantment(), event.getHand().getGroup(), event.getEntity().getEquipment());

        if (infinityLevel <= 0)
            return;

        // RNG for saving arrow
        if (getNonconsumeChance(infinityLevel) / 100.0 > Math.random())
            return;

        ItemStack consumable = event.getConsumable();
        if (consumable == null)
            return;

        consumable.setAmount(consumable.getAmount()-1);
    }
}

package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;

public class LeechEnchantment extends CustomEnchantment implements Listener {

    public static int getLifestealPercent(int level) {
        return level;
    }

    public LeechEnchantment(String id) {
        super(id);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Leech");
    }

    @Override
    public Component getDescription() {
        return Component.text("Heal ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getLifestealPercent(getLevel()) + "%").color(NamedTextColor.GREEN)
                .append(Component.text(" of max health upon kills").color(NamedTextColor.GRAY))
                );
    }

    @Override
    public RegistryKeySet<ItemType> getSupportedItems(RegistryFreezeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event) {
        return event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_WEAPON);
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getWeight() {
        return 2;
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 3);
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 1);
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @EventHandler
    public void onPlayerLeechedEntity(EntityDeathEvent event) {

        Player killer = event.getEntity().getKiller();

        // Did a player kill this entity?
        if (killer == null)
            return;

        AttributeInstance maxHP = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHP == null)
            return;

        // Is this player holding the enchantment?
        int leechLevels = EnchantmentUtil.getHoldingEnchantLevel(this, EquipmentSlotGroup.HAND, killer.getEquipment());
        if (leechLevels <= 0)
            return;

        // Heal for a percentage of their max HP
        killer.heal(getLifestealPercent(leechLevels) / 100.0 * maxHP.getValue(), EntityRegainHealthEvent.RegainReason.CUSTOM);
        killer.getWorld().playSound(event.getEntity().getEyeLocation(), Sound.ENTITY_BAT_DEATH, .05f, 2.0f);
    }
}

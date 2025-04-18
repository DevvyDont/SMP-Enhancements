package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class LeechEnchantment extends CustomEnchantment implements Listener {

    public static double getLifestealPercent(int level) {
        return switch (level){
            case 1 -> 0.2;
            case 2 -> 0.35;
            case 3 -> 0.65;
            case 4 -> 0.95;
            case 5 -> 1.25;
            case 6 -> 1.60;
            case 7 -> 2;
            default -> level * .25 + getLifestealPercent(5);
        };
    }

    public LeechEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Leech");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Heal "),
            ComponentUtils.create("+" + getLifestealPercent(getLevel()) + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" of max health when hurting an enemy")
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_WEAPON;
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
        return EnchantmentRarity.UNCOMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 15;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLeechedEntity(CustomEntityDamageByEntityEvent event) {

        if (!(event.getDealer() instanceof Player player))
            return;

        AttributeInstance maxHP = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHP == null)
            return;

        // Is this player holding the enchantment?
        int leechLevels = EnchantmentUtil.getHoldingEnchantLevel(this, EquipmentSlotGroup.HAND, player.getEquipment());
        if (leechLevels <= 0)
            return;

        // Heal for a percentage of their max HP
        player.heal(getLifestealPercent(leechLevels) / 100.0 * maxHP.getValue(), EntityRegainHealthEvent.RegainReason.CUSTOM);
        player.getWorld().playSound(event.getDamaged(), Sound.BLOCK_LAVA_POP, .3f, 1.75f);
    }
}

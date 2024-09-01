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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class SyphonEnchantment extends CustomEnchantment implements Listener {

    public static int getLifestealPercent(int level) {
        return switch (level){
            case 1 -> 2;
            case 2 -> 5;
            case 3 -> 10;
            case 4 -> 16;
            case 5 -> 25;
            default -> level * 10 + getLifestealPercent(5);
        };
    }

    public SyphonEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Syphon");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Heal "),
            ComponentUtils.create("+" + getLifestealPercent(getLevel()) + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" of max health when killing an enemy")
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
        return 39;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLeechedEntity(EntityDeathEvent event) {

        // If nobody killed this entity don't do anything
        if (event.getEntity().getKiller() == null)
            return;

        Player killer = event.getEntity().getKiller();

        AttributeInstance maxHP = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHP == null)
            return;

        // Is this player holding the enchantment?
        int leechLevels = EnchantmentUtil.getHoldingEnchantLevel(this, EquipmentSlotGroup.HAND, killer.getEquipment());
        if (leechLevels <= 0)
            return;

        // Heal for a percentage of their max HP
        killer.heal(getLifestealPercent(leechLevels) / 100.0 * maxHP.getValue(), EntityRegainHealthEvent.RegainReason.CUSTOM);
        killer.getWorld().playSound(event.getEntity(), Sound.ENTITY_BAT_DEATH, .07f, .75f);
    }
}

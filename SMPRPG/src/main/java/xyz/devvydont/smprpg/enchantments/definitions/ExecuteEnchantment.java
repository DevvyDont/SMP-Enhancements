package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class ExecuteEnchantment extends CustomEnchantment implements Listener {

    public static int getPercentDamageIncreaseForLowEnemy(int level) {
        return level * 15;
    }

    public static int HEALTH_THRESHOLD = 50;

    public ExecuteEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Execute");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases damage by "),
            ComponentUtils.create("+" + getPercentDamageIncreaseForLowEnemy(getLevel()) + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" for enemies under "),
            ComponentUtils.create(HEALTH_THRESHOLD + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" of their maximum health")
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
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 8;
    }

    @EventHandler
    private void __onDealDamageWithExecute(CustomEntityDamageByEntityEvent event) {

        if (!(event.getDealer() instanceof LivingEntity living) || living.getEquipment() == null)
            return;

        if (!(event.getDamaged() instanceof LivingEntity victim) || victim.getAttribute(Attribute.MAX_HEALTH) == null)
            return;

        // Are they over the threshold?
        double hp = victim.getHealth();
        double maxHP = victim.getAttribute(Attribute.MAX_HEALTH).getValue();
        if (hp / maxHP * 100 > HEALTH_THRESHOLD)
            return;

        // Retrieve the higher first strike level of the two hands to determine which one to use
        int firstStrikeLevels;
        int mainHandFSLevels = living.getEquipment().getItemInMainHand().getEnchantmentLevel(getEnchantment());
        int offHandFSLevels = living.getEquipment().getItemInOffHand().getEnchantmentLevel(getEnchantment());
        firstStrikeLevels = Math.max(mainHandFSLevels, offHandFSLevels);

        if (firstStrikeLevels <= 0)
            return;

        double multiplier = 1.0 + getPercentDamageIncreaseForLowEnemy(firstStrikeLevels) / 100.0;
        event.multiplyDamage(multiplier);
    }
}

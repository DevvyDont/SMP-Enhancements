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

public class CalamityEnchantment extends CustomEnchantment implements Listener {

    public static int getPercentDamageIncreaseForLowPlayer(int level) {
        return level * 10;
    }

    public static int HEALTH_THRESHOLD = 50;

    public CalamityEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Calamity");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases damage by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getPercentDamageIncreaseForLowPlayer(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" when you are below "))
                .append(Component.text(getPercentDamageIncreaseForLowPlayer(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" of your maximum health").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_CHEST_ARMOR;
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
        return EquipmentSlotGroup.CHEST;
    }

    @Override
    public int getSkillRequirement() {
        return 46;
    }

    @EventHandler
    public void onDealDamageWithCalamity(CustomEntityDamageByEntityEvent event) {

        if (!(event.getDealer() instanceof LivingEntity living) || living.getEquipment() == null)
            return;

        // Are they over the threshold?
        double hp = living.getHealth();
        double maxHP = living.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (hp / maxHP * 100 > HEALTH_THRESHOLD)
            return;

        // Retrieve the higher first strike level of the two hands to determine which one to use
        if (living.getEquipment().getChestplate() == null)
            return;

        int calamityLevels = living.getEquipment().getChestplate().getEnchantmentLevel(getEnchantment());
        if (calamityLevels <= 0)
            return;

        double multiplier = 1.0 + getPercentDamageIncreaseForLowPlayer(calamityLevels) / 100.0;
        event.multiplyDamage(multiplier);
    }

}

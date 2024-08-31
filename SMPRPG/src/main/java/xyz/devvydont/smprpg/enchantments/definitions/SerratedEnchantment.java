package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class SerratedEnchantment extends CustomEnchantment implements Listener {

    public static int getAdditionalPercentageIncrease(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 10;
            case 2 -> 20;
            case 3 -> 30;
            case 4 -> 45;
            case 5 -> 60;
            case 6 -> 75;
            case 7 -> 100;
            default -> getAdditionalPercentageIncrease(5) + 25 * (level-7);
        };
    }

    public SerratedEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Serrated");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.getDefaultText("Increases ")
                .append(ComponentUtils.getColoredComponent("critical hit", NamedTextColor.RED))
                .append(ComponentUtils.getDefaultText(" damage by an additional "))
                .append(ComponentUtils.getColoredComponent("+" + getAdditionalPercentageIncrease(getLevel()) + "%", NamedTextColor.GREEN));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 7;
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
        return 11;
    }

    @EventHandler
    public void onCriticalDamageDealt(CustomEntityDamageByEntityEvent event) {

        if (!event.getOriginalEvent().isCritical())
            return;

        if (!(event.getDealer() instanceof LivingEntity dealer))
            return;

        int level = EnchantmentUtil.getHoldingEnchantLevel(this, EquipmentSlotGroup.MAINHAND, dealer.getEquipment());
        if (level <= 0)
            return;

        double multi = getAdditionalPercentageIncrease(level) / 100.0;
        event.addScalarDamage(multi);
    }
}

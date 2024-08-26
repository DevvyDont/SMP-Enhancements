package xyz.devvydont.smprpg.enchantments.definitions;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;

public class WisdomEnchantment extends CustomEnchantment implements Listener {

    public static int getPercentExpForLevel(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 50;
            case 2 -> 75;
            case 3 -> 100;
            case 4 -> 150;
            case 5 -> 200;
            case 6 -> 250;
            case 7 -> 300;
            default -> 50 * level + getPercentExpForLevel(7);
        };
    }

    public WisdomEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Wisdom");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Experience orbs provide ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getPercentExpForLevel(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" more experience when picked up"));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_HEAD_ARMOR;
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
        return EquipmentSlotGroup.HEAD;
    }

    @Override
    public int getSkillRequirement() {
        return 50;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPickupExpWithWisdom(PlayerPickupExperienceEvent event) {

        int wisdomLevels = EnchantmentUtil.getEnchantLevel(getEnchantment(), event.getPlayer().getInventory().getHelmet());
        if (wisdomLevels <= 0)
            return;

        double multiplier = 1.0 + getPercentExpForLevel(wisdomLevels) / 100.0;
        event.getExperienceOrb().setExperience((int) Math.ceil(multiplier * event.getExperienceOrb().getExperience()));
    }

}

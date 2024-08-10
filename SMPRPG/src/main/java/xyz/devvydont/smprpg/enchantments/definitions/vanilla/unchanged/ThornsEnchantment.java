package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class ThornsEnchantment extends UnchangedEnchantment implements Listener {

    public static int getReflectChance(int level) {
        return level * 15;
    }

    public static int getReflectDamage(int level) {
        return level * level * 20;
    }


    public ThornsEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Thorns");
    }

    @Override
    public Component getDescription() {
        return Component.text("Provides a ").color(NamedTextColor.GRAY)
                .append(Component.text(getReflectChance(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" chance to reflect ").color(NamedTextColor.GRAY))
                .append(Component.text(getReflectDamage(getLevel())).color(NamedTextColor.RED))
                .append(Component.text(" damage when hurt").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 26;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onThornsDamage(EntityDamageByEntityEvent event) {

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.THORNS))
            return;

        if (!(event.getDamager() instanceof LivingEntity living))
            return;

        int thornsLevel = EnchantmentUtil.getWornEnchantLevel(getEnchantment(), living.getEquipment());

        // This probably means it was a mob like the guardian
        if (thornsLevel <= 0) {
            event.setDamage(20);
        } else {
            event.setDamage(getReflectDamage(thornsLevel));
        }
    }
}

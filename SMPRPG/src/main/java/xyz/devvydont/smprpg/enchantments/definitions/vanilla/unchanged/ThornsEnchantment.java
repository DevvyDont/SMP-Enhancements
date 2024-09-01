package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

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
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Thorns");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Provides a "),
            ComponentUtils.create(getReflectChance(getLevel()) + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" chance to reflect "),
            ComponentUtils.create(String.valueOf(getReflectDamage(getLevel())), NamedTextColor.RED),
            ComponentUtils.create(" damage when hurt")
        );
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

        // If this is player on player, cancel it for the sake of PVE annoyance
        if (event.getEntity() instanceof Player && event.getDamageSource().getCausingEntity() instanceof Player) {
            event.setCancelled(true);
            return;
        }

        int thornsLevel = EnchantmentUtil.getWornEnchantLevel(getEnchantment(), living.getEquipment());

        // This probably means it was a mob like the guardian
        if (thornsLevel <= 0) {
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, 20);
        } else {
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, getReflectDamage(thornsLevel));
        }
    }
}

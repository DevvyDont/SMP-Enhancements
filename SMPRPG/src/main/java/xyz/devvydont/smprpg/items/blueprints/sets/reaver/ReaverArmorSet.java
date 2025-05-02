package xyz.devvydont.smprpg.items.blueprints.sets.reaver;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.Collection;
import java.util.List;

public abstract class ReaverArmorSet extends CustomArmorBlueprint implements IHeaderDescribable, IBreakableEquipment, Trimmable, Listener {

    public static final int POWER = 25;
    public static final int DURABILITY = 32_000;
    public static final int WITHER_RESIST = 20;

    public ReaverArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public int getWitherResistance() {
        return WITHER_RESIST;
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                ComponentUtils.EMPTY,
                AbilityUtil.getAbilityComponent("Necrotic (Passive)"),
                ComponentUtils.create("Resists ").append(ComponentUtils.create("-" + getWitherResistance() + "%", NamedTextColor.GREEN)).append(ComponentUtils.create(" of wither damage")),
                ComponentUtils.create("(stacks multiplicatively)", NamedTextColor.DARK_GRAY)
        );
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, getStrength()),
                new AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, .15)
        );
    }

    public abstract int getDefense();

    public abstract int getHealth();

    public abstract double getStrength();

    @Override
    public int getPowerRating() {
        return POWER;
    }

    @Override
    public int getMaxDurability() {
        return DURABILITY;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void __onTakeWitherDamageWhileWearing(EntityDamageEvent event) {

        var directEntityIsWitherType = event.getDamageSource().getCausingEntity() instanceof WitherSkeleton ||
                event.getDamageSource().getCausingEntity() instanceof Wither;
        var isWither = event.getCause().equals(EntityDamageEvent.DamageCause.WITHER) || directEntityIsWitherType;

        // Only listen to wither damage
        if (!isWither)
            return;

        // Check if we are wearing the set
        if (!(event.getEntity() instanceof Player player))
            return;

        // Loop through all armor pieces, if its either null, air, or not the type of this item we do not care
        boolean foundItem = false;
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item == null || item.getType().equals(Material.AIR))
                continue;

            // If this is our item, we want to continue w/ the damage reduction
            if (isItemOfType(item))
                foundItem = true;
        }

        if (!foundItem)
            return;

        // We are wearing the armor, decrease the damage
        double multiplier = 1 - (getWitherResistance()/100.0);
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, event.getDamage() * multiplier);
    }
}

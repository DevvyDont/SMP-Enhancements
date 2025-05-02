package xyz.devvydont.smprpg.items.blueprints.sets.reaver;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeHelmetBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.Collection;
import java.util.List;

public class ReaverHelmet extends CustomFakeHelmetBlueprint implements IHeaderDescribable, IBreakableEquipment, Listener {

    public ReaverHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                ComponentUtils.EMPTY,
                AbilityUtil.getAbilityComponent("Necrotic (Passive)"),
                ComponentUtils.create("Resists ").append(ComponentUtils.create("-" + ReaverArmorSet.WITHER_RESIST + "%", NamedTextColor.GREEN)).append(ComponentUtils.create(" of wither damage")),
                ComponentUtils.create("(stacks multiplicatively)", NamedTextColor.DARK_GRAY)
        );
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 45),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 5),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .25)
        );
    }

    @Override
    public int getPowerRating() {
        return ReaverArmorSet.POWER;
    }

    @Override
    public int getMaxDurability() {
        return ReaverArmorSet.DURABILITY;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTakeWitherDamageWhileWearing(EntityDamageEvent event) {

        // Only listen to wither damage
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.WITHER))
            return;

        // Check if we are wearing the set
        if (!(event.getEntity() instanceof Player player))
            return;

        // Loop through all armor pieces, if its either null, air, or not the type of this item we do not care
        ItemStack item = player.getInventory().getHelmet();
        if (item == null || item.getType().equals(Material.AIR) || !isItemOfType(item))
            return;

        // We are wearing the armor, decrease the damage
        double multiplier = 1 - (ReaverArmorSet.WITHER_RESIST / 100.0);
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, event.getDamage() * multiplier);
    }
}

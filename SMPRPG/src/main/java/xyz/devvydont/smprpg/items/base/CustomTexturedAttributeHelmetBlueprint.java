package xyz.devvydont.smprpg.items.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides.UnbreakingEnchantment;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents a combination of a custom fake textured skull item, and a helmet that can be worn.
 */
public abstract class CustomTexturedAttributeHelmetBlueprint extends CustomHeadBlueprint implements Attributeable, Sellable, Listener {

    public CustomTexturedAttributeHelmetBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getWorth() {
        return AttributeUtil.calculateValue(getPowerRating(), getDefaultRarity(), this instanceof Craftable);
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.BASE;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HEAD;
    }

    @Override
    public void applyModifiers(ItemMeta meta) {
        AttributeUtil.applyModifiers(this, meta);
    }

    @Override
    public AttributeModifierType.AttributeSession getAttributeSession(AttributeModifierType type, ItemMeta meta) {
        return type.session(this, meta);
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        // Append the attribute data just before the description of the item.
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Power Rating: ").color(NamedTextColor.GRAY).append(Component.text(Symbols.POWER + getTotalPower(meta)).color(NamedTextColor.YELLOW)));
        lore.add(Component.empty());

        lore.addAll(AttributeUtil.getAttributeLore(this, meta));
        lore.addAll(super.getDescriptionComponent(meta));
        return lore;
    }

    @Override
    public void updateMeta(ItemMeta meta) {

        // Before we can update the meta of this item, we need to fix its "vanilla" behavior
        applyModifiers(meta);
        super.updateMeta(meta);
    }

    /**
     * Sums the power rating of the item with any additional bonuses on it
     *
     * @param meta
     * @return
     */
    public int getTotalPower(ItemMeta meta) {
        return getPowerRating() + AttributeUtil.getPowerBonus(meta);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityWithstoodDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        ItemStack helmet = living.getEquipment().getHelmet();

        if (helmet == null || helmet.getType().equals(Material.AIR))
            return;

        if (!isItemOfType(helmet))
            return;

        if (helmet.getItemMeta().isUnbreakable())
            return;

        if (!(helmet.getItemMeta() instanceof Damageable damageable))
            return;

        int unbreakingLevel = helmet.getEnchantmentLevel(Enchantment.UNBREAKING);
        int unbreakingChance = UnbreakingEnchantment.getDurabilityIgnoreChance(unbreakingLevel);
        if (unbreakingLevel > 0 && Math.random() * 100 < unbreakingChance)
            return;

        if (damageable.getDamage() >= damageable.getMaxDamage()) {
            helmet.setAmount(0);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
            return;
        }

        damageable.setDamage(damageable.getDamage()+1);
        helmet.setItemMeta(damageable);
    }
}

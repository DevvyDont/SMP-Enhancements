package xyz.devvydont.smprpg.items.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.listeners.EntityDamageCalculatorService;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.List;

/*
 * A variant of a bow that can instantly shoot when used.
 */
public abstract class CustomShortbow extends CustomAttributeItem implements IHeaderDescribable, Listener {

    public CustomShortbow(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                AbilityUtil.getAbilityComponent("Shortbow (Left/Right Click)"),
                ComponentUtils.create("Instantly shoots arrows!")
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.SHORTBOW;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerInteractedWithShortbow(PlayerInteractEvent event) {

        // Is this a shortbow?
        ItemStack item = event.getItem();
        if (!isItemOfType(item))
            return;

        // Denies the vanilla usage of the bow. We are overriding the behavior.
        event.setUseItemInHand(Event.Result.DENY);

        // Are we clicking a block?
        if (event.getClickedBlock() != null)
            return;

        // Event should be cancelled no matter what if we aren't clicking a block.
        event.setCancelled(true);

        if (event.getHand() == null)
            return;

        // Are we on attack cooldown?
        if (event.getPlayer().getCooldown(getCustomItemType().DisplayMaterial) > 0)
            return;

        // Do we have an arrow to use as a consumable?
        ItemStack consumable = null;
        Class<? extends AbstractArrow> arrowClass = null;
        boolean shouldConsume = true;
        for (ItemStack inventoryItem : event.getPlayer().getInventory().getContents()) {
            if (inventoryItem == null)
                continue;

            if (inventoryItem.getType().equals(Material.ARROW)) {
                consumable = inventoryItem;
                arrowClass = Arrow.class;
                break;
            }

            if (inventoryItem.getType().equals(Material.SPECTRAL_ARROW)) {
                consumable = inventoryItem;
                arrowClass = SpectralArrow.class;
                break;
            }

        }

        // If the player is in creative mode, then we just use some fake arrow...
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
            if (consumable != null)
                consumable = consumable.clone();
            else {
                consumable = ItemService.generate(Material.ARROW);
                arrowClass = Arrow.class;
            }

        if (consumable == null) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_WORK_FLETCHER, 1.0f, 1.5f);
            SMPRPG.getService(ActionBarService.class).addActionBarComponent(event.getPlayer(), ActionBarService.ActionBarSource.MISC, ComponentUtils.create("NO ARROWS!", NamedTextColor.RED), 2);
            return;
        }

        // Now launch the arrow.
        event.setCancelled(true);
        AbstractArrow arrow = event.getPlayer().launchProjectile(arrowClass, event.getPlayer().getLocation().getDirection().normalize().multiply(EntityDamageCalculatorService.MAX_ARROW_DAMAGE_VELOCITY/20));
        arrow.setItemStack(consumable.asOne());
        arrow.setCritical(true);
        if (event.getItem().containsEnchantment(Enchantment.INFINITY))
            arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);

        // We should be good to shoot an arrow. Manually call the shoot bow event so plugins can modify it.
        // This will also cause the damage listener to set the damage as intended on the arrow so we don't have to that
        EntityShootBowEvent bowEvent = new EntityShootBowEvent(event.getPlayer(), item, consumable, arrow, event.getHand(), 1.0f, shouldConsume);
        bowEvent.callEvent();

        // If something cancelled the event, remove the arrow and cancel the interaction
        if (bowEvent.isCancelled()) {
            event.setCancelled(true);
            arrow.remove();
            return;
        }

        // Post processing effects that we call only when the event was successful.
        event.getPlayer().getWorld().playSound(event.getPlayer().getEyeLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1.5f);
        event.getPlayer().resetCooldown();
        int cooldown = (int) (20 / event.getPlayer().getAttribute(Attribute.ATTACK_SPEED).getValue());
        event.getPlayer().setCooldown(getCustomItemType().DisplayMaterial, cooldown);

        // If the arrow shot was a normal arrow and the bow has infinity, consumable logic is handled by
        // the infinity enchantment class.
        if (consumable.getType().equals(Material.ARROW) && event.getItem().containsEnchantment(Enchantment.INFINITY))
            return;


        // If the event flags this event as an instance where we should consume the item, we should do it
        if (bowEvent.shouldConsumeItem())
            consumable.setAmount(consumable.getAmount()-1);
    }
}

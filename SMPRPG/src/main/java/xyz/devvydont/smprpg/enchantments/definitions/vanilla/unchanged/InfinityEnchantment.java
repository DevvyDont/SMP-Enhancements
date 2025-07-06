package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class InfinityEnchantment extends VanillaEnchantment implements Listener {

    public static int getNonconsumeChance(int level) {
        return (level+1) * 9;
    }


    public InfinityEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Infinity");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Provides a "),
            ComponentUtils.create(getNonconsumeChance(getLevel()) + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" chance to not consume arrows")
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_BOW;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 10;
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
        return 45;
    }

    /*
     * We need to make vanilla infinity not work and function as a normal bow so that the next even can work for both
     * instances where a bow does and doesn't have infinity.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onShotBowWithInfinity(EntityShootBowEvent event) {

        // Only players can take advantage of infinity.
        if (!(event.getEntity() instanceof Player player))
            return;

        int infinityLevel = EnchantmentUtil.getHoldingEnchantLevel(getEnchantment(), event.getHand().getGroup(), event.getEntity().getEquipment());

        // If we don't have infinity, then we use vanilla's logic
        if (infinityLevel <= 0)
            return;

        // The arrow was not an arrow affected by infinity. Use vanilla's logic.
        if (event.getConsumable() == null)
            return;

        if (!event.getConsumable().getType().equals(Material.ARROW))
            return;

        // RNG for saving arrow. If this check evaluates to true, we use vanilla's logic for saving an arrow with infinity.
        if (getNonconsumeChance(infinityLevel) / 100.0 > Math.random())
            return;

        if (event.getBow() == null)
            return;

        if (!event.getBow().containsEnchantment(Enchantment.INFINITY))
            return;

        // Not sure when this should happen, but it is true even when we have infinity.
        if (!event.shouldConsumeItem() || event.getConsumable() == null)
            return;

        SMPItemBlueprint consumableBlueprint = SMPRPG.getService(ItemService.class).getBlueprint(event.getConsumable());

        // We shot a bow with infinity. Take away the consumable from the inventory.
        for (ItemStack item : player.getInventory().getContents()) {

            // No item in this inventory slot? don't care
            if (item == null)
                return;

            // Does the consumable blueprint have a match with this item?
            if (!consumableBlueprint.isItemOfType(item))
                continue;

            // We found an item match, decrease the amount of the item at this inventory slot by one
            item.setAmount(item.getAmount()-1);

            // If the projectile shot was an arrow, modify some attributes on it
            // We need to allow the arrow to be picked up again, and override the itemstack to be picked up from
            // the arrow object so that it stacks nicely back into our inventory.
            if (event.getProjectile() instanceof Arrow arrow) {
                arrow.setPickupStatus(AbstractArrow.PickupStatus.ALLOWED);
                ItemStack arrowItemStack = item.clone();
                arrowItemStack.setAmount(1);
                arrow.setItemStack(arrowItemStack);
            }
            break;
        }
    }
}

package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;

public class DensityEnchantment extends VanillaEnchantment implements Listener {

    public DensityEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    public static int getDamagePerBlock(int level) {
        return switch (level) {
          case 0 -> 0;
          case 1 -> 1;
          case 2 -> 3;
          case 3 -> 6;
          case 5 -> 10;
          default -> getDamagePerBlock(level) + (level-5)*5;
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Density");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases damage dealt by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getDamagePerBlock(getLevel())).color(NamedTextColor.GREEN))
                .append(Component.text(" per block fallen").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_MACE;
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
        return 2;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 60;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFallingMaceDamage(CustomEntityDamageByEntityEvent event) {

        if (!(event.getDealer() instanceof Player player))
            return;

        if (player.getFallDistance() <= 3.0)
            return;

        int density = EnchantmentUtil.getHoldingEnchantLevel(getEnchantment(), EquipmentSlotGroup.MAINHAND, player.getEquipment());
        if (density <= 0)
            return;

        int damage = getDamagePerBlock(density);
        event.setFinalDamage(event.getFinalDamage() + damage);
    }

    /**
     * A set of enchantments that this enchantment conflicts with.
     * If there are none, this enchantment has no conflicts
     *
     * @return
     */
    @NotNull
    public RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.BREACH);
    }
}

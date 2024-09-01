package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import static org.bukkit.Material.AIR;

public class MercyBlessing extends CustomEnchantment implements Listener {

    public static final int COOLDOWN = 180;

    public MercyBlessing(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Blessing of Mercy", NamedTextColor.YELLOW);
    }

    @Override
    public @NotNull TextColor getEnchantColor() {
        return NamedTextColor.YELLOW;
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Survive a "),
            ComponentUtils.create("fatal blow", NamedTextColor.DARK_PURPLE),
            ComponentUtils.create(" once every "),
            ComponentUtils.create(COOLDOWN + "s", NamedTextColor.GREEN)
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_CHEST_ARMOR;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getWeight() {
        return EnchantmentRarity.BLESSING.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.CHEST;
    }

    @Override
    public int getSkillRequirement() {
        return 30;
    }

    @Override
    public @NotNull RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentService.KEEPING_BLESSING.getTypedKey());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFatalDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player player))
            return;

        ItemStack chestplate = player.getEquipment().getChestplate();
        if (chestplate == null || chestplate.getType().equals(AIR))
            return;

        if (!chestplate.containsEnchantment(getEnchantment()))
            return;

        if (player.getCooldown(chestplate.getType()) > 0)
            return;

        // Invalid damage type?
        boolean invalid = switch (event.getCause()) {
            case CUSTOM, POISON, VOID, SUICIDE, KILL -> true;
            default -> false;
        };

        if (invalid)
            return;

        // If we have absorption we can't die yet
        if (player.getAbsorptionAmount() > 0)
            return;

        // Is this going to kill us?
        if (event.getDamage() < player.getHealth())
            return;

        event.setDamage(EntityDamageEvent.DamageModifier.BASE, player.getHealth()-1);
        player.setCooldown(chestplate.getType(), COOLDOWN*20);
        player.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, player.getEyeLocation(), 25);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*30, 4, true));
        SMPRPG.getInstance().getActionBarService().addActionBarComponent(player, ActionBarService.ActionBarSource.MISC, ComponentUtils.create("MERCY!", NamedTextColor.YELLOW), 3);
    }
}

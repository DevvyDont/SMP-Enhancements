package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.entity.interfaces.IDamageTrackable;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class FirstStrikeEnchantment extends CustomEnchantment implements Listener {

    public static int getFirstHitDamage(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 25;
            case 2 -> 40;
            case 3 -> 55;
            case 4 -> 75;
            case 5 -> 100;
            default -> getFirstHitDamage(5) + 20 * level;
        };
    }

    public FirstStrikeEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("First Strike");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases damage by "),
            ComponentUtils.create("+" + getFirstHitDamage(getLevel()) + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" when hitting an enemy for the first time")
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_WEAPON;
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
        return EnchantmentRarity.UNCOMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 7;
    }

    @Override
    public @NotNull RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentService.DOUBLE_TAP.getTypedKey());
    }

    @EventHandler(ignoreCancelled = true)
    public void onDealDamage(CustomEntityDamageByEntityEvent event) {

        if (!(event.getDealer() instanceof LivingEntity living) || living.getEquipment() == null)
            return;

        // Is this the first hit?
        if (!(SMPRPG.getService(EntityService.class).getEntityInstance(event.getDamaged()) instanceof IDamageTrackable trackable))
            return;

        int numHits = trackable.getDamageTracker().getNumberOfHitsDealtByEntity(event.getDealer());
        if (numHits > 0)
            return;

        // Retrieve the higher first strike level of the two hands to determine which one to use
        int firstStrikeLevels;
        int mainHandFSLevels = living.getEquipment().getItemInMainHand().getEnchantmentLevel(getEnchantment());
        int offHandFSLevels = living.getEquipment().getItemInOffHand().getEnchantmentLevel(getEnchantment());
        firstStrikeLevels = Math.max(mainHandFSLevels, offHandFSLevels);

        if (firstStrikeLevels <= 0)
            return;

        double multiplier = 1.0 + getFirstHitDamage(firstStrikeLevels) / 100.0;
        event.multiplyDamage(multiplier);
    }
}

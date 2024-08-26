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
import xyz.devvydont.smprpg.entity.base.EnemyEntity;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.services.EnchantmentService;

public class DoubleTapEnchantment extends CustomEnchantment implements Listener {

    public static int getFirstHitDamage(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 20;
            case 2 -> 35;
            case 3 -> 45;
            case 4 -> 65;
            case 5 -> 90;
            default -> getFirstHitDamage(5) + 20 * level;
        };
    }

    public DoubleTapEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Double Tap");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases damage by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getFirstHitDamage(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" when hitting an enemy for the first two times"));
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
        return 2;
    }

    @Override
    public @NotNull RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentService.FIRST_STRIKE.getTypedKey());
    }

    @EventHandler(ignoreCancelled = true)
    public void onDealDamage(CustomEntityDamageByEntityEvent event) {

        if (!(event.getDealer() instanceof LivingEntity living) || living.getEquipment() == null)
            return;

        // Is this the first/second hit?
        if (!(SMPRPG.getInstance().getEntityService().getEntityInstance(event.getDamaged()) instanceof EnemyEntity enemyEntity))
            return;

        int numHits = enemyEntity.getNumberOfHitsDealtByEntity(event.getDealer());
        if (numHits > 1)
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

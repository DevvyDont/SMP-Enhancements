package xyz.devvydont.smprpg.items.blueprints.vanilla;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.interfaces.IMace;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class ItemMace extends VanillaAttributeItem implements IBreakableEquipment, IMace, Listener {

    public static final int MACE_ATTACK_DAMAGE = 120;
    public static final double MACE_ATTACK_SPEED_DEBUFF = -0.85;

    public ItemMace(ItemService itemService, Material material) {
        super(itemService, material);
    }

    @Override
    public double getVelocityMultiplier() {
        return 1.0;
    }

    @Override
    public ItemRarity getDefaultRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MACE;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, MACE_ATTACK_DAMAGE),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, MACE_ATTACK_SPEED_DEBUFF)
        );
    }

    @Override
    public int getPowerRating() {
        return 30;
    }

    @Override
    public int getMaxDurability() {
        return 70_000;
    }

    /**
     * Maces have the special property of dealing increased damage based on how fast the person is falling.
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    private void __onMaceDamage(CustomEntityDamageByEntityEvent event) {

        // Can the damaging entity hold equipment?
        if (!(event.getDealer() instanceof LivingEntity livingEntity))
            return;

        var equipment = livingEntity.getEquipment();
        if (equipment == null)
            return;

        // Are they holding a mace?
        var item = equipment.getItemInMainHand();
        var blueprint = itemService.getBlueprint(item);
        if (!(blueprint instanceof IMace))
            return;


        // Falling?
        if (livingEntity.isOnGround())
            return;

        // Multiply damage by their downwards velocity.
        var downwardsVelocity = livingEntity.getVelocity().getY();
        Bukkit.broadcast(Component.text(downwardsVelocity));
    }
}

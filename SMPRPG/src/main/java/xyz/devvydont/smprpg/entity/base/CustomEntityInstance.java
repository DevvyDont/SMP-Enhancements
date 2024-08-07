package xyz.devvydont.smprpg.entity.base;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;

/**
 * Can be used for enemies that don't have any special functionality defined in CustomEntityType.
 * If a custom entity needs to listen and react to events, make a child class that extends this and implement
 * Listener
 */
public class CustomEntityInstance extends EnemyEntity {

    private final CustomEntityType entityType;

    public CustomEntityInstance(SMPRPG plugin, LivingEntity entity, CustomEntityType entityType) {
        super(plugin, entity);
        this.entityType = entityType;
    }

    public CustomEntityType getEntityType() {
        return entityType;
    }

    @Override
    public String getClassKey() {
        return entityType.key();
    }

    @Override
    public EntityType getDefaultEntityType() {
        return entityType.entityType;
    }

    @Override
    public String getDefaultName() {
        return entityType.name;
    }

    @Override
    public int getDefaultLevel() {
        return entityType.baseLevel;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return entityType.baseDamage;
    }

    @Override
    public void updateAttributes() {
        updateBaseAttribute(Attribute.GENERIC_MAX_HEALTH, entityType.baseHp);
        heal();
        updateBaseAttribute(Attribute.GENERIC_ATTACK_DAMAGE, calculateBaseAttackDamage());
    }

    public boolean isEntityOfType(Entity entity) {
        return getEntityType().isOfType(plugin.getEntityService(), entity);
    }

    public boolean isEntity(Entity entity) {
        return entity.equals(this.entity);
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }

    protected ItemStack getAttributelessItem(Material material) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.setAttributeModifiers(null);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new NamespacedKey("smprpg", "dummy-attribute"), 0.01, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY));
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(new NamespacedKey("smprpg", "dummy-attribute"), 0.01, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlotGroup.ANY));
        });
        plugin.getItemService().setIgnoreMetaUpdate(item);
        return item;
    }

    /**
     * Sets it so that this entity will never drop equipment.
     */
    protected void setNoDropEquipment() {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null)
            return;

        for (EquipmentSlot slot : EquipmentSlot.values())
            equipment.setDropChance(slot, 0f);
    }
}

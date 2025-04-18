package xyz.devvydont.smprpg.entity.base;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
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

    public CustomEntityInstance(SMPRPG plugin, Entity entity, CustomEntityType entityType) {
        super(plugin, entity);
        this.entityType = entityType;
    }

    @Override
    public void setup() {
        super.setup();
        setNoDropEquipment();
    }

    @Override
    public int getInvincibilityTicks() {
        return 0;
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
    public String getEntityName() {
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
        updateBaseAttribute(Attribute.MAX_HEALTH, entityType.baseHp);
        heal();
        updateBaseAttribute(Attribute.ATTACK_DAMAGE, calculateBaseAttackDamage());
    }

    public boolean isEntityOfType(Entity entity) {
        return getEntityType().isOfType(plugin.getEntityService(), entity);
    }

    public boolean isEntity(Entity entity) {
        return entity.equals(this.entity);
    }

    protected ItemStack getAttributelessItem(Material material) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.setAttributeModifiers(null);
            meta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(new NamespacedKey("smprpg", "dummy-attribute"), 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY));
            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(new NamespacedKey("smprpg", "dummy-attribute"), 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY));
        });
        plugin.getItemService().setIgnoreMetaUpdate(item);
        return item;
    }

    /**
     * Sets it so that this entity will never drop equipment.
     */
    protected void setNoDropEquipment() {

        if (!(entity instanceof LivingEntity living))
            return;

        EntityEquipment equipment = living.getEquipment();
        if (equipment == null)
            return;

        equipment.setItemInMainHandDropChance(0);
        equipment.setItemInOffHandDropChance(0);
        equipment.setHelmetDropChance(0);
        equipment.setChestplateDropChance(0);
        equipment.setLeggingsDropChance(0);
        equipment.setBootsDropChance(0);
    }

    /**
     * Sets it so that this entity has not equipment.
     */
    protected void removeEquipment() {

        if (!(entity instanceof LivingEntity living))
            return;

        EntityEquipment equipment = living.getEquipment();
        if (equipment == null)
            return;

        equipment.setItemInMainHand(null);
        equipment.setItemInOffHand(null);
        equipment.setHelmet(null);
        equipment.setChestplate(null);
        equipment.setLeggings(null);
        equipment.setBoots(null);
    }
}

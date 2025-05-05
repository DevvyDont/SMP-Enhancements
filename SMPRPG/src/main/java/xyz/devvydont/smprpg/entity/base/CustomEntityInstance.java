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
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.components.DamageTracker;
import xyz.devvydont.smprpg.entity.components.EntityConfiguration;
import xyz.devvydont.smprpg.entity.interfaces.IDamageTrackable;

/**
 * Can be used for enemies that don't have any special functionality defined in CustomEntityType.
 * If a custom entity needs to listen and react to events, make a child class that extends this and implement
 * Listener
 */
public class CustomEntityInstance<T extends Entity> extends LeveledEntity<T> implements IDamageTrackable {

    private final CustomEntityType entityType;
    private final DamageTracker _tracker = new DamageTracker();

    /**
     * An unsafe constructor to use to allow dynamic creation of custom entities.
     * This is specifically used as a casting hack for the CustomEntityType enum in order to dynamically create
     * entities.
     * @param entity The entity that should map the T type parameter.
     * @param entityType The entity type.
     */
    public CustomEntityInstance(Entity entity, CustomEntityType entityType) {
        super(entity);
        this.entityType = entityType;
    }

    @Override
    public DamageTracker getDamageTracker() {
        return _tracker;
    }

    @Override
    public void setup() {
        super.setup();
        setNoDropEquipment();
    }

    @Override
    public EntityConfiguration getDefaultConfiguration() {
        return EntityConfiguration.builder()
                .withLevel(entityType.getLevel())
                .withHealth(entityType.getHp())
                .withDamage(entityType.getDamage())
                .build();
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
        return entityType.Type;
    }

    @Override
    public String getEntityName() {
        return entityType.Name;
    }

    @Override
    public void updateAttributes() {
        updateBaseAttribute(AttributeWrapper.HEALTH, this._config.getBaseHealth());
        updateBaseAttribute(AttributeWrapper.STRENGTH, this._config.getBaseDamage());
        heal();
    }

    public boolean isEntityOfType(Entity entity) {
        return getEntityType().isOfType(_plugin.getEntityService(), entity);
    }

    public boolean isEntity(Entity entity) {
        return entity.equals(this._entity);
    }

    protected ItemStack getAttributelessItem(Material material) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.setAttributeModifiers(null);
            meta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(new NamespacedKey("smprpg", "dummy-attribute"), 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY));
            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(new NamespacedKey("smprpg", "dummy-attribute"), 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY));
        });
        _plugin.getItemService().setIgnoreMetaUpdate(item);
        return item;
    }

    /**
     * Sets it so that this entity will never drop equipment.
     */
    protected void setNoDropEquipment() {

        if (!(_entity instanceof LivingEntity living))
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

        if (!(_entity instanceof LivingEntity living))
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

package xyz.devvydont.smprpg.entity.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.LootSource;

import java.util.Collection;

public abstract class LeveledEntity implements LootSource {

    protected final SMPRPG plugin;
    protected final LivingEntity entity;

    public LeveledEntity(SMPRPG plugin, LivingEntity entity) {
        this.plugin =  plugin;
        this.entity = entity;
    }

    public void setup() {
        addPersistentEntityClassTag();
        updateAttributes();
        dimNametag();
        updateNametag();
    }

    public void addPersistentEntityClassTag() {
        // Tag this entity with its class key so it can be found later
        entity.getPersistentDataContainer().set(plugin.getEntityService().getClassNamespacedKey(), PersistentDataType.STRING, getClassKey());
    }

    public void dimNametag() {
        entity.setSneaking(true);
    }

    public void brightenNametag() {
        entity.setSneaking(false);
    }

    public Entity getEntity() {
        return entity;
    }

    public TextColor getEntityNametagColor() {
        return switch (entity) {
            case Boss boss -> NamedTextColor.DARK_PURPLE;
            case Enemy enemy -> NamedTextColor.RED;
            case Tameable tameable when tameable.isTamed() -> NamedTextColor.GREEN;
            case null, default -> NamedTextColor.WHITE;
        };
    }

    public static TextColor getChatColorFromHealth(double hp, double maxHp) {
        double percent = hp / maxHp;
        if (percent <= 0)
            return NamedTextColor.DARK_GRAY;
        else if (percent < .20)
            return NamedTextColor.DARK_RED;
        else if (percent < .50)
            return NamedTextColor.GOLD;
        else if (percent < .8)
            return NamedTextColor.YELLOW;
        else if (percent <= 1.0)
            return NamedTextColor.GREEN;
        else
            return NamedTextColor.AQUA;
    }

    public Component getNametagPowerComponent() {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text(Symbols.POWER + getLevel()).color(NamedTextColor.YELLOW))
                .append(Component.text("] ").color(NamedTextColor.GRAY));
    }

    public Component getDisplaynameNametagComponent() {
        return Component.text(getDefaultName()).color(getEntityNametagColor());
    }

    public Component getHealthNametagComponent() {

        int hp;
        if (getHp() <= 0)
            hp = 0;
        else if (getHp() > 0 && getHp() < 1)
            hp = 1;
        else
            hp = (int)getHp();
        int maxHp = (int)Math.round(getMaxHp());

        TextColor hpTextColor = getChatColorFromHealth(getHp(), getMaxHp());

        return Component.text(" " + hp).color(hpTextColor)
                .append(Component.text("/").color(NamedTextColor.GRAY))
                .append(Component.text(maxHp).color(NamedTextColor.GREEN))
                .append(Component.text(Symbols.HEART).color(NamedTextColor.DARK_RED));
    }

    /**✦❤
     * Default nametag behavior. Displays as [✦3] Zombie 100/100❤
     */
    public void updateNametag() {
        entity.setCustomNameVisible(true);
        entity.customName(getNametagPowerComponent().append(getDisplaynameNametagComponent()).append(getHealthNametagComponent()));
    }

    /**
     * Absorption health is calculated as 5% of max HP per absorption hitpoint
     *
     * @return
     */
    public double getAbsorptionHealth() {
        return entity.getAbsorptionAmount() * getHalfHeartValue();
    }

    /**
     * Converts absorption health to what it should be when setting it on the player
     *
     * @return
     */
    public void setAbsorptionHealth(double amount) {
        entity.setAbsorptionAmount(amount / getHalfHeartValue());
    }

    public void updateAbsorptionHealth(double amount) {
        double hp = getAbsorptionHealth();
        hp += amount;
        if (hp <= 0)
            hp = 0;
        setAbsorptionHealth(hp);
    }

    public double getHp() {
        return entity.getHealth() + getAbsorptionHealth();
    }

    public double getMaxHp() {
        return entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }

    /**
     * The current value of HP this player's half of heart is HP wise
     * This amount of HP is used a lot for damage such as fall damage, burning, and regeneration values
     *
     * @return
     */
    public double getHalfHeartValue() {
        return getMaxHp() / 20.0;
    }

    public double getRegenerationAmount(EntityRegainHealthEvent.RegainReason reason) {
        return getHalfHeartValue();
    }

    public abstract double calculateBaseAttackDamage();

    /**
     * Attempts to find the attack damage statistic stored on this entity. If not stored, we will calculate it ourselves.
     *
     * @return
     */
    public double getBaseAttackDamage() {
        AttributeInstance attack = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attack != null)
            return attack.getValue();

        return calculateBaseAttackDamage();
    }


    public void cleanup() {
    }


    public abstract String getClassKey();

    public abstract EntityType getDefaultEntityType();

    public abstract String getDefaultName();

    /**
     * Returns the default level of this entity given certain circumstances
     *
     * @return
     */
    public abstract int getDefaultLevel();

    /**
     * Returns the level set to this entity. If not present, default to whatever the set default is.
     *
     * @return
     */
    public int getLevel() {

        // If this entity doesn't have a level yet it must mean that we need to give them their default
        if (!entity.getPersistentDataContainer().has(plugin.getEntityService().getLevelNamespacedKey()))
            resetLevel();

        return entity.getPersistentDataContainer().getOrDefault(plugin.getEntityService().getLevelNamespacedKey(), PersistentDataType.INTEGER, getDefaultLevel());
    }

    /**
     * Sets the level stored on this entity. If you wish to update the entity's attributes as well, call
     * updateAttributes() afterward.
     *
     * @param level
     */
    public void setLevel(int level) {
        entity.getPersistentDataContainer().set(plugin.getEntityService().getLevelNamespacedKey(), PersistentDataType.INTEGER, level);
    }

    /**
     * Sets this entity's level to is defined default, same as LeveledEntity#setLevel(LeveledEntity#getDefaultLevel())
     */
    public void resetLevel() {
        setLevel(getDefaultLevel());
    }

    protected void updateBaseAttribute(Attribute attribute, double value) {

        AttributeInstance attrInstance = entity.getAttribute(attribute);
        if (attrInstance == null) {
            plugin.getLogger().fine(String.format("Tried to set %s attribute on %s, does not have it", attribute.name(), entity.getName()));
            return;
        }

        attrInstance.setBaseValue(value);
    }

    /**
     * Using getLevel(), set this entity's attributes
     */
    public abstract void updateAttributes();

    public void heal() {
        entity.setHealth(getMaxHp());
        updateNametag();
    }

    /**
     * Takes a look at the armor this entity is wearing and determines the defense of the items.
     * @return
     */
    public int getDefense() {

        if (!(getEntity() instanceof LivingEntity living))
            return 0;

        return (int) AttributeUtil.getAttributeValue(AttributeWrapper.DEFENSE.getAttribute(), living);
    }

    public double getHealthPercentage() {
        return getHp() / getMaxHp();
    }

    public void setHealthPercentage(double percentage) {
        entity.setHealth(getMaxHp() * Math.max(0, Math.min(1.0, percentage)));
    }

    public double getCombatExperienceMultiplier() {

        if (entity instanceof Boss)
            return 50;

        else if (entity instanceof Animals)
            return 1;

        else if (entity instanceof Creature)
            return 5;

        return 0;
    }

    public int getCombatExperienceDropped() {
        return (int) (getLevel() * getCombatExperienceMultiplier());
    }

    /**
     * Whether or not to consider vanilla drops on death
     *
     * @return
     */
    public boolean hasVanillaDrops() {
        return false;
    }

    /**
     * Returns a collection of loot drops for this entity. If null, assumes vanilla drop behavior.
     *
     * @return
     */
    @Nullable
    public Collection<LootDrop> getItemDrops() {
        return null;
    }

    /**
     * Component that displays in chat when a rare drop is obtained
     *
     * @return
     */
    @Override
    public Component getAsComponent() {
        return ComponentUtil.getDefaultText("defeating a(n) ").append(ComponentUtil.getColoredComponent(getDefaultName(), NamedTextColor.RED));
    }
}

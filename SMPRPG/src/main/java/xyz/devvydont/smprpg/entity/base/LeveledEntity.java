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
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.LootSource;

import java.util.Collection;

public abstract class LeveledEntity implements LootSource {

    protected final SMPRPG plugin;
    protected final Entity entity;

    public LeveledEntity(SMPRPG plugin, Entity entity) {
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
            case Tameable tameable when tameable.isTamed() -> NamedTextColor.AQUA;
            case Animals animals -> NamedTextColor.DARK_GREEN;
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
        if (getTotalHp() <= 0)
            hp = 0;
        else if (getTotalHp() > 0 && getTotalHp() < 1)
            hp = 1;
        else
            hp = (int) getTotalHp();
        int maxHp = (int)Math.round(getMaxHp());

        TextColor hpTextColor = getChatColorFromHealth(getTotalHp(), getMaxHp());

        return Component.text(" " + MinecraftStringUtils.formatNumber(hp)).color(hpTextColor)
                .append(Component.text("/").color(NamedTextColor.GRAY))
                .append(Component.text(MinecraftStringUtils.formatNumber(maxHp)).color(NamedTextColor.GREEN))
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
        if (entity instanceof LivingEntity living)
            return living.getAbsorptionAmount() * getHalfHeartValue();
        return 0;
    }

    /**
     * Converts absorption health to what it should be when setting it on the player
     *
     * @return
     */
    public void setAbsorptionHealth(double amount) {
        if (entity instanceof LivingEntity living)
            living.setAbsorptionAmount(amount / getHalfHeartValue());
    }

    public void updateAbsorptionHealth(double amount) {
        double hp = getAbsorptionHealth();
        hp += amount;
        if (hp <= 0)
            hp = 0;
        setAbsorptionHealth(hp);
    }

    public double getHp() {
        if (entity instanceof LivingEntity living)
            return living.getHealth();
        return 0;
    }

    public double getTotalHp() {
        if (entity instanceof LivingEntity living)
            return living.getHealth() + getAbsorptionHealth();
        return 0;
    }

    public double getMaxHp() {
        if (entity instanceof LivingEntity living)
            return living.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        return 1;
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

        if (!(entity instanceof LivingEntity living))
            return 0;

        AttributeInstance attack = living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
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

        if (!(entity instanceof LivingEntity living))
            return;

        AttributeInstance attrInstance = living.getAttribute(attribute);
        if (attrInstance == null) {
            living.registerAttribute(attribute);
            plugin.getLogger().fine(String.format("Tried to set %s attribute on %s, does not have it", attribute.name(), entity.getName()));
            attrInstance = living.getAttribute(attribute);
        }

        if (attrInstance != null)
            attrInstance.setBaseValue(value);
    }

    /**
     * Using getLevel(), set this entity's attributes
     */
    public abstract void updateAttributes();

    public void heal() {

        if (!(entity instanceof LivingEntity living))
            return;

        living.setHealth(getMaxHp());
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

        if (!(entity instanceof LivingEntity living))
            return;

        living.setHealth(getMaxHp() * Math.max(0, Math.min(1.0, percentage)));
    }

    /**
     * How much should we multiply the combat experience for this enemy by?
     *
     * @return
     */
    public double getCombatExperienceMultiplier() {

        if (entity instanceof Boss)
            return 75;

        else if (entity instanceof Animals)
            return .8;

        else if (entity instanceof Creature)
            return 3;

        return 0;
    }

    /**
     * How much combat experience should be awarded for killing this entity?
     *
     * @return
     */
    public int getCombatExperienceDropped() {
        return (int) (getLevel() * getCombatExperienceMultiplier());
    }

    /**
     * How much experience should we drop as vanilla experience orbs?
     *
     * @return
     */
    public int getMinecraftExperienceDropped() {
        return getLevel();
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

    /**
     * How much damage should be dealt to be considered for full loot/exp?
     *
     * @return
     */
    public double getDamageRatioRequirement() {
        return .5;
    }

    /**
     * Using the ratio requirement, calculate a number of exact damage we need to do
     *
     * @return
     */
    public int getDamageRequirement() {
        return (int) (getMaxHp() * getDamageRatioRequirement());
    }
}

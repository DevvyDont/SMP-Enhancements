package xyz.devvydont.smprpg.entity.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.EntityGlobals;
import xyz.devvydont.smprpg.entity.components.EntityConfiguration;
import xyz.devvydont.smprpg.listeners.EntityDamageCalculatorService;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.skills.utils.SkillExperienceReward;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.LootSource;

import java.util.Collection;

public abstract class LeveledEntity<T extends Entity> implements LootSource {

    protected final SMPRPG _plugin;
    protected final T _entity;
    private int _initialLevel;  // The level that was detected at setup time. Never changes after that.

    protected EntityConfiguration _config = EntityConfiguration.DEFAULT;

    public LeveledEntity(Entity entity) {
        this._plugin =  SMPRPG.getInstance();
        this._entity = (T) entity;
    }

    /**
     * Invincibility ticks are essentially how many ticks an entity is invulnerable for when taking damage.
     * Setting this to lower values allows entities to be hit at a much quicker rate.
     *
     * @return the amount of ticks to be invulnerable for when taking damage
     */
    public abstract int getInvincibilityTicks();

    /**
     * Class keys are the persistent data that we store on entities so that we can associate a Minecraft entity with
     * a class in our plugin. Gets the class key to store on an entity to persist entity types
     *
     * @return This entity's class key
     */
    public abstract String getClassKey();

    /**
     * Gets the default Minecraft entity type to associate with this class when initially spawning one.
     * This can be anything except a Player
     *
     * @return The vanilla Minecraft entity type to default to when spawning this entity
     */
    public abstract EntityType getDefaultEntityType();


    /**
     * Gets the entity name to display in the name tag.
     *
     * @return A string representing this entity's name.
     */
    public abstract String getEntityName();

    /**
     * Using getLevel(), set this entity's attributes
     */
    public abstract void updateAttributes();

    /**
     * Setup this entity. This is called any time an entity instance is attached to a vanilla Minecraft entity.
     */
    public void setup() {

        // Tag this entity class so we can construct it later.
        this.applyPersistentEntityClassTag();

        // Set the initial required configuration of this entity, and then stash its initial level.
        this.setConfiguration(this.getDefaultConfiguration());
        _initialLevel = getLevel();

        // Update the nametag.
        this.dimNametag();
        this.updateNametag();
        if (_entity instanceof LivingEntity living)
            living.setMaximumNoDamageTicks(this.getInvincibilityTicks());
    }

    /**
     * Called when this wrapper instance is being detached from the entity. This can either be due to chunk unloading,
     * the entity dying, or being transformed into another entity.
     */
    public void cleanup() {
    }

    /**
     * Spigot API hack to mimic the sneaking nametag behavior of players but on normal entities. This allows us
     * to effectively "turn off" the wallhacks you get by seeing entity nametags in caves below the world.
     */
    public void dimNametag() {
        _entity.setSneaking(true);
    }

    /**
     * Used to reverse the Spigot API hack of dimming nametags. Sets the nametag back to its default state of showing
     * like a normal nametag.
     */
    public void brightenNametag() {
        _entity.setSneaking(false);
    }

    public abstract EntityConfiguration getDefaultConfiguration();

    /**
     * Gets the entity instance this wrapper is attached to.
     * @return The entity.
     */
    public T getEntity() {
        return _entity;
    }

    public EntityConfiguration getConfiguration() {
        return _config;
    }

    public void setConfiguration(EntityConfiguration config) {
        this._config = config;
        this.updateAttributes();
    }

    /**
     * Tags the attached entity with the class tag defined in this class so that in the event of a chunk unload
     * and/or server restart, we can re-associate this entity back with its proper custom wrapper instance.
     */
    public void applyPersistentEntityClassTag() {
        // Tag this entity with its class key so it can be found later
        _entity.getPersistentDataContainer().set(EntityService.getClassNamespacedKey(), PersistentDataType.STRING, getClassKey());
    }

    /**
     * Default behavior for determining a name color for the name portion in the nametag.
     * @return The color of the name portion of the nametag.
     */
    public TextColor getNameColor() {
        return switch (_entity) {
            case Boss ignored -> NamedTextColor.DARK_PURPLE;
            case Enemy ignored -> NamedTextColor.RED;
            case Tameable tameable when tameable.isTamed() -> NamedTextColor.AQUA;
            case Animals ignored -> NamedTextColor.DARK_GREEN;
            case null, default -> NamedTextColor.WHITE;
        };
    }

    /**
     * Generates the bracketed power component to display in a nametag. This is usually the prefix
     * @return A Component of the current entity level
     */
    public Component getPowerComponent() {
        return ComponentUtils.powerLevelPrefix(getLevel());
    }

    /**
     * Generates the name portion of a nametag.
     * @return A component representing the name portion of a nametag
     */
    public Component getNameComponent() {
        return ComponentUtils.create(getEntityName(), getNameColor());
    }

    /**
     * Generates the health portion of a nametag based on the current health state of the entity.
     * @return A component representing the health of the entity
     */
    public Component getHealthComponent() {

        int hp;
        if (getTotalHp() <= 0)
            hp = 0;
        else if (getTotalHp() > 0 && getTotalHp() < 1)
            hp = 1;
        else
            hp = (int) Math.ceil(getTotalHp());

        var maxHp = (int) Math.ceil(getMaxHp());

        var hpTextColor = EntityGlobals.getChatColorFromHealth(getTotalHp(), getMaxHp());

        return ComponentUtils.merge(
            ComponentUtils.create(MinecraftStringUtils.formatNumber(hp), hpTextColor),
            ComponentUtils.create("/"),
            ComponentUtils.create(MinecraftStringUtils.formatNumber(maxHp), NamedTextColor.GREEN),
            ComponentUtils.create(Symbols.HEART, NamedTextColor.DARK_RED)
        );
    }

    /**
     * Returns the full component to render for this entity's nametag. Shows their power level, their name, and their
     * current health state.
     * @return A component to use as a name tag.
     */
    public Component getFullComponent() {
        return ComponentUtils.merge(
                getPowerComponent(), ComponentUtils.create(" "),
                getNameComponent(), ComponentUtils.create(" "),
                getHealthComponent()
        );
    }

    /**
     * Default nametag behavior. Displays as [✦3] Zombie 100/100❤
     */
    public void updateNametag() {
        _entity.setCustomNameVisible(true);
        _entity.customName(this.getFullComponent());
    }

    /**
     * Absorption health is calculated as 5% of max HP per absorption hit point
     * @return The current absorption health.
     */
    public double getAbsorptionHealth() {
        if (_entity instanceof LivingEntity living)
            return living.getAbsorptionAmount() * getHalfHeartValue();
        return 0;
    }

    /**
     * Converts absorption health to what it should be when setting it on the player
     *
     * @return
     */
    public void setAbsorptionHealth(double amount) {
        if (_entity instanceof LivingEntity living)
            living.setAbsorptionAmount(amount / getHalfHeartValue());
    }

    public void addAbsorptionHealth(double amount) {
        double hp = getAbsorptionHealth();
        hp += amount;
        if (hp <= 0)
            hp = 0;
        setAbsorptionHealth(hp);
    }

    /**
     * Gets this entity's health. This does not factor in absorption health, for TOTAL health use getTotalHp()
     * @return a double representing the entity's health
     */
    public double getHp() {
        if (_entity instanceof LivingEntity living)
            return living.getHealth();
        return 0;
    }

    /**
     * Get this entity's total health. This includes absorption health.
     * @return a double representing the entity's total amount of health
     */
    public double getTotalHp() {
        if (_entity instanceof LivingEntity living)
            return living.getHealth() + getAbsorptionHealth();
        return 0;
    }

    /**
     * Gets the entity's max health.
     * @return a double representing this entity's max health
     */
    public double getMaxHp() {
        if (_entity instanceof LivingEntity living)
            return living.getAttribute(Attribute.MAX_HEALTH).getValue();
        return 1;
    }

    /**
     * Calculates how much a "half heart" is for this entity. Things such as environmental damage/explosions need
     * to factor in a portion of max HP since health numbers can get pretty high
     *
     * @return
     */
    public double getHalfHeartValue() {
        return getMaxHp() / 20.0;
    }

    /**
     * Some entities can regenerate health in certain circumstances. Depending on the reason, we should adjust how
     * much health to regenerate while using the half heart value of this entity in most circumstances.
     *
     * @param reason The reason why this entity is regenerating health
     * @return How much health should be regenerated
     */
    public double getRegenerationAmount(EntityRegainHealthEvent.RegainReason reason) {

        // Regeneration is almost always based on their half heart value as a base.
        var amount = this.getHalfHeartValue();

        // Only living entities can do an attribute check.
        if (!(this._entity instanceof LivingEntity living))
            return amount;

        // Check if they have the regeneration attribute. If they don't, no biggie, just assume half heart.
        var regeneration = AttributeService.getInstance().getAttribute(living, AttributeWrapper.REGENERATION);
        if (regeneration == null)
            return amount;

        // Regeneration is a number representing the percentage of efficiency of regeneration. 100 = no change. 200 = double. 0 = nothing, etc.
        var multiplier = regeneration.getValue() / 100.0;
        return amount * multiplier;
    }

    /**
     * Checks if this entity has a level set in their PDC. If a level is not set, then calls to getLevel() will result
     * in the default level being returned. An entity with a persistent level set will be prevented from having their
     * stats recalculated when they load in the world.
     * @return true if this entity has a persistent level set.
     */
    protected boolean hasLevelSet() {
        return _entity.getPersistentDataContainer().has(EntityService.getLevelNamespacedKey(), PersistentDataType.INTEGER);
    }

    /**
     * Returns the level set to this entity. If not present, default to whatever the set default is.
     * @return an integer representing the current level of this entity
     */
    public int getLevel() {
        return _entity.getPersistentDataContainer().getOrDefault(EntityService.getLevelNamespacedKey(), PersistentDataType.INTEGER, this._config.getBaseLevel());
    }

    /**
     * Sets the level stored on this entity. The level you set will be persistent so that it will successfully
     * restore itself to its proper state on reloads.
     * @param level The level to set the entity to
     */
    public void setLevel(int level) {
        _entity.getPersistentDataContainer().set(EntityService.getLevelNamespacedKey(), PersistentDataType.INTEGER, level);

        // If the level changed, we should scale their stats to reflect it.
        if (_config.getBaseLevel() != level)
            this.setConfiguration(EntityConfiguration.scale(_config, level));
    }

    /**
     * Sets this entity's level to is defined default, same as LeveledEntity#setLevel(LeveledEntity#getDefaultLevel())
     */
    public void resetLevel() {
        setLevel(_initialLevel);
    }

    /**
     * Helper method to make it quicker to set the value of an attribute on an entity. Exception safe way to do this:
     *      entity.getAttribute(attribute).setBaseValue(value)
     * Also ensures that the entity will have the attribute registered to them if it is not already
     *
     * @param attribute The attribute to modify
     * @param value The value to set the base value of the attribute to
     */
    private void updateBaseAttribute(Attribute attribute, double value) {

        if (!(_entity instanceof LivingEntity living))
            return;

        AttributeInstance attrInstance = living.getAttribute(attribute);
        if (attrInstance == null) {
            living.registerAttribute(attribute);
            _plugin.getLogger().fine(String.format("Tried to set %s attribute on %s, does not have it", attribute.key().asMinimalString(), _entity.getName()));
            attrInstance = living.getAttribute(attribute);
        }

        if (attrInstance != null)
            attrInstance.setBaseValue(value);
    }

    /**
     * Helper method to make it quicker to set the value of an attribute on an entity.
     * Also ensures that the entity will have the attribute registered to them if it is not already
     * @param attribute The attribute to modify
     * @param value The value to set the base value of the attribute to
     */
    protected void updateBaseAttribute(AttributeWrapper attribute, double value) {

        // If this is a vanilla attribute, do it the vanilla way.
        if (attribute.isVanilla() && attribute.getWrappedAttribute() != null) {
            updateBaseAttribute(attribute.getWrappedAttribute(), value);
            return;
        }

        // Otherwise, do it the custom way. Only LivingEntity instances can have attributes.
        if (!(_entity instanceof LivingEntity target))
            return;

        // Retrieve the attribute, set the value, and apply the changes.
        var attrInstance = SMPRPG.getService(AttributeService.class).getOrCreateAttribute(target, attribute);
        attrInstance.setBaseValue(value);
        attrInstance.save(target, attribute);
    }

    /**
     * Helper method to heal the entity back to full HP.
     */
    public void heal() {

        if (!(_entity instanceof LivingEntity living))
            return;

        living.setHealth(getMaxHp());
        updateNametag();
    }

    /**
     * Calculates how much defense we have accumulated from effects. (Resistance potion effect)
     *
     * @return an integer of how much bonus defense this entity has. 0 if they don't have Resistance.
     */
    public int getDefenseFromEffects() {
        if (!(getEntity() instanceof LivingEntity living))
            return 0;

        PotionEffect resistance = living.getPotionEffect(PotionEffectType.RESISTANCE);
        if (resistance == null)
            return 0;

        int level = resistance.getAmplifier() + 1;

        double multiplier = EntityDamageCalculatorService.DEFENSE_PERCENT_PER_LEVEL_RESISTANCE_EFFECT * level / 100.0;
        return (int) (getBaseDefense() * multiplier);
    }

    /**
     * Retrieves the defense on this entity only based on gear that is equipped.
     *
     * @return an integer representing how much defense they have.
     */
    public double getBaseDefense() {

        if (!(getEntity() instanceof LivingEntity living))
            return 0;

        var defense = SMPRPG.getService(AttributeService.class).getAttribute(living, AttributeWrapper.DEFENSE);
        if (defense == null)
            return 0;

        return defense.getValue();
    }

    /**
     * Takes a look at the armor this entity is wearing and determines the defense of the items and effects we have.
     * @return an integer representing defense.
     */
    public int getDefense() {
        return Math.toIntExact(Math.round(getBaseDefense() + getDefenseFromEffects()));
    }

    public double getHealthPercentage() {
        return getHp() / getMaxHp();
    }

    public void setHealthPercentage(double percentage) {

        if (!(_entity instanceof LivingEntity living))
            return;

        living.setHealth(getMaxHp() * Math.max(0, Math.min(1.0, percentage)));
    }

    /**
     * How much should we multiply the skill experience for this enemy by?
     * @return A multiplier
     */
    public double getSkillExperienceMultiplier() {

        if (_entity instanceof Boss)
            return 20;

        else if (_entity instanceof Animals)
            return .25;

        else if (_entity instanceof Creature)
            return 1;

        return 0;
    }

    /**
     * Generate a mutable experience reward for killing this entity. This can safely be modified after calling without
     * the worry of stacking multiplication or add calls.
     * @return A fresh skill experience reward instance.
     */
    public SkillExperienceReward generateSkillExperienceReward() {
        return SkillExperienceReward.of(SkillType.COMBAT, (int) (getLevel() * getSkillExperienceMultiplier()));
    }

    /**
     * How much experience should we drop as vanilla experience orbs?
     *
     * @return
     */
    public int getMinecraftExperienceDropped() {
        return getLevel() * 3;
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
        return ComponentUtils.create("defeating a(n) ").append(ComponentUtils.create(getEntityName(), NamedTextColor.RED));
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

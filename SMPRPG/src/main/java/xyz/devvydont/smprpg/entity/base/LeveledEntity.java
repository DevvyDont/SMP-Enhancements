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
import xyz.devvydont.smprpg.listeners.EntityDamageCalculatorService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
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

    /**
     * Invincibility ticks are essentially how many ticks an entity is invulnerable for when taking damage.
     * Setting this to lower values allows entities to be hit at a much quicker rate.
     *
     * @return the amount of ticks to be invulnerable for when taking damage
     */
    public abstract int getInvincibilityTicks();

    /**
     * Calculates how much damage this entity should do when dealing damage.
     *
     * @return The damage this entity should do
     */
    public abstract double calculateBaseAttackDamage();

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
     * Returns the default level of this entity given certain circumstances
     *
     * @return an integer representing the default level of an entity type
     */
    public abstract int getDefaultLevel();


    /**
     * Using getLevel(), set this entity's attributes
     */
    public abstract void updateAttributes();

    /**
     * Setup this entity. This is called any time an entity instance is attached to a vanilla Minecraft entity.
     */
    public void setup() {
        addPersistentEntityClassTag();
        updateAttributes();
        dimNametag();
        updateNametag();
        if (entity instanceof LivingEntity living)
            setupLivingEnitity(living);
    }

    /**
     * Ran as a part of the setup phase but only if the entity attached to this instance is a LivingEntity.
     * Used to prevent many annoying isinstance checks since 99% of the entities that use this class will
     * be living entities, but there are a few exceptions we need to handle.
     *
     * @param living this.entity but safely casted to a LivingEntity instance.
     */
    public void setupLivingEnitity(LivingEntity living) {
        living.setMaximumNoDamageTicks(getInvincibilityTicks());
    }

    /**
     * Called when this wrapper instance is being detached from the entity. This can either be due to chunk unloading,
     * the entity dying, or being transformed into another entity.
     */
    public void cleanup() {
    }

    /**
     * Tags the attached entity with the class tag defined in this class so that in the event of a chunk unload
     * and/or server restart, we can re-associate this entity back with its proper custom wrapper instance.
     */
    public void addPersistentEntityClassTag() {
        // Tag this entity with its class key so it can be found later
        entity.getPersistentDataContainer().set(plugin.getEntityService().getClassNamespacedKey(), PersistentDataType.STRING, getClassKey());
    }

    /**
     * Spigot API hack to mimic the sneaking nametag behavior of players but on normal entities. This allows us
     * to effectively "turn off" the wallhacks you get by seeing entity nametags in caves below the world.
     */
    public void dimNametag() {
        entity.setSneaking(true);
    }

    /**
     * Used to reverse the Spigot API hack of dimming nametags. Sets the nametag back to its default state of showing
     * like a normal nametag.
     */
    public void brightenNametag() {
        entity.setSneaking(false);
    }


    public Entity getEntity() {
        return entity;
    }

    /**
     * Default behavior for determining a name color for the name portion in the nametag.
     *
     * @return
     */
    public TextColor determineNametagColor() {
        return switch (entity) {
            case Boss boss -> NamedTextColor.DARK_PURPLE;
            case Enemy enemy -> NamedTextColor.RED;
            case Tameable tameable when tameable.isTamed() -> NamedTextColor.AQUA;
            case Animals animals -> NamedTextColor.DARK_GREEN;
            case null, default -> NamedTextColor.WHITE;
        };
    }

    /**
     * Helper method to determine a color to associate with a certain health percentage given HP and max HP
     *
     * @param hp The HP of the entity
     * @param maxHp The max HP of an entity
     * @return A text color to associate with a certain percentage of health
     */
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

    /**
     * Generates the bracketed power component to display in a nametag. This is usually the prefix
     *
     * @return A Component of the current entity level
     */
    public Component generateNametagComponent() {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text(Symbols.POWER + getLevel()).color(NamedTextColor.YELLOW))
                .append(Component.text("] ").color(NamedTextColor.GRAY));
    }

    /**
     * Generates the name portion of a nametag.
     *
     * @return A component representing the name portion of a nametag
     */
    public Component getDisplaynameNametagComponent() {
        return Component.text(getEntityName()).color(determineNametagColor());
    }

    /**
     * Generates the health portion of a nametag based on the current health state of the entity.
     *
     * @return A component representing the health of the entity
     */
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
        entity.customName(generateNametagComponent().append(getDisplaynameNametagComponent()).append(getHealthNametagComponent()));
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

    /**
     * Gets this entity's health. This does not factor in absorption health, for TOTAL health use getTotalHp()
     *
     * @return a double representing the entity's health
     */
    public double getHp() {
        if (entity instanceof LivingEntity living)
            return living.getHealth();
        return 0;
    }

    /**
     * Get this entity's total health. This includes absorption health.
     *
     * @return a double representing the enitity's total amount of health
     */
    public double getTotalHp() {
        if (entity instanceof LivingEntity living)
            return living.getHealth() + getAbsorptionHealth();
        return 0;
    }

    /**
     * Gets the entity's max health.
     *
     * @return a double representing this entity's max health
     */
    public double getMaxHp() {
        if (entity instanceof LivingEntity living)
            return living.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
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
        return getHalfHeartValue();
    }



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

    /**
     * Returns the level set to this entity. If not present, default to whatever the set default is.
     *
     * @return an integer representing the current level of this entity
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
     * @param level The level to set the entity to
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

    /**
     * Helper method to make it quicker to set the value of an attribute on an entity. Exception safe way to do this:
     *      entity.getAttribute(attribute).setBaseValue(value)
     * Also ensures that the entity will have the attribute registered to them if it is not already
     *
     * @param attribute The attribute to modify
     * @param value The value to set the base value of the attribute to
     */
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
     * Helper method to heal the entity back to full HP.
     */
    public void heal() {

        if (!(entity instanceof LivingEntity living))
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
    public int getBaseDefense() {

        if (!(getEntity() instanceof LivingEntity living))
            return 0;

        return (int) AttributeUtil.getAttributeValue(AttributeWrapper.DEFENSE.getAttribute(), living);
    }

    /**
     * Takes a look at the armor this entity is wearing and determines the defense of the items and effects we have.
     * @return an integer representing defense.
     */
    public int getDefense() {
        return getBaseDefense() + getDefenseFromEffects();
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
        return ComponentUtils.getDefaultText("defeating a(n) ").append(ComponentUtils.getColoredComponent(getEntityName(), NamedTextColor.RED));
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

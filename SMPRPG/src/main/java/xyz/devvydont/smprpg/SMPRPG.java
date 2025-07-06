package xyz.devvydont.smprpg;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.effects.services.SpecialEffectService;
import xyz.devvydont.smprpg.listeners.*;
import xyz.devvydont.smprpg.loot.LootListener;
import xyz.devvydont.smprpg.services.*;
import xyz.devvydont.smprpg.util.animations.AnimationService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO:
 * - Implement POWER enchantment for bows in listeners.DamageOverrideListener
 * - Fix issue with bow damage stacking for normal melee damage/dual wielding bows
 */

public final class SMPRPG extends JavaPlugin {

    public static SMPRPG INSTANCE;

    public static SMPRPG getInstance() {
        return INSTANCE;
    }

    /**
     * A list of listeners that provide basic game mechanics that can be toggled on and off.
     */
    List<ToggleableListener> generalListeners = new ArrayList<>();

    /**
     * A list of services that are meant to provide core plugin functionality between other services.
     */
    List<IService> services = new ArrayList<>();

    /**
     * Sends a message to all online operators. This can be used as an alert if something somewhat serious happens.
     * @param alert The component to show to operators.
     */
    public static void broadcastToOperators(TextComponent alert) {
        Bukkit.getLogger().warning(alert.content());
        for (var player : Bukkit.getOnlinePlayers())
            if (player.isOp() || player.permissionValue("smprpg.receiveopmessages").toBooleanOrElse(false))
                player.sendMessage(ComponentUtils.alert(ComponentUtils.create("OP MSG", NamedTextColor.DARK_RED), alert));
    }

    /**
     * Sends a message to all online operators. This can be used as an alert if something somewhat serious happens.
     * The player parameter is a way to provide additional context by supplying a player who "caused" this message.
     * @param player The player that caused this interaction.
     * @param alert The component to show to operators.
     */
    public static void broadcastToOperatorsCausedBy(Player player, TextComponent alert) {
        Bukkit.getLogger().warning(alert.content());
        for (var op : Bukkit.getOnlinePlayers())
            if (op.isOp() || op.permissionValue("smprpg.receiveopmessages").toBooleanOrElse(false)) {
                op.sendMessage(ComponentUtils.alert(ComponentUtils.create("OP MSG", NamedTextColor.DARK_RED), ComponentUtils.create("(Caused by " + player.getName() +  ") ", NamedTextColor.RED).append(alert)));
            }
    }

    /**
     * The core method for cross plugin module communication.
     * Attempts to find a service that is of type clazz. They are programmatically guaranteed to exist.
     * If a service does not exist, a runtime exception will throw as this is a critical level programmer
     * error, as you should never reference services that don't exist.
     * @param clazz The class of the service you want.
     * @return The {@link IService} instance. If not found, an application exception throws.
     */
    public static @NotNull <T extends IService> T getService(Class<T> clazz) {
        for (var service : INSTANCE.getServices())
            if (clazz.isAssignableFrom(service.getClass()))
                return clazz.cast(service);

        INSTANCE.getLogger().severe("Service " + clazz.getName() + " not instantiated. Did you forget to create it?");
        throw new RuntimeException("Service " + clazz.getName() + " not instantiated?");
    }

    /**
     * Attempts to find a listener that is of type clazz. They are not guaranteed to exist.
     * @param clazz The class of the listener you want.
     * @return The {@link ToggleableListener} instance. If not found, returns null.
     */
    public static @Nullable <T extends ToggleableListener> T getListener(Class<T> clazz) {
        for (var listener : INSTANCE.getListeners())
            if (clazz.isAssignableFrom(listener.getClass()))
                return clazz.cast(listener);
        return null;
    }

    public Collection<IService> getServices() {
        return services;
    }

    public Collection<ToggleableListener> getListeners() {
        return generalListeners;
    }

    @Override
    public void onEnable() {

        INSTANCE = this;

        // Instantiate services. As a programmer, if you create a service class in the codebase you should have it
        // instantiated here no matter what to prevent runtime exceptions from nonexistent services.
        // Services are meant to be singleton instances that ALWAYS exist.
        services.add(new EconomyService());  // Allows transactions/money to work.
        services.add(new ChatService());  // Provides plugin with player display names and chat formatting.
        services.add(new EnchantmentService());  // Provides base enchantment functionality.
        services.add(new AttributeService());  // Provides custom attribute functionality.
        services.add(new EntityDamageCalculatorService());  // Logic for most damage calculations.
        services.add(new ItemService());  // Provides custom items and handlers for them.
        services.add(new EntityService());  // Provides just about anything entity related, attributes, stats, etc.
        services.add(new DifficultyService());  // Allows players to tweak their profile experience.
        services.add(new SpecialEffectService());  // Implements the "ailments" system.
        services.add(new SkillService());  // Logic for skills/skill experience for players.
        services.add(new DropsService());  // Implements the "drop protection" mechanic, as well as some other QoL.
        services.add(new ActionBarService());  // Broadcasts player information to player action bars.
        services.add(new UnstableListenersService());  // Implements some listeners that depend on ProtocolLib.
        services.add(new AnimationService());  // Mainly provides GUIs with an easy-to-use animation API.

        // Start all the services. Make sure nothing goes wrong.
        for (var service : services) {
            service.setup();
            getLogger().info(service.getClass().getSimpleName() + " service successfully enabled!");

            // If this service wants to listen to events, register it.
            // Keep in mind there's two cases here, one where a service wants to be toggleable and another where
            // the events should *always* fire, unless we manually unregister it the intended bukkit way.
            if (service instanceof ToggleableListener toggleableListener) {
                toggleableListener.start();
                getLogger().info(service.getClass().getSimpleName() + " is now listening to events and can be toggled on/off.");
            }
            else if (service instanceof Listener listener) {
                getServer().getPluginManager().registerEvents(listener, INSTANCE);
                getLogger().info(service.getClass().getSimpleName() + " is now listening to events.");
            }
        }

        // Initialize the general listeners that aren't core enough to be considered services.
        generalListeners.add(new EnvironmentalDamageListener());  // Scales environmental damage to be percentage based.
        generalListeners.add(new HealthScaleListener());  // Makes health scale update correctly.
        generalListeners.add(new HealthRegenerationListener());  // Scales HP regeneration based on max HP.
        generalListeners.add(new AbsorptionDamageFix());  // Makes absorption work correctly.
        generalListeners.add(new DimensionPortalLockingListener());  // Implements dimension requirements.
        generalListeners.add(new AnvilEnchantmentCombinationFixListener());  // Makes anvil combinations work.
        generalListeners.add(new PvPListener());  // Disables PVP in certain contexts.
        generalListeners.add(new StructureEntitySpawnListener());  // Allows entities to spawn as the level of the structure they're in.
        generalListeners.add(new LootListener());  // Overrides vanilla loot tables by injecting our items into it.

        // Start all of them.
        for (var listener : generalListeners)
            listener.start();
    }

    @Override
    public void onDisable() {
        for (var service : services)
            service.cleanup();
        for (var listener : generalListeners)
            listener.stop();
    }

}

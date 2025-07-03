package xyz.devvydont.smprpg.effects.services;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.effects.listeners.ShroudedEffectListener;
import xyz.devvydont.smprpg.effects.tasks.SpecialEffectTask;
import xyz.devvydont.smprpg.services.IService;

import java.util.*;

/*
 * Used across the plugin to give/remove/interact with special effects to apply on players.
 */
public class SpecialEffectService implements IService {


    private final Map<UUID, SpecialEffectTask> currentTasks = new HashMap<>();
    private final List<Listener> listeners = new ArrayList<>();

    SMPRPG plugin;

    public SpecialEffectService(SMPRPG plugin) {
        this.plugin = plugin;
    }

    private void registerListeners() {
        listeners.add(new ShroudedEffectListener(this));

        for (Listener listener : listeners)
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public boolean setup() {
        registerListeners();
        return true;
    }

    @Override
    public void cleanup() {
        for (Listener listener : listeners)
            HandlerList.unregisterAll(listener);

        listeners.clear();
    }

    @Override
    public boolean required() {
        return true;
    }

    /**
     * Query if a player has a special effect or not.
     *
     * @param player The player to check for an effect.
     * @return true if the player has a special effect, false if they don't.
     */
    public boolean hasEffect(Player player) {
        SpecialEffectTask task = currentTasks.get(player.getUniqueId());
        if (task == null)
            return false;

        return task.getSeconds() > 0;
    }

    /**
     * Used to give a player a special effect. This will remove the current effect if they already have one.
     * This method will handle all the processing of starting the tick timer and registering events.
     *
     * @param player The player to give an effect to
     * @param effect The effect to give them, simply just needs to be a new instance of an effect.
     */
    public void giveEffect(Player player, SpecialEffectTask effect) {

        // Remove if it is active
        removeEffect(player);

        // Create a task and run it every second and store it
        if (effect instanceof Listener listener)
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        effect.runTaskTimer(plugin, 0, SpecialEffectTask.PERIOD);
        currentTasks.put(player.getUniqueId(), effect);
    }

    /**
     * Removes an effect from the player if and only if the given class matches the effect that is currently applied
     * to the player. Useful for if you want to remove an effect from a player only if it is a certain type.
     *
     * @param player The player to remove an effect from.
     * @param effectClass The type of effect to remove
     */
    public void removeEffect(Player player, Class<? extends SpecialEffectTask> effectClass) {
        SpecialEffectTask effect = currentTasks.get(player.getUniqueId());
        if (effectClass.equals(effect.getClass()))
            removeEffect(player);
    }

    /*
     * Removes any special effects from a player regardless of what it is.
     */
    public void removeEffect(Player player) {
        removeEffect(player.getUniqueId());
    }

    /*
     * Removes any special effects from a player regardless of what it is.
     */
    public void removeEffect(UUID uuid) {

        SpecialEffectTask task = currentTasks.get(uuid);

        // They don't have an effect, don't do anything
        if (task == null)
            return;

        // Cancel the task, send an expired action bar, and remove the reference to the task
        task.cancel();
        task.removed();
        currentTasks.remove(uuid);
        task.sendActionBar(-1);
        if (task instanceof Listener listener)
            HandlerList.unregisterAll(listener);
    }



}

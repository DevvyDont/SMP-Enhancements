package xyz.devvydont.smprpg.services;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActionBarService implements BaseService, Listener {

    public static final int UPDATE_FREQUENCY = 5;

    public record ActionBarComponent(ActionBarSource source, Component display, long expiry){}

    /**
     * Works as a key so we can display multiple action bar things at the same time
     */
    public enum ActionBarSource {
        SKILL,
        STRUCTURE,
        AILMENT,
        MISC
    }

    private final SMPRPG plugin;

    private BukkitRunnable sendAllActionBarTask = null;
    private final Map<UUID, Map<ActionBarSource, ActionBarComponent>> components = new HashMap<>();

    public ActionBarService(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private Map<ActionBarSource, ActionBarComponent> getPlayerComponents(Player player) {
        Map<ActionBarSource, ActionBarComponent> playersComponents = this.components.get(player.getUniqueId());
        if (playersComponents == null) {
            this.components.put(player.getUniqueId(), new HashMap<>());
            playersComponents = this.components.get(player.getUniqueId());
        }
        return playersComponents;
    }

    /**
     * Adds a component to display to the player's action bar. Overwrites previous components of similar source.
     *
     * @param player
     * @param source
     * @param display
     * @param seconds
     */
    public void addActionBarComponent(Player player, ActionBarSource source, Component display, int seconds) {
        long expiry = System.currentTimeMillis() + seconds * 1000L;
        ActionBarComponent component = new ActionBarComponent(source, display, expiry);
        addActionBarComponent(player, component);
    }

    /**
     * Adds a component to display to the player's action bar. Overwrites previous components of similar source.
     *
     * @param player
     * @param component
     */
    public void addActionBarComponent(Player player, ActionBarComponent component) {
        Map<ActionBarSource, ActionBarComponent>componentMap = getPlayerComponents(player);
        componentMap.put(component.source(), component);
        display(player);
    }

    private Component getHealthComponent(final Player player) {

        LeveledPlayer leveledPlayer = plugin.getEntityService().getPlayerInstance(player);
        int hp = (int) Math.ceil(leveledPlayer.getTotalHp());
        int maxHP = (int) leveledPlayer.getMaxHp();
        TextColor color = LeveledEntity.getChatColorFromHealth(hp, maxHP);
        return ComponentUtils.getColoredComponent(hp + "", color)
                .append(ComponentUtils.getDefaultText("/"))
                .append(ComponentUtils.getColoredComponent(maxHP + "", NamedTextColor.GREEN))
                .append(ComponentUtils.getColoredComponent(Symbols.HEART, NamedTextColor.RED));
    }

    private Component getDefenseComponent(final Player player) {

        // We need to calculate defense since it is not storable on the player
        int def = plugin.getEntityService().getPlayerInstance(player).getDefense();
        return ComponentUtils.getColoredComponent(def + "", NamedTextColor.DARK_GREEN)
                .append(ComponentUtils.getColoredComponent(Symbols.SHIELD, NamedTextColor.GRAY));

    }

    private Component getPowerComponent(final Player player) {
        LeveledPlayer p = plugin.getEntityService().getPlayerInstance(player);
        return ComponentUtils.getBracketedPowerComponent(p.getLevel());
    }

    private void display(Player player) {

        // The component Will always have their health
        Component message = getPowerComponent(player).append(Component.text(" ")).append(getHealthComponent(player));

        // Check for components
        Map<ActionBarSource, ActionBarComponent> playersComponents = getPlayerComponents(player);

        // If we are displaying more than 1 extra component, omit defense
        if (playersComponents.size() <= 1)
            message = message.append(Component.text(" | ").color(NamedTextColor.GRAY)).append(getDefenseComponent(player));

        for (Map.Entry<ActionBarSource, ActionBarComponent> entry : playersComponents.entrySet())
            message = message.append(Component.text(" | ").color(NamedTextColor.GRAY)).append(entry.getValue().display());

        player.sendActionBar(message);
    }

    /**
     * Checks all components in the component map. If a component has expired, it removes it
     *
     * @param player
     */
    private void checkForExpiredComponents(Player player) {

        Map<ActionBarSource, ActionBarComponent> playersComponents = getPlayerComponents(player);
        for (ActionBarSource source : ActionBarSource.values())
            if (playersComponents.containsKey(source))
                if (playersComponents.get(source).expiry() < System.currentTimeMillis())
                    playersComponents.remove(source);
    }


    @Override
    public boolean setup() {

        if (sendAllActionBarTask != null)
            sendAllActionBarTask.cancel();

        sendAllActionBarTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    checkForExpiredComponents(player);
                    display(player);
                }
            }
        };
        sendAllActionBarTask.runTaskTimerAsynchronously(plugin, 1, UPDATE_FREQUENCY);
        return true;
    }

    @Override
    public void cleanup() {
        if (sendAllActionBarTask != null)
            sendAllActionBarTask.cancel();
    }

    @Override
    public boolean required() {
        return true;
    }

    /**
     * When a player quits, we no longer need to keep track of their components
     *
     * @param event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        components.remove(event.getPlayer().getUniqueId());
    }
}

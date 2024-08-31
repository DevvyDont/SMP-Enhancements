package xyz.devvydont.smprpg.entity.base;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.scoreboard.SimpleGlobalScoreboard;

import java.util.*;

public abstract class BossInstance extends EnemyEntity implements Listener {

    private record PlayerDamageEntry (Player player, int damage){}

    public static long INFINITE_TIME_LIMIT = Long.MAX_VALUE;

    // The bossbar attached to this boss to update whenever possible. It is possible this will be null.
    @Nullable protected BossBar bossBar = null;

    // The scoreboard wrapper to make text display much easier.
    private SimpleGlobalScoreboard scoreboard;

    // The task that is responsible for the AI/decisions that the entity makes based on certain conditions and
    // scoreboard updates.
    BukkitTask entityBrainTask = null;

    /*
     * The System.currentTimeMillis() timestamp that this boss should "wipe" at. A wipe will simply get rid of the boss
     * and kill all involved players with no loot rewards. If the timestamp is 0, it is either not set or we shouldn't
     * have this behavior.
     */
    private long wipeTimestamp = 0;

    /*
     * A collection of players who are ACTIVELY participating. Walking away and/or dying will remove them from this.
     * This is used to know who we should "wipe" in the event time runs out.
     */
    private final Map<UUID, Player> activelyInvolvedPlayers = new HashMap<>();

    public BossInstance(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    /*
     * Default time in seconds for how long this boss has to be defeated. Use INFINITE_TIME_LIMIT to have an infinite one.
     */
    public long getTimeLimit() {
        return INFINITE_TIME_LIMIT;
    }

    /*
     * A time limit does not start when the boss spawns. It starts when damage is first applied or when this method is
     * called.
     */
    public void startDefaultTimeLimit() {

        // If we have an unlimited time limit, set the timestamp to be never ending
        if (getTimeLimit() == INFINITE_TIME_LIMIT) {
            wipeTimestamp = INFINITE_TIME_LIMIT;
            return;
        }

        // If we do have a time limit already set, also don't do anything (this may change)
        if (wipeTimestamp != 0)
            return;

        // We do have a time limit, set the timestamp
        wipeTimestamp = System.currentTimeMillis() + (getTimeLimit() * 1000L);
    }

    /*
     * Call from elsewhere to manually add a time limit to a boss.
     */
    public void setManualTimeLimit(int seconds) {
        wipeTimestamp = System.currentTimeMillis() + (seconds * 1000L);
    }

    public int getSecondsLeft() {
        return (int) (wipeTimestamp - System.currentTimeMillis()) / 1000;
    }

    /*
     * Creates a boss bar that should be used for this boss. When implementing this method, null can be returned if
     * a custom boss bar is not needed. Ender Dragons and Withers by default already have boss bars in vanilla, so
     * these entities can have null boss bars.
     */
    @Nullable
    public abstract BossBar createBossBar();

    /*
     * Given how much time is left on the boss and the current server tick, considers playing a heartbeat.
     */
    public void considerHeartbeat() {

        // Too much time?
        int secondsLeft = getSecondsLeft();
        if (secondsLeft > 30)
            return;

        // Bad tick? only do even ticks if this is second 30 or less, then every tick in the final moments
        boolean isSecondTick = Bukkit.getServer().getCurrentTick() % 20 == 0;
        boolean isEvenTick = secondsLeft % 2 == 0;
        if (!isSecondTick)
            return;
        if (secondsLeft > 15 && !isEvenTick)
            return;
        if (secondsLeft < 0)
            return;

        // Play it
        for (Player player : getActivelyInvolvedPlayers())
            player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 1.5f, .7f);
    }

    /*
     * Method that runs every tick called by the entityBrainTask BukkitTask. Can be extended to add more functionality
     * beyond just simply updating the scoreboard.
     */
    public void tick() {

        if (!entity.isValid()) {
            cleanupBrainTickTask();
            return;
        }

        // If the boss bar is defined, update the progress and the name display
        if (bossBar != null) {
            Component name = entity.customName();
            if (name != null)
                bossBar.name(name);
            bossBar.progress((float) Math.clamp(getHealthPercentage(), 0f, 1f));
        }

        // We only need to update the scoreboard a couple times a second unless we are running low on time.
        int updateFreq = getSecondsLeft() > 60 ? 20 : 2;
        if (Bukkit.getCurrentTick() % updateFreq == 0)
            updateScoreboard();

        considerHeartbeat();

        // Are we out of time? (If a time limit is set)
        long now = System.currentTimeMillis();
        if (wipeTimestamp != 0 && now > wipeTimestamp)
            wipe();
    }

    /*
     * Gets all the players who are ACTIVELY participating who are currently online. Players that distance themselves
     * or die will be removed from this
     */
    public Collection<Player> getActivelyInvolvedPlayers() {
        return activelyInvolvedPlayers.values();
    }

    /*
     * Call this method to "wipe" the players involved and get rid of the boss. This is usually only called when
     * the timer expires and the players did not kill the boss in time.
     */
    public void wipe() {

        for (Player player : getActivelyInvolvedPlayers()) {
            // Kill everyone
            player.setHealth(0);
        }

        Bukkit.broadcast(ComponentUtils.getGenericMessage(
                ComponentUtils.getDefaultText("The "))
                .append(generateNametagComponent()).append(getDisplaynameNametagComponent())
                .append(ComponentUtils.getDefaultText(" has reigned victorious and wiped out those who challenged it."))
        );

        for (Player p : Bukkit.getOnlinePlayers())
            p.playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, .1f);

        cleanupBrainTickTask();
        entity.remove();
    }

    @Override
    public TextColor determineNametagColor() {
        return NamedTextColor.DARK_PURPLE;
    }

    @Override
    public int getInvincibilityTicks() {
        return 0;
    }

    private TextColor getPlaceColor(int rank) {
        return switch (rank) {
            case 1 -> NamedTextColor.YELLOW;
            case 2 -> NamedTextColor.WHITE;
            case 3 -> TextColor.color(0x65350F);  // Brown
            default -> NamedTextColor.GRAY;
        };
    }

    private String getPlaceth(int rank) {
        return switch (rank % 9) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    public List<Component> getRankingsComponents() {
        ArrayList<Component> rankings = new ArrayList<>();
        List<PlayerDamageEntry> entries = new ArrayList<>();
        for (Map.Entry<Player, Integer> entry : getPlayerDamageTracker().entrySet())
            entries.add(new PlayerDamageEntry(entry.getKey(), entry.getValue()));
        entries.sort((o1, o2) -> {
            if (o1.damage() == o2.damage())
                return 0;
            return o1.damage() > o2.damage() ? -1 : 1;
        });
        for (int i = 0; i < entries.size(); i++) {
            PlayerDamageEntry entry = entries.get(i);
            Player player = entry.player();
            int damage = entry.damage();
            int place = i + 1;
            rankings.add(
                    ComponentUtils.getColoredComponent(place + getPlaceth(place) + ": ", getPlaceColor(place))
                            .append(Component.text(plugin.getChatService().getPlayerDisplayname(player)))
                            .append(ComponentUtils.getDefaultText(" - "))
                            .append(ComponentUtils.getColoredComponent(MinecraftStringUtils.formatNumber(damage), NamedTextColor.RED))
                            .append(ComponentUtils.getColoredComponent(String.format(" (%d%%)", (int)(damage/getMaxHp()*100)), NamedTextColor.DARK_GRAY))
            );
        }
        return rankings;
    }

    private void updateScoreboard() {

        if (scoreboard == null) {
            cleanupScoreboard();
            return;
        }

        List<Component> lines = new ArrayList<>();

        // Add HP description, 3 lines
        lines.add(Component.empty());
        lines.add(ComponentUtils.getDefaultText("Boss Health: ").append(getHealthNametagComponent()));
        lines.add(Component.empty());

        // Add damage rankings (7 lines max!)
        List<Component> rankings = getRankingsComponents();
        lines.addAll(rankings.subList(0, Math.min(8, rankings.size())));

        // Add some information, 2 lines
        lines.add(Component.empty());
        lines.add(
                ComponentUtils.getDefaultText("Deal ")
                        .append(ComponentUtils.getColoredComponent(MinecraftStringUtils.formatNumber(getDamageRequirement()), NamedTextColor.RED))
                        .append(ComponentUtils.getDefaultText(" damage for "))
                        .append(ComponentUtils.getColoredComponent("MAX LOOT", NamedTextColor.LIGHT_PURPLE))
                        .append(ComponentUtils.getDefaultText("!"))
        );

        // If we have a time limit, add a wipe section
        int secondsLeft = Math.max(0, getSecondsLeft());
        long msLeft = wipeTimestamp-System.currentTimeMillis()-(secondsLeft*1000L);
        if (secondsLeft < 1000) {
            lines.add(Component.empty());

            // Color is always green by default
            NamedTextColor timeColor = NamedTextColor.GREEN;
            String timestring = String.format("%d:%02d", secondsLeft/60, secondsLeft%60);
            // If there is less than a minute left, we turn red.
            if (secondsLeft < 60) {
                timeColor = NamedTextColor.RED;
                timestring = String.format("%02d.%d", secondsLeft, msLeft/100);
            }

            // If there is less than 30 seconds left even seconds are red
            if (secondsLeft < 30 && secondsLeft % 2 == 1)
                timeColor = NamedTextColor.DARK_GRAY;

            // If there is less than 15 seconds half of a tick is one color and the other half is the other
            if (secondsLeft < 10)
                timeColor = Bukkit.getCurrentTick() % 20 >= 10 ? NamedTextColor.RED : NamedTextColor.DARK_GRAY;


            lines.add(ComponentUtils.getDefaultText("Time Left: ").append(Component.text(timestring, timeColor)));
        }

        // With default settings so far, we are using 12 lines. We have space for another 3 if desired
        scoreboard.setLines(lines);
    }

    @Override
    public void updateAttributes() {
        updateBaseAttribute(Attribute.GENERIC_ATTACK_DAMAGE, calculateBaseAttackDamage());
        updateBaseAttribute(Attribute.GENERIC_MAX_HEALTH, calculateBaseHealth());
    }

    public abstract double calculateBaseHealth();

    private Scoreboard cloneScoreboard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Scoreboard main = Bukkit.getScoreboardManager().getMainScoreboard();

        // Copy all the teams over
        for (Team team : main.getTeams()) {
            Team newTeam = scoreboard.registerNewTeam(team.getName());
            newTeam.prefix(team.prefix());
            if (team.hasColor())
                newTeam.color(NamedTextColor.nearestTo(team.color()));
            newTeam.suffix(team.suffix());

            for (String entry : team.getEntries())
                newTeam.addEntry(entry);
        }

        // Dupe scoreboard properties to this one
        for (Objective obj : main.getObjectives()) {
            Objective newObjective = scoreboard.registerNewObjective(obj.getName(), obj.getTrackedCriteria(), obj.displayName());
            newObjective.setDisplaySlot(obj.getDisplaySlot());
            newObjective.setRenderType(obj.getRenderType());
        }

        return scoreboard;
    }

    @Override
    public double getHalfHeartValue() {
        return Math.max(2, getMaxHp() / 300.0);
    }

    @Override
    public double getDamageRatioRequirement() {
        return .25;
    }

    @Override
    public int getMinecraftExperienceDropped() {
        return super.getMinecraftExperienceDropped() * 18;
    }

    @Override
    public double getCombatExperienceMultiplier() {
        return 40;
    }

    @Override
    public void setup() {
        super.setup();
        scoreboard = new SimpleGlobalScoreboard(cloneScoreboard(), generateNametagComponent().append(getDisplaynameNametagComponent()));
        heal();
        bossBar = createBossBar();
        cleanupBrainTickTask();
        entityBrainTask = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 1, 1);
    }

    @Override
    public void cleanup() {
        super.cleanup();

        cleanupBrainTickTask();
        cleanupScoreboard();
        if (bossBar != null)
            for (Player p : Bukkit.getOnlinePlayers())
                bossBar.removeViewer(p);
        bossBar = null;
    }

    private void cleanupScoreboard() {
        if (scoreboard == null)
            return;
        scoreboard.cleanup();
        scoreboard = null;
    }

    private void cleanupBrainTickTask() {
        if (entityBrainTask != null) {
            entityBrainTask.cancel();
            entityBrainTask = null;
        }
    }

    /*
     * When this boss dies, announce it to chat
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBossDeath(EntityDeathEvent event) {

        if (!entity.equals(event.getEntity()))
            return;

        cleanupBrainTickTask();

        if (getPlayerDamageTracker().isEmpty() || event.getEntity().getKiller() == null)
            return;

        Player player = event.getEntity().getKiller();

        // We died!!!
        Bukkit.broadcast(ComponentUtils.getDefaultText("-----------------------------"));
        Bukkit.broadcast(generateNametagComponent().append(getDisplaynameNametagComponent()).append(ComponentUtils.getColoredComponent(" Defeated!", determineNametagColor())));
        Bukkit.broadcast(Component.empty());
        Bukkit.broadcast(Component.text(plugin.getChatService().getPlayerDisplayname(player)).append(ComponentUtils.getDefaultText(" dealt the final blow!")));
        Bukkit.broadcast(Component.empty());
        for (Component component : getRankingsComponents())
            Bukkit.broadcast(component);
        Bukkit.broadcast(ComponentUtils.getDefaultText("-----------------------------"));

        for (Player p : Bukkit.getOnlinePlayers())
            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, .2f, 1);
    }

    /*
     * When a player damages this entity, set them as an involved player and set their scoreboard.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {

        if (!entity.equals(event.getEntity()))
            return;

        if (!(event.getDamageSource().getCausingEntity() instanceof Player player))
            return;

        if (scoreboard == null)
            return;

        activelyInvolvedPlayers.put(player.getUniqueId(), player);
        startDefaultTimeLimit();
        scoreboard.display(player);
        if (bossBar != null)
            bossBar.addViewer(player);
    }

    /*
     * When a player dies, remove them from this scoreboard and the involved players of this entity.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        activelyInvolvedPlayers.remove(event.getPlayer().getUniqueId());

        if (bossBar != null)
            bossBar.removeViewer(event.getPlayer());

        if (scoreboard == null)
            return;

        if (scoreboard.showing(event.getPlayer()))
            scoreboard.hide(event.getPlayer());

    }

    /*
     * If a player has moved 200 blocks away from this entity, remove them from the involved players.
     */
    @EventHandler
    public void onPlayerDistanced(PlayerMoveEvent event) {

        if (!event.getPlayer().getWorld().equals(entity.getWorld()))
            return;

        if (!event.hasChangedBlock())
            return;

        if (event.getPlayer().getLocation().distance(entity.getLocation()) < 200) {
            if (bossBar != null)
                bossBar.addViewer(event.getPlayer());
            return;
        }

        if (bossBar != null)
            bossBar.removeViewer(event.getPlayer());

        activelyInvolvedPlayers.remove(event.getPlayer().getUniqueId());

        if (scoreboard == null)
            return;

        if (!scoreboard.showing(event.getPlayer()))
            return;

        scoreboard.hide(event.getPlayer());
    }

    /*
     * If a player quits, remove them from the involved players.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        activelyInvolvedPlayers.remove(event.getPlayer().getUniqueId());
        if (bossBar != null)
            bossBar.removeViewer(event.getPlayer());
    }
}

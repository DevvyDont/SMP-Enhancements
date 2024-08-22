package xyz.devvydont.smprpg.entity.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.scoreboard.SimpleGlobalScoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BossInstance extends EnemyEntity implements Listener {

    private record PlayerDamageEntry (Player player, int damage){}

    private SimpleGlobalScoreboard scoreboard;
    BukkitTask scoreboardUpdateTask;

    public BossInstance(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
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
                    ComponentUtil.getColoredComponent(place + getPlaceth(place) + ": ", getPlaceColor(place))
                            .append(Component.text(plugin.getChatService().getPlayerDisplayname(player)))
                            .append(ComponentUtil.getDefaultText(" - "))
                            .append(ComponentUtil.getColoredComponent(MinecraftStringUtils.formatNumber(damage), NamedTextColor.RED))
                            .append(ComponentUtil.getColoredComponent(String.format(" (%d%%)", (int)(damage/getMaxHp()*100)), NamedTextColor.DARK_GRAY))
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
        lines.add(ComponentUtil.getDefaultText("Boss Health: ").append(getHealthNametagComponent()));
        lines.add(Component.empty());

        // Add damage rankings (7 lines max!)
        List<Component> rankings = getRankingsComponents();
        lines.addAll(rankings.subList(0, Math.min(7, rankings.size())));

        // Add some information, 2 lines
        lines.add(Component.empty());
        lines.add(
                ComponentUtil.getDefaultText("Deal ")
                        .append(ComponentUtil.getColoredComponent(MinecraftStringUtils.formatNumber(getDamageRequirement()), NamedTextColor.RED))
                        .append(ComponentUtil.getDefaultText(" damage for "))
                        .append(ComponentUtil.getColoredComponent("MAX LOOT", NamedTextColor.LIGHT_PURPLE))
                        .append(ComponentUtil.getDefaultText("!"))
        );

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
        return super.getMinecraftExperienceDropped() * 20;
    }

    @Override
    public double getCombatExperienceMultiplier() {
        return 40;
    }

    @Override
    public void setup() {
        super.setup();
        scoreboard = new SimpleGlobalScoreboard(cloneScoreboard(), generateNametagComponent().append(getDisplaynameNametagComponent()));
        scoreboardUpdateTask = SMPRPG.getInstance().getServer().getScheduler().runTaskTimer(SMPRPG.getInstance(), this::updateScoreboard, 0, 20L);
        heal();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        cleanupScoreboard();
    }

    private void cleanupScoreboard() {
        if (scoreboard == null)
            return;
        scoreboard.cleanup();
        scoreboardUpdateTask.cancel();
        scoreboard = null;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBossDeath(EntityDeathEvent event) {

        if (!entity.equals(event.getEntity()))
            return;

        if (getPlayerDamageTracker().isEmpty() || event.getEntity().getKiller() == null)
            return;

        Player player = event.getEntity().getKiller();

        // We died!!!
        Bukkit.broadcast(ComponentUtil.getDefaultText("-----------------------------"));
        Bukkit.broadcast(generateNametagComponent().append(getDisplaynameNametagComponent()).append(ComponentUtil.getColoredComponent(" Defeated!", determineNametagColor())));
        Bukkit.broadcast(Component.empty());
        Bukkit.broadcast(Component.text(plugin.getChatService().getPlayerDisplayname(player)).append(ComponentUtil.getDefaultText(" dealt the final blow!")));
        Bukkit.broadcast(Component.empty());
        for (Component component : getRankingsComponents())
            Bukkit.broadcast(component);
        Bukkit.broadcast(ComponentUtil.getDefaultText("-----------------------------"));

        for (Player p : Bukkit.getOnlinePlayers())
            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, .2f, 1);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {

        if (!entity.equals(event.getEntity()))
            return;

        if (!(event.getDamageSource().getCausingEntity() instanceof Player player))
            return;

        if (scoreboard == null)
            return;

        scoreboard.display(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!entity.equals(event.getEntity()))
            return;

        if (scoreboard == null)
            return;

        if (scoreboard.showing(event.getPlayer()))
            scoreboard.hide(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDistanced(PlayerMoveEvent event) {

        if (!event.getPlayer().getWorld().equals(entity.getWorld()))
            return;

        if (!event.hasChangedBlock())
            return;

        if (scoreboard == null)
            return;

        if (!scoreboard.showing(event.getPlayer()))
            return;

        if (event.getPlayer().getLocation().distance(entity.getLocation()) < 200)
            return;

        scoreboard.hide(event.getPlayer());
    }
}

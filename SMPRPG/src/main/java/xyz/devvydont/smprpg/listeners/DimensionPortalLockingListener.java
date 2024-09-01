package xyz.devvydont.smprpg.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.PortalType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DimensionPortalLockingListener implements Listener {

    private static Date generateDate(int month, int day, int year) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    public static final Date NETHER_LOCK = generateDate(7, 20, 2024);
    public static final Date END_LOCK = generateDate(8, 7, 2024);

    public static final int NETHER_SKILL_REQUIREMENT = 20;
    public static final int END_SKILL_REQUIREMENT = 36;

    public static final int MESSAGE_COOLDOWN = 1000;

    private final SMPRPG plugin;
    private final Map<UUID, Long> messageCooldown = new HashMap<>();

    public DimensionPortalLockingListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private String formatTimeDifference(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0)
            sb.append(days).append(" Days ");

        if (hours > 0 || days > 0)
            sb.append(hours).append(" Hours ");

        if (minutes > 0 || days > 0 || hours > 0)
            sb.append(minutes).append("m");

        sb.append(seconds).append("s");

        return sb.toString().trim();
    }

    private void sendSkillTooLowMessage(LeveledPlayer player, String dimension, int requirement) {

        // Don't do anything if we are on cooldown
        long now = System.currentTimeMillis();
        long cooldown = messageCooldown.getOrDefault(player.getPlayer().getUniqueId(), 0L);
        if (cooldown > now)
            return;

        messageCooldown.put(player.getPlayer().getUniqueId(), now + MESSAGE_COOLDOWN);
        player.getPlayer().sendMessage(ComponentUtils.error("You must have a Skill Average of " + requirement + " to enter the " + dimension + " dimension! You can check your progress by using /skill"));
    }

    private void sendTimeDiffMessage(Entity entity, String dimension, Date lockedUntil) {

        // Dont send messages to non-players
        if (!(entity instanceof Player))
            return;

        // Don't do anything if we are on cooldown
        long now = System.currentTimeMillis();
        long cooldown = messageCooldown.getOrDefault(entity.getUniqueId(), 0L);
        if (cooldown > now)
            return;

        messageCooldown.put(entity.getUniqueId(), now + MESSAGE_COOLDOWN);
        long diff = lockedUntil.getTime() - new Date().getTime();
        Component timeDiff = ComponentUtils.create(formatTimeDifference(Duration.of(diff, ChronoUnit.MILLIS)), NamedTextColor.DARK_RED);
        entity.sendMessage(ComponentUtils.error("You cannot enter the " + dimension + " dimension for another ").append(timeDiff));
    }

    @EventHandler
    public void onAttemptDimensionTeleport(EntityPortalEnterEvent event) {

        // Ignore if this is not a player
        if (!(event.getEntity() instanceof Player player))
            return;

        // If the player is in creative mode, allow a bypass
        if (player.getGameMode().equals(GameMode.CREATIVE))
            return;

        // Handle the portal we are entering
        PortalType portal = event.getPortalType();
        Date now = new Date();
        LeveledPlayer leveledPlayer = plugin.getEntityService().getPlayerInstance(player);

        // If this is a nether portal check the date
        if (portal.equals(PortalType.NETHER)) {

            if (now.before(NETHER_LOCK)) {
                event.setCancelled(true);
                sendTimeDiffMessage(event.getEntity(), "Nether", NETHER_LOCK);
                return;
            }

            // Too low?
            if (leveledPlayer.getAverageSkillLevel() < NETHER_SKILL_REQUIREMENT) {
                event.setCancelled(true);
                sendSkillTooLowMessage(leveledPlayer, "Nether", NETHER_SKILL_REQUIREMENT);
                return;
            }

            return;
        }

        if (portal.equals(PortalType.ENDER)) {

            if (now.before(END_LOCK)) {
                event.setCancelled(true);
                sendTimeDiffMessage(event.getEntity(), "End", END_LOCK);
                return;
            }

            // Too low?
            if (leveledPlayer.getAverageSkillLevel() < END_SKILL_REQUIREMENT) {
                event.setCancelled(true);
                sendSkillTooLowMessage(leveledPlayer, "End", END_SKILL_REQUIREMENT);
                return;
            }

        }
    }


}

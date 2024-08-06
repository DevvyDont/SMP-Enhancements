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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DimensionPortalLockingListener implements Listener {

    private static Date generateDate(int month, int day, int year) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    public static final Date NETHER_LOCK = generateDate(7, 15, 2024);
    public static final Date END_LOCK = generateDate(7, 20, 2024);

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
        Component timeDiff = Component.text(formatTimeDifference(Duration.of(diff, ChronoUnit.MILLIS)), NamedTextColor.DARK_RED);
        entity.sendMessage(Component.text("You cannot enter the " + dimension + " dimension for another ", NamedTextColor.RED).append(timeDiff));
    }

    @EventHandler
    public void onAttemptDimensionTeleport(EntityPortalEnterEvent event) {

        // If the player is in creative mode, allow a bypass
        if (event.getEntity() instanceof Player && ((Player) event.getEntity()).getGameMode().equals(GameMode.CREATIVE))
            return;

        // If the portal is not a nether or end portal, ignore
        PortalType portal = event.getPortalType();
        Date now = new Date();

        // If this is a nether portal check the date
        if (portal.equals(PortalType.NETHER)) {

            if (now.before(NETHER_LOCK)) {
                event.setCancelled(true);
                sendTimeDiffMessage(event.getEntity(), "Nether", NETHER_LOCK);
            }
            return;
        }

        if (portal.equals(PortalType.ENDER)) {
            if (now.before(END_LOCK)) {
                event.setCancelled(true);
                sendTimeDiffMessage(event.getEntity(), "End", END_LOCK);
            }
        }
    }


}

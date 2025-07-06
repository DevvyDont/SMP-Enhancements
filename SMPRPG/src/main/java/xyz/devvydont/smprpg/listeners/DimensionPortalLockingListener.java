package xyz.devvydont.smprpg.listeners;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.PortalType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.entity.player.ProfileDifficulty;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Intercepts dimension changing events and determines if we should allow it or not.
 * If you want dimensions to have requirements such as average skill thresholds, or time, then instantiate this listener.
 */
public class DimensionPortalLockingListener extends ToggleableListener {

    // When standing in a portal, the attempt transport event fires every tick. We need a cooldown for the message.
    public static final int MESSAGE_COOLDOWN = 1000;
    private final Map<UUID, Long> messageCooldown = new HashMap<>();

    /**
     * A container that defines what this dimension is locked behind.
     * @param Timelock The time at which this dimension is accessible.
     * @param Level The level needed to enter this dimension.
     */
    public record DimensionLock(Instant Timelock, int Level){}

    // The requirements to enter the nether.
    public static DimensionLock NETHER_LOCK = new DimensionLock(Instant.now(), 0);
    // The requirements to enter the end.
    public static DimensionLock END_LOCK = new DimensionLock(Instant.now(), 0);

    /**
     * Retrieve the requirement from the portal type.
     * @param portal The portal that is being used.
     * @return The lock that is associated with it. Returns null if it is always unlocked.
     */
    public static @Nullable DimensionLock fromPortal(PortalType portal) {
        return switch (portal){
            case NETHER -> NETHER_LOCK;
            case ENDER -> END_LOCK;
            default -> null;
        };
    }

    @Override
    public void start() {
        super.start();

        // Reload from config to initialize.
        try {
            reload();
        } catch (Exception e) {
            SMPRPG.getInstance().getLogger().severe("Failed to initialize dimension locks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reloads the timestamps and level requirements for the end and the nether from the config.
     */
    public void reload() {
        var cfg = SMPRPG.getInstance().getConfig();

        // Read the sections from the config we care about.
        var netherLvl = cfg.getInt("world_skill_unlocks.nether");
        var endLvl = cfg.getInt("world_skill_unlocks.end");
        var netherTime = cfg.getString("world_time_unlocks.nether");
        var endTime = cfg.getString("world_time_unlocks.end");

        // Check if anything went wrong.
        if (netherTime == null)
            throw new IllegalStateException("Failed to parse time unlock for the nether!");
        if (endTime == null)
            throw new IllegalStateException("Failed to parse time unlock for the end!");

        // Update.
        NETHER_LOCK = new DimensionLock(Instant.parse(netherTime), netherLvl);
        END_LOCK = new DimensionLock(Instant.parse(endTime), endLvl);
        SMPRPG.getInstance().getLogger().info("Reloaded dimension locks from config");
    }

    /**
     * Intercept the event when someone is attempting to teleport to a new dimension.
     * If they don't meet the requirements, stop them and let them know.
     * @param event The {@link EntityPortalEnterEvent} event that provides us with relevant context.
     */
    @EventHandler
    private void __onAttemptDimensionTeleport(EntityPortalEnterEvent event) {

        // For admin purposes, if this is someone in creative mode then allow it to happen.
        if (event.getEntity() instanceof Player player && player.getGameMode() == GameMode.CREATIVE)
            return;

        // Retrieve the lock. If there isn't one, we don't care.
        var lock = fromPortal(event.getPortalType());
        if (lock == null)
            return;

        // First, check the time. We don't want anything or anyone to get through if the dimension is locked by time.
        var now = Instant.now();
        if (now.isBefore(lock.Timelock)) {
            event.setCancelled(true);
            sendTimeDiffMessage(event.getEntity(), Duration.between(now, lock.Timelock));
            return;
        }

        // The dimension is unlocked timewise! Only players are relevant past this point.
        if (!(event.getEntity() instanceof Player player))
            return;

        // If this player is playing on easy, we don't check their skill and allow them to go.
        var wrapper = SMPRPG.getService(EntityService.class).getPlayerInstance(player);
        if (wrapper.getDifficulty() == ProfileDifficulty.EASY)
            return;

        // Is this player high enough skill?
        if (wrapper.getAverageSkillLevel() < lock.Level) {
            event.setCancelled(true);
            sendSkillTooLowMessage(wrapper, lock.Level);
            return;
        }

        // They are allowed to go! We don't need to do anything.
        SMPRPG.getInstance().getLogger().finest(String.format("%s has passed all checks for dimension travel using a %s portal.", player.getName(), event.getPortalType()));
    }

    /**
     * Given a time duration, format a clean string representation that is friendly for a player to view.
     * @param duration The time duration.
     * @return A formatted string.
     */
    private String formatTimeDifference(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        var sb = new StringBuilder();
        if (days > 0)
            sb.append(days).append(" Days ");

        if (hours > 0 || days > 0)
            sb.append(hours).append(" Hours ");

        if (minutes > 0 || days > 0 || hours > 0)
            sb.append(minutes).append("m");

        sb.append(seconds).append("s");

        return sb.toString().trim();
    }

    /**
     * Sends a message to the player that they are too low of a level to go there.
     * @param player The player that is too low.
     * @param requirement The requirement they need to go there.
     */
    private void sendSkillTooLowMessage(LeveledPlayer player, int requirement) {

        // Don't do anything if we are on cooldown
        long now = System.currentTimeMillis();
        long cooldown = messageCooldown.getOrDefault(player.getPlayer().getUniqueId(), 0L);
        if (cooldown > now)
            return;

        messageCooldown.put(player.getPlayer().getUniqueId(), now + MESSAGE_COOLDOWN);
        player.getPlayer().sendMessage(ComponentUtils.error(ComponentUtils.merge(
                ComponentUtils.create("You must have an average skill level of ", NamedTextColor.RED),
                ComponentUtils.create("" + requirement, NamedTextColor.DARK_RED),
                ComponentUtils.create(" to enter this portal. You average skill level is currently ", NamedTextColor.RED),
                ComponentUtils.create("" + (int)player.getAverageSkillLevel(), NamedTextColor.DARK_RED),
                ComponentUtils.create("!", NamedTextColor.RED)
        )));
    }

    /**
     * Sends a message to the player that this dimension is locked, and when it unlocks.
     * @param entity The entity that wants to receive the message.
     * @param lockedFor The time until the dimension unlocks.
     */
    private void sendTimeDiffMessage(Entity entity, Duration lockedFor) {

        // Dont send messages to non-players
        if (!(entity instanceof Player))
            return;

        // Don't do anything if we are on cooldown
        var now = System.currentTimeMillis();
        var cooldown = messageCooldown.getOrDefault(entity.getUniqueId(), 0L);
        if (cooldown > now)
            return;

        messageCooldown.put(entity.getUniqueId(), now + MESSAGE_COOLDOWN);
        var timeDiff = ComponentUtils.create(formatTimeDifference(lockedFor), NamedTextColor.DARK_RED);
        entity.sendMessage(ComponentUtils.error("This dimension is locked for another ").append(timeDiff));
    }


}

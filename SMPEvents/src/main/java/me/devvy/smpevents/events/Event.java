package me.devvy.smpevents.events;

import me.devvy.smpevents.SMPEvents;
import me.devvy.smpevents.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * An event that the members of a server participate in
 */
public class Event {

    public enum EventState {
        EDITING,  // The event was instantiated, but we are editing settings
        TICKING,  // The event has started counting down to start
        ONGOING  // Event has started and we are not accepting any more people
    }

    private EventState state = EventState.EDITING;
    private List<UUID> participants = new ArrayList<>();  // The people who decided to sign up
    private EventTimerTask timerTask;

    public Event() {
    }

    /**
     * Call to start the event countdown
     */
    public void countdown() {

        // Can only start countdown from edit state
        if (state != EventState.EDITING)
            return;

        if (timerTask != null)
            timerTask.cancel();

        state = EventState.TICKING;
        EventTimerTask timerTask = new EventTimerTask(this, 15);
        timerTask.runTaskTimer(SMPEvents.getInstance(), 1, EventTimerTask.PERIOD);
    }

    public EventState getState() {
        return state;
    }

    public List<UUID> getParticipants() {
        return participants;
    }

    public List<Player> getParticipantsAsPlayers() {
        List<Player> players = new ArrayList<>();
        for (UUID pid : getParticipants())
            if (Bukkit.getPlayer(pid) != null)
                players.add(Bukkit.getPlayer(pid));

        return players;
    }

    /**
     * Announces how long until the event starts and provides a button to join it
     *
     * @param timeleft What to display for the time left portion of the message
     */
    public void announce(String timeleft) {

        Bukkit.broadcast(
                ComponentUtil.getEventPrefix(ComponentUtil.YELLOW)
                        .append(Component.text("Event starting in ", ComponentUtil.GRAY))
                        .append(Component.text(timeleft, ComponentUtil.YELLOW, TextDecoration.BOLD))
                        .append(Component.text("! Use ", ComponentUtil.GRAY))
                        .append(Component.text("/event join ", ComponentUtil.GREEN))
                        .append(Component.text("to participate!", ComponentUtil.GRAY))
        );

    }

    /**
     * Have a player join the event, all we need to do is start tracking them and maybe send a message
     */
    public void join(Player player) {

        if (participants.contains(player.getUniqueId())) {
            player.sendMessage(Component.text("You already joined!", ComponentUtil.GREEN));
            return;
        }

        participants.add(player.getUniqueId());
        Bukkit.broadcast(
                ComponentUtil.getEventPrefix(ComponentUtil.YELLOW)
                        .append(player.displayName().color(ComponentUtil.YELLOW))
                        .append(Component.text(" joined the event!", ComponentUtil.GRAY))
        );
    }

    /**
     * Have a player leave the event, we need to handle cases differently if the event already started
     */
    public void leave(Player player) {

        if (!participants.contains(player.getUniqueId())) {
            player.sendMessage(Component.text("You haven't joined!", ComponentUtil.RED));
            return;
        }

        participants.remove(player.getUniqueId());
        Bukkit.broadcast(
                ComponentUtil.getEventPrefix(ComponentUtil.YELLOW)
                        .append(player.displayName().color(ComponentUtil.YELLOW))
                        .append(Component.text(" left the event!", ComponentUtil.RED))
        );

    }

    /**
     * Call to start the event, warp all players to event area, etc
     */
    public void start() {

        state = EventState.ONGOING;

        Bukkit.broadcast(
                ComponentUtil.getEventPrefix(ComponentUtil.YELLOW)
                        .append(Component.text("Event is starting now! ", ComponentUtil.YELLOW, TextDecoration.BOLD))
                        .append(Component.text("You can still spectate by using ", ComponentUtil.GRAY))
                        .append(Component.text("/event", ComponentUtil.GREEN))
                        .append(Component.text("!", ComponentUtil.GRAY))
        );

        for (Player player : getParticipantsAsPlayers())
            SMPEvents.getInstance().teleportToEventHub(player);
    }

    /**
     * Call to end event, do any cleanup needed and warp all players out
     */
    public void end() {
        Bukkit.broadcast(
                ComponentUtil.getEventPrefix(ComponentUtil.YELLOW)
                        .append(Component.text("Event is over! ", ComponentUtil.YELLOW, TextDecoration.BOLD))
                        .append(Component.text("Thank you for participating :) ", ComponentUtil.GRAY))
        );

        for (Player player : getParticipantsAsPlayers())
            SMPEvents.getInstance().getPlayerStateManager().unregisterPlayer(player);

        participants.clear();

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        state = EventState.EDITING;
    }

}

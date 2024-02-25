package me.devvy.smpevents.commands;

import me.devvy.smpevents.SMPEvents;
import me.devvy.smpevents.events.Event;
import me.devvy.smpevents.player.EventPlayer;
import me.devvy.smpevents.tasks.DelayEventWarpTask;
import me.devvy.smpevents.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventCommand implements CommandExecutor, TabCompleter {

    public static final String[] OP_SUBCOMMANDS = {"setspawn", "create", "cancel", "edit"};
    public static final String[] NORMAL_SUBCOMMANDS = {"current", "join", "leave"};

    public boolean startDelayTeleport(Player player, int delaySec) {

        // If they are already in event mode then tell them to use leave command instead
        if (SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(player.getUniqueId()) != null) {

            player.sendMessage(
                    ComponentUtil.getEventPrefix(ComponentUtil.RED)
                            .append(Component.text("You are already here! Use ", ComponentUtil.RED))
                            .append(Component.text("/event leave ", ComponentUtil.GREEN, TextDecoration.BOLD))
                            .append(Component.text("to go back!", ComponentUtil.RED))
            );
            player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        // Otherwise allow the teleport
        DelayEventWarpTask task = new DelayEventWarpTask(player, delaySec);
        task.runTaskTimer(SMPEvents.getInstance(), 1, DelayEventWarpTask.PERIOD);
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        // Ignore console
        if (!(commandSender instanceof Player))
            return true;

        Player sender = (Player) commandSender;

        // No args? just do the warp
        if (strings.length == 0)
            return startDelayTeleport(sender, sender.isOp() ? 1 : 7);

        // 1 arg?
        if (strings.length == 1) {

            String cmd = strings[0].toLowerCase();

            // Setting spawn? (OP command)
            if (sender.isOp() && cmd.equals("setspawn"))
                return handleSetSpawn(sender);

            // Creating? (OP command)
            if (sender.isOp() && cmd.equals("create"))
                return handleCreate(sender);

            // Canceling/Ending? (OP command)
            if (sender.isOp() && cmd.equals("cancel"))
                return handleCancel(sender);

            // Editing? (OP command)
            if (sender.isOp() && cmd.equals("edit"))
                return handleEdit(sender);

            // Leaving?
            if (cmd.equals("leave"))
                return handleLeave(sender);

            // Joining event?
            if (cmd.equals("join"))
                return handleJoin(sender);


        }


        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        List<String> validArgs = new ArrayList<>();

        if (strings.length == 0)
            return null;

        if (strings.length == 1) {

            String soFar = strings[0];
            List<String> availableCommands = new ArrayList<>(List.of(NORMAL_SUBCOMMANDS));
            if (commandSender.isOp())
                availableCommands.addAll(List.of(OP_SUBCOMMANDS));

            for (String availCmd : availableCommands)
                if (availCmd.toLowerCase().contains(soFar.toLowerCase()))
                    validArgs.add(availCmd);

            return validArgs;

        }

        return null;
    }

    public boolean handleSetSpawn(Player player) {

        SMPEvents.getInstance().getConfig().set(SMPEvents.CONFIG_EVENT_HUB_PATH, player.getLocation());
        SMPEvents.getInstance().saveConfig();

        player.sendMessage(
                Component.text("Set event hub spawnpoint!", TextColor.color(0, 200, 0))
        );
        player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1f, 1f);
        return true;
    }

    private boolean handleJoin(Player sender) {

        Event currentEvent = SMPEvents.getInstance().getEventManager().getCurrentEvent();

        // If there isn't an event show an error
        if (currentEvent == null) {
            sender.sendMessage(Component.text("There is no event to join!", ComponentUtil.RED));
            return true;
        }

        // If the event is not accepting players then we cannot join
        if (currentEvent.getState() == Event.EventState.ONGOING) {
            sender.sendMessage(Component.text("You cannot join an event in progress!", ComponentUtil.RED));
            return true;
        }

        currentEvent.join(sender);
        return true;
    }

    private boolean handleLeave(Player sender) {

        // We first check if we are trying to leave an event
        Event currentEvent = SMPEvents.getInstance().getEventManager().getCurrentEvent();
        if (currentEvent != null && currentEvent.getParticipants().contains(sender.getUniqueId())) {
            currentEvent.leave(sender);
            return true;
        }

        // If the player is not registered give them an error
        if (SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(sender.getUniqueId()) == null) {
            sender.sendMessage(
                    ComponentUtil.getEventPrefix(ComponentUtil.RED)
                            .append(Component.text("You are not at the event hub! Use ", ComponentUtil.RED))
                            .append(Component.text("/event ", ComponentUtil.GREEN, TextDecoration.BOLD))
                            .append(Component.text("to go there!", ComponentUtil.RED))
            );
            sender.playSound(sender.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        // If the player is playing a game tell them to finish it
        EventPlayer ep = SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(sender.getUniqueId());
        if (ep.isPlayingGame()) {
            sender.sendMessage(
                    ComponentUtil.getEventPrefix(ComponentUtil.RED)
                            .append(Component.text("You are participating in a game! Finish the game to leave! ", ComponentUtil.RED))
            );
            sender.playSound(sender.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        SMPEvents.getInstance().getPlayerStateManager().unregisterPlayer(sender);
        return true;
    }

    private boolean handleCreate(Player sender) {

        // Already existing event?
        Event currentEvent = SMPEvents.getInstance().getEventManager().getCurrentEvent();
        if (currentEvent != null) {
            sender.sendMessage(Component.text("There is currently an event happening", ComponentUtil.RED));
            return true;
        }

        boolean success = SMPEvents.getInstance().getEventManager().createEvent();

        if (!success) {
            sender.sendMessage(Component.text("Something went wrong creating this event", ComponentUtil.RED));
            return true;
        }

        currentEvent = SMPEvents.getInstance().getEventManager().getCurrentEvent();
        sender.sendMessage(Component.text("Event created!", ComponentUtil.GREEN));
        return true;
    }

    private boolean handleCancel(Player sender) {

        // No event to cancel?
        Event currentEvent = SMPEvents.getInstance().getEventManager().getCurrentEvent();
        if (currentEvent == null) {
            sender.sendMessage(Component.text("There is no event happening", ComponentUtil.RED));
            return true;
        }

        boolean success = SMPEvents.getInstance().getEventManager().stopEvent();

        if (!success) {
            sender.sendMessage(Component.text("Something went wrong ending this event", ComponentUtil.RED));
            return true;
        }

        sender.sendMessage(Component.text("canceled!", ComponentUtil.GREEN));
        return true;

    }

    private boolean handleEdit(Player sender) {

        // No event to edit?
        Event currentEvent = SMPEvents.getInstance().getEventManager().getCurrentEvent();
        if (currentEvent == null) {
            sender.sendMessage(Component.text("There is no event to edit", ComponentUtil.RED));
            return true;
        }

        if (currentEvent.getState() != Event.EventState.EDITING) {
            sender.sendMessage(Component.text("Cannot edit event while it started!", ComponentUtil.RED));
            return true;
        }

        // todo gui to edit the event, for now let us just start it

        boolean success = SMPEvents.getInstance().getEventManager().startTickingEvent();

        if (!success) {
            sender.sendMessage(Component.text("Something went wrong starting the countdown", ComponentUtil.RED));
            return true;
        }

        sender.sendMessage(Component.text("Started event!", ComponentUtil.GREEN));
        return true;

    }

}

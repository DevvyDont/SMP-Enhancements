package me.devvy.smpbuildworld.commands;

import me.devvy.smpbuildworld.SMPBuildWorld;
import me.devvy.smpbuildworld.tasks.DelayEventWarpTask;
import me.devvy.smpbuildworld.util.ComponentUtil;
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

public class BuildWorldCommand implements CommandExecutor, TabCompleter {

    public static final String[] OP_SUBCOMMANDS = {"wipe", "enable", "disable"};
    public static final String[] NORMAL_SUBCOMMANDS = {"warp", "leave", "create", "reset"};

    /**
     * Helper method to allow delayed teleports to build world
     *
     * @param player
     * @param location
     * @return
     */
    public boolean startDelayTeleport(Player player) {

        // If they are already at build world or build world is not on then return

        if (!SMPBuildWorld.getInstance().isBuildWorldEnabled()) {
            player.sendMessage(Component.text("BuildWorld is not enabled right now.", ComponentUtil.RED));
            return true;
        }

        if (SMPBuildWorld.getInstance().getPlayerManager().inBuildWorld(player)) {

            player.sendMessage(
                    ComponentUtil.getEventPrefix(ComponentUtil.RED)
                            .append(Component.text("You are already here! Use ", ComponentUtil.RED))
                            .append(Component.text("/buildworld leave ", ComponentUtil.GREEN, TextDecoration.BOLD))
                            .append(Component.text("to go back!", ComponentUtil.RED))
            );
            player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        if (DelayEventWarpTask.playerIsMidWarp(player)) {
            player.sendMessage(ComponentUtil.getEventPrefix(ComponentUtil.RED).append(Component.text("Slow down!", ComponentUtil.RED)));
            return true;
        }

        // Otherwise allow the teleport
        DelayEventWarpTask task = new DelayEventWarpTask(player);
        task.runTaskTimer(SMPBuildWorld.getInstance(), 1, DelayEventWarpTask.PERIOD);
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        // Ignore console
        if (!(commandSender instanceof Player))
            return true;

        Player sender = (Player) commandSender;

        // No args? display help
        if (strings.length == 0)
            return showHelp(sender);

        // 1+ args?
        String cmd = strings[0].toLowerCase();

        // Wiping? (OP command)
        if (sender.isOp() && cmd.equals("wipe"))
            return handleWipe(sender);

        // Enabling? (OP command)
        if (sender.isOp() && cmd.equals("enable"))
            return handleEnable(sender);

        // Disabling? (OP command)
        if (sender.isOp() && cmd.equals("disable"))
            return handleDisable(sender);

        // Warping?
        if (cmd.equals("warp"))
            return startDelayTeleport(sender);

        // Leaving?
        if (cmd.equals("leave"))
            return handleLeave(sender);// Leaving?

        // Resetting?
        if (cmd.equals("reset"))
            return handleReset(sender);

        // Creating?
        if (cmd.equals("create"))
            return handleCreate(sender);


        return false;
    }

    public boolean showHelp(Player player) {
        player.sendMessage("Help goes here");
        return true;
    }

    private boolean handleDisable(Player sender) {
        if (!SMPBuildWorld.getInstance().isBuildWorldEnabled()) {
            sender.sendMessage(
                    Component.text("BuildWorld is not enabled. Use ", ComponentUtil.RED)
                            .append(Component.text("/buildworld enable ", ComponentUtil.GREEN, TextDecoration.BOLD))
                            .append(Component.text("to enable it!", ComponentUtil.RED))
            );
            return true;
        }

        SMPBuildWorld.getInstance().disableBuildWorld();

        sender.sendMessage(Component.text("BuildWorld is now disabled!", ComponentUtil.GREEN));
        return true;
    }

    private boolean handleEnable(Player sender) {

        if (SMPBuildWorld.getInstance().isBuildWorldEnabled()) {
            sender.sendMessage(
                    Component.text("BuildWorld is already enabled. Use ", ComponentUtil.RED)
                            .append(Component.text("/buildworld disable ", ComponentUtil.GREEN, TextDecoration.BOLD))
                            .append(Component.text("to disable it!", ComponentUtil.RED))
            );
            return true;
        }

        SMPBuildWorld.getInstance().enableBuildWorld();

        sender.sendMessage(Component.text("BuildWorld is now enabled!", ComponentUtil.GREEN));
        return true;
    }

    private boolean handleWipe(Player sender) {
        sender.sendMessage("Wipe goes here");
        return true;
    }


    private boolean handleReset(Player sender) {
        sender.sendMessage("Reset goes here");
        return true;
    }


    private boolean handleCreate(Player sender) {
        sender.sendMessage("Create goes here");
        return true;
    }

    private boolean handleLeave(Player sender) {

        // If the player is not in build world don't do anything
        if (!SMPBuildWorld.getInstance().getPlayerManager().inBuildWorld(sender)){
            sender.sendMessage(
                    ComponentUtil.getEventPrefix(ComponentUtil.RED)
                            .append(Component.text("You are not at Build World! Use ", ComponentUtil.RED))
                            .append(Component.text("/buildworld warp ", ComponentUtil.GREEN, TextDecoration.BOLD))
                            .append(Component.text("to go there!", ComponentUtil.RED))
            );
            sender.playSound(sender.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return true;
        }

        // Let them go home
        SMPBuildWorld.getInstance().getPlayerManager().exitBuildWorld(sender);
        return true;
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












}

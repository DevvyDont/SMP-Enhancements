package me.devvy.smpmobarena.commands;

import me.devvy.smpmobarena.SMPMobArena;
import me.devvy.smpmobarena.config.ConfigManager;
import me.devvy.smpmobarena.events.PlayerAttemptJoinArenaActivePlayersEvent;
import me.devvy.smpmobarena.player.ArenaPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobArenaCommand implements CommandExecutor, TabCompleter {

    public static final String[] OP_SUBCOMMANDS = {"setlocation"};
    public static final String[] NORMAL_SUBCOMMANDS = {"join"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        // Ignore console
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;

        // No args? Tell them
        if (args.length == 0) {
            player.sendMessage(Component.text("Arguments needed!", NamedTextColor.RED));
            return true;
        }

        // 1 arg?
        if (args.length == 1) {

            String cmd = args[0].toLowerCase();

            // Setting arena location? (OP command)
            if (player.isOp() && cmd.equals("setlocation"))
                return handleSetLocation(player);

            // Joining arena? (Normal command)
            if (cmd.equals("join"))
                return handleJoin(player);

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> validArgs = new ArrayList<>();

        // If there are no args then they have nothing to tab complete
        if (args.length < 1)
            return validArgs;

        // If they are more than 1 arg, they are not tab completing
        if (args.length >= 2)
            return null;

        // Construct a list of valid subcommands based on if the sender is OP or not
        // If they are OP, they get access to all subcommands
        // If they are not, they only get access to the normal subcommands
        validArgs.addAll(Arrays.asList(NORMAL_SUBCOMMANDS));

        if (sender.isOp())
            validArgs.addAll(Arrays.asList(OP_SUBCOMMANDS));

        return validArgs;
    }

    private boolean handleSetLocation(Player sender) {

        ConfigManager.setArenaLocation(sender.getLocation());
        SMPMobArena.getInstance().saveConfig();

        sender.sendMessage(
                Component.text("Set mob arena origin!", NamedTextColor.GREEN)
        );
        sender.playSound(sender.getEyeLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1f, 1f);
        return true;

    }

    private boolean handleJoin(Player player) {

        ArenaPlayer ap = SMPMobArena.getInstance().getArena().getPlayerManager().getArenaPlayer(player.getUniqueId());
        if (ap == null) {
            player.sendMessage(Component.text("You are not in the arena!", NamedTextColor.RED));
            return true;
        }

        if (SMPMobArena.getInstance().getArena().getGameplayManager().isActivePlayer(ap)) {
            player.sendMessage(Component.text("You are already in the arena!", NamedTextColor.RED));
            return true;
        }

        // Attempt to join the arena as an active player
        PlayerAttemptJoinArenaActivePlayersEvent event = new PlayerAttemptJoinArenaActivePlayersEvent(ap);
        event.callEvent();
        if (event.isCancelled()) {
            player.sendMessage(Component.text("You cannot join the arena right now!", NamedTextColor.RED));
            return true;
        }

        return true;
    }
}

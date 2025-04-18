package xyz.devvydont.smprpg.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public abstract class PlayerCommandBase extends CommandBase {
    public PlayerCommandBase(String name) {
        super(name);
    }

    @Override
    public final void execute(@NotNull CommandSourceStack ctx, @NotNull String[] args) {
        var sender = ctx.getSender();
        if (sender instanceof Player player) {
            this.playerInvoked(player, ctx, args);
        } else {
            sender.sendMessage(ComponentUtils.error("You are the console! You cannot do this!"));
        }
    }

    protected abstract void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args);
}

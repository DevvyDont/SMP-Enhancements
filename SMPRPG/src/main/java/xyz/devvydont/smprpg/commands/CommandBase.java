package xyz.devvydont.smprpg.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;

import java.util.Collection;
import java.util.List;

public abstract class CommandBase implements BasicCommand {

    private String name;

    public CommandBase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return "No description was provided for this command";
    }

    public Collection<String> getAliases() {
        return List.of();
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        return BasicCommand.super.suggest(commandSourceStack, args);
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return BasicCommand.super.canUse(sender) || sender.isOp();
    }
}

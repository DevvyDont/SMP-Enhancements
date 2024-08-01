package xyz.devvydont.smprpg.commands.player;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.gui.InterfaceReforge;

public class CommandReforge extends CommandBase {

    public CommandReforge(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        if (!(commandSourceStack.getSender() instanceof Player player))
            return;

        new InterfaceReforge(SMPRPG.getInstance(), player).open();
    }
}

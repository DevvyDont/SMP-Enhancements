package xyz.devvydont.smprpg.commands.player;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.gui.player.MenuDifficultyChooser;

public class CommandDifficulty extends CommandBase {

    public CommandDifficulty(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, String @NotNull [] args) {
        if (!(commandSourceStack.getSender() instanceof Player player))
            return;
        
        new MenuDifficultyChooser(player).openMenu();
    }

    @Override
    public @Nullable String permission() {
        return super.permission();
    }
}

package xyz.devvydont.smprpg.commands.player;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.fishing.gui.LootTypeChancesMenu;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class CommandFishing extends PlayerCommandBase {

    public CommandFishing(String name) {
        super(name);
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {
        // Simple open the menu.
        new LootTypeChancesMenu(player).openMenu();
        player.sendMessage(ComponentUtils.success("Opened the fishing menu!"));
    }
}

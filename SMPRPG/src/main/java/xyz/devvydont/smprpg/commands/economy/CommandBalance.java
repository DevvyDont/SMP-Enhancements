package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class CommandBalance extends CommandBase {

    public CommandBalance(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        OfflinePlayer target;

        // If an argument was provided, assume we are querying another player's balance
        if (strings.length > 0) {

            // First try online players
            target = Bukkit.getPlayer(strings[0]);

            // If they're not online, try offline
            if (target == null) {
                target = Bukkit.getOfflinePlayerIfCached(strings[0]);
            }

            // If the target still isn't detected, incorrect argument was provided
            if (target == null) {
                commandSourceStack.getSender().sendMessage(ComponentUtils.getErrorMessage("Could not find player with name " + strings[0]));
                return;
            }
        }

        // No arguments provided and something that isn't a player is running this command, don't allow this
        else if (!(commandSourceStack.getSender() instanceof Player)) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.getErrorMessage("You are the console! You have infinite money!"));
            return;
        }

        // No arguments provided and the console is not sending this, someone is checking their own balance.
        else {
            target = (Player) commandSourceStack.getSender();
        }

        Component reply = Component.text(target.getName()).color(NamedTextColor.AQUA)
                .append(Component.text("'s balance is ").color(NamedTextColor.GRAY))
                .append(Component.text(SMPRPG.getInstance().getEconomyService().formatMoney(target)).color(NamedTextColor.GOLD));
        commandSourceStack.getSender().sendMessage(ComponentUtils.getGenericMessage(reply));
    }

}

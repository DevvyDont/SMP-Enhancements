package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;

public class CommandEcoAdmin extends CommandBase {

    public CommandEcoAdmin(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        if (args.length == 0) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.error("Valid subcommands: add | set | take"));
            return;
        }

        if (args.length == 1) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.error("Provide a player to target!"));
            return;
        }

        if (args.length == 2) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.error("Please provide an integer to modify the user's balance with!"));
            return;
        }

        String subcommand = args[0];
        String playerName = args[1];
        String rawNumber = args[2];

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(playerName);
        if (target == null) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.error("Player not found! They need to join the server at least once to modify their currency."));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(rawNumber);
        } catch (NumberFormatException e) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.error(rawNumber + " is not a valid integer input. Please try again!"));
            return;
        }

        var economyService = SMPRPG.getService(EconomyService.class);
        boolean success = false;
        if (subcommand.equalsIgnoreCase("add"))
            success = economyService.addMoney(target, amount);
        else if (subcommand.equalsIgnoreCase("take"))
            success = economyService.takeMoney(target, amount);
        else if (subcommand.equalsIgnoreCase("set")) {

            int balance = economyService.getMoney(target);
            if (balance == amount) {
                commandSourceStack.getSender().sendMessage(ComponentUtils.error(target.getName() + " aleady has a balance of " + EconomyService.formatMoney(amount)));
                return;
            }

            // Need more money? Add the difference
            if (balance < amount)
                success = economyService.addMoney(target, amount-balance);
            else
                success = economyService.takeMoney(target, balance-amount);
        } else {
            commandSourceStack.getSender().sendMessage(ComponentUtils.error("Unknown subcommand! add | set | take"));
            return;
        }

        if (success)
            commandSourceStack.getSender().sendMessage(ComponentUtils.success("Success! " + target.getName() + " now has a balance of " + EconomyService.formatMoney(economyService.getMoney(target))));
        else
            commandSourceStack.getSender().sendMessage(ComponentUtils.error("Failed! " + target.getName() + " probably has insufficient funds or that amount of money is not allowed. Balance is " + EconomyService.formatMoney(economyService.getMoney(target))));

    }

    @Override
    protected Collection<String> determineSuggestions(int argumentIndex, String input) {

        // Subcommand
        if (argumentIndex == 0)
            return generateArgumentCollection(input, "set", "add", "take");

        // Amount
        if (argumentIndex == 2)
            return generateArgumentCollection(input, "10", "25", "50", "100", "1000", "5000", "9999", "75000");

        return super.determineSuggestions(argumentIndex, input);
    }

    @Override
    public @Nullable String permission() {
        return "smprpg.command.eco";
    }
}

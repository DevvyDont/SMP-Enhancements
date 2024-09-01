package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.*;

public class CommandBalanceTop extends CommandBase {

    private record PlayerBalanceEntry(OfflinePlayer player, int balance) {
    }

    public CommandBalanceTop(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        CommandSender commandSender = commandSourceStack.getSender();

        commandSender.sendMessage(ComponentUtils.alert("Querying users..."));

        new BukkitRunnable(){

            @Override
            public void run() {
                // Create the title UI
                var output = ComponentUtils.merge(
                    ComponentUtils.create("------------ "),
                    ComponentUtils.create("Top Balances", NamedTextColor.GOLD),
                    ComponentUtils.create(" ------------\n\n")
                );

                // Retrieve every player that has ever played on the server
                Map<UUID, PlayerBalanceEntry> allPlayers = new HashMap<>();
                for (OfflinePlayer p : Bukkit.getOfflinePlayers())
                    allPlayers.put(p.getUniqueId(), new PlayerBalanceEntry(p, SMPRPG.getInstance().getEconomyService().getMoney(p)));
                for (Player p : Bukkit.getOnlinePlayers())
                    allPlayers.put(p.getUniqueId(), new PlayerBalanceEntry(p, SMPRPG.getInstance().getEconomyService().getMoney(p)));

                // Construct a sortable list of entries containing player information including their current balance
                List<PlayerBalanceEntry> listOfPlayerBalances = new ArrayList<>(allPlayers.values().stream().toList());
                listOfPlayerBalances.sort((o1, o2) -> {
                    if (o1.balance() == o2.balance())
                        return 0;
                    return o1.balance() > o2.balance() ? -1 : 1;
                });

                // Sum all entries balances for a server total
                long sum = 0;
                for (PlayerBalanceEntry entry : listOfPlayerBalances)
                    sum += entry.balance;

                // Display the total economy
                output = output.append(ComponentUtils.merge(
                    ComponentUtils.create("Total Server Economy: ", NamedTextColor.RED, TextDecoration.BOLD),
                    ComponentUtils.create(EconomyService.formatMoney(sum), NamedTextColor.GOLD),
                    ComponentUtils.create("\n\n")
                ));

                // Display all the entries
                int rank = 1;
                for (PlayerBalanceEntry entry : listOfPlayerBalances) {
                    // Don't show past #10
                    if (rank > 10)
                        break;

                    String legacyName = SMPRPG.getInstance().getChatService().getPlayerDisplayname(entry.player);
                    output = output.append(ComponentUtils.merge(
                            ComponentUtils.create(String.format("#%d: ", rank), NamedTextColor.AQUA, TextDecoration.ITALIC),
                            ComponentUtils.create(legacyName),
                            ComponentUtils.create(" - "),
                            ComponentUtils.create(EconomyService.formatMoney(entry.balance), NamedTextColor.GOLD),
                            ComponentUtils.create("\n")
                    ));
                    rank++;
                }

                // Display the UI
                output = output.append(ComponentUtils.create("\n-------------------------------------"));
                commandSender.sendMessage(output);
            }
        }.runTaskAsynchronously(SMPRPG.getInstance());

    }

}

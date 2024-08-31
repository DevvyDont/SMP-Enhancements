package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
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

        commandSender.sendMessage(ComponentUtils.alert(Component.text("Querying users...").color(NamedTextColor.GRAY)));

        new BukkitRunnable(){

            @Override
            public void run() {
                Component component = Component.text("------------ ").color(NamedTextColor.GRAY);
                component = component.append(Component.text("Top Balances").color(NamedTextColor.GOLD));
                component = component.append(Component.text(" ------------\n\n").color(NamedTextColor.GRAY));

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

                component = component.append(Component.text("Total Server Economy: ").style(Style.style(NamedTextColor.RED, TextDecoration.BOLD)));
                component = component.append(Component.text(EconomyService.formatMoney(sum)).color(NamedTextColor.GOLD));
                component = component.append(Component.text("\n\n"));


                // Display all the entries
                int rank = 1;
                for (PlayerBalanceEntry entry : listOfPlayerBalances) {

                    // Don't show past #10
                    if (rank > 10)
                        break;

                    String legacyName = SMPRPG.getInstance().getChatService().getPlayerDisplayname(entry.player);

                    component = component.append(Component.text(String.format("#%d: ", rank)).style(Style.style(NamedTextColor.AQUA, TextDecoration.ITALIC)));
                    component = component.append(Component.text(legacyName));
                    component = component.append(Component.text(" - ").color(NamedTextColor.GRAY));
                    component = component.append(Component.text(EconomyService.formatMoney(entry.balance)).color(NamedTextColor.GOLD));
                    component = component.append(Component.text("\n"));

                    rank++;
                }

                component = component.append(Component.text("\n-------------------------------------").color(NamedTextColor.GRAY));
                commandSender.sendMessage(component);
            }
        }.runTaskAsynchronously(SMPRPG.getInstance());

    }

}

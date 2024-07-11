package xyz.devvydont.smprpg.commands.items;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.gui.InterfaceItemBrowser;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandSearchItem extends CommandBase {

    public CommandSearchItem(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        CommandSender commandSender = commandSourceStack.getSender();

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatUtil.getErrorMessage("You must be a player to search items!"));
            return;
        }

        Player player = (Player) commandSender;

        String query = "";
        if (strings.length > 0)
            query = strings[0].toLowerCase();
        InterfaceItemBrowser gui = new InterfaceItemBrowser(SMPRPG.getInstance(), player, query);
        gui.open();

        if (query.equalsIgnoreCase(""))
            query = "ALL ITEMS";

        commandSender.sendMessage(ChatUtil.getSuccessMessage("Browsing custom items! Query: " + query));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        List<String> validArgs = new ArrayList<>();

        if (args.length != 1)
            return validArgs;

        List<String> keys = new ArrayList<>();
        keys.add("all");
        for (SMPItemBlueprint blueprint : SMPRPG.getInstance().getItemService().getCustomBlueprints())
            if (blueprint instanceof CustomItemBlueprint)
                keys.add(((CustomItemBlueprint) blueprint).getCustomItemType().getKey().toLowerCase());

        for (String key : keys)
            if (key.contains(args[0].toLowerCase()))
                validArgs.add(key);

        return validArgs;

    }
}

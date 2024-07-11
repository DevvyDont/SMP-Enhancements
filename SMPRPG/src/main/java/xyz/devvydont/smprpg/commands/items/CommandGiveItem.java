package xyz.devvydont.smprpg.commands.items;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandGiveItem extends CommandBase {

    public CommandGiveItem(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        CommandSender commandSender = commandSourceStack.getSender();

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatUtil.getErrorMessage("You must be a player to get items!"));
            return;
        }

        if (strings.length <= 0) {
            commandSender.sendMessage(ChatUtil.getErrorMessage("You must provide an item key!"));
            return;
        }

        Player player = (Player) commandSender;

        // Players are allowed to provide "all" to get one of every item.
        if (strings[0].equalsIgnoreCase("all")) {
            for (SMPItemBlueprint blueprint : SMPRPG.getInstance().getItemService().getCustomBlueprints())
                player.getInventory().addItem(blueprint.generate());
            commandSender.sendMessage(ChatUtil.getSuccessMessage("Gave you one of everything!"));
            return;
        }

        ItemStack item = SMPRPG.getInstance().getItemService().getCustomItem(strings[0]);
        if (item == null) {
            commandSender.sendMessage(ChatUtil.getErrorMessage("Unknown item with key: " + strings[0]));
            return;
        }

        player.getInventory().addItem(item);
        commandSender.sendMessage(ChatUtil.getSuccessMessage("Gave you a " + strings[0]));
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

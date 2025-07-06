package xyz.devvydont.smprpg.commands.items;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandGiveItem extends CommandBase {

    public CommandGiveItem(String name) {
        super(name);
    }

    @Override
    public Collection<String> getAliases() {
        return List.of("i", "item");
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        CommandSender commandSender = commandSourceStack.getSender();

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ComponentUtils.error("You must be a player to get items!"));
            return;
        }

        if (strings.length <= 0) {
            commandSender.sendMessage(ComponentUtils.error("You must provide an item key!"));
            return;
        }

        Player player = (Player) commandSender;

        // Players are allowed to provide "all" to get one of every item.
        if (strings[0].equalsIgnoreCase("all")) {
            for (SMPItemBlueprint blueprint : SMPRPG.getService(ItemService.class).getCustomBlueprints())
                player.getInventory().addItem(blueprint.generate());
            commandSender.sendMessage(ComponentUtils.success("Gave you one of everything!"));
            return;
        }

        ItemStack item = SMPRPG.getService(ItemService.class).getCustomItem(strings[0].replace("smprpg:", ""));
        if (item == null) {

            Material vanillaMaterial = Material.matchMaterial(strings[0].replace("minecraft:", "").toLowerCase());
            if (vanillaMaterial == null) {
                commandSender.sendMessage(ComponentUtils.error("Unknown item with key: " + strings[0]));
                return;
            }
            item = SMPRPG.getService(ItemService.class).getCustomItem(vanillaMaterial);

        }

        int amount = 1;
        try {
            amount = Integer.parseInt(strings[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {}
        amount = Math.min(Math.max(0, amount), 99);
        item.setAmount(amount);
        player.getInventory().addItem(item);
        commandSender.sendMessage(ComponentUtils.success("Gave you: " + strings[0] + " x" + amount));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        List<String> validArgs = new ArrayList<>();

        if (args.length != 1)
            return validArgs;

        List<String> keys = new ArrayList<>();
        keys.add("all");
        for (SMPItemBlueprint blueprint : SMPRPG.getService(ItemService.class).getCustomBlueprints())
            if (blueprint instanceof CustomItemBlueprint)
                keys.add("smprpg:" + ((CustomItemBlueprint) blueprint).getCustomItemType().getKey().toLowerCase());

        for (Material material : Material.values())
            if (!material.isLegacy())
                if (material.isItem())
                    if (material.name().toLowerCase().contains(args[0].toLowerCase()))
                        keys.add(material.key().asString());

        for (String key : keys)
            if (key.contains(args[0].toLowerCase()))
                validArgs.add(key);

        return validArgs;

    }

    @Override
    public @Nullable String permission() {
        return "smprpg.command.give";
    }
}

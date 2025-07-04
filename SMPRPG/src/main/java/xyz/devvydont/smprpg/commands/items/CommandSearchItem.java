package xyz.devvydont.smprpg.commands.items;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.gui.items.MenuItemBrowser;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandSearchItem extends PlayerCommandBase {

    public CommandSearchItem(String name) {
        super(name);
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {
        String query = String.join(" ", args).replace("_", " ").trim();
        new MenuItemBrowser(player, query).openMenu();
        if (query.equalsIgnoreCase(""))
            query = "ALL ITEMS";

        player.sendMessage(ComponentUtils.success("Browsing custom items! Query: " + query));
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

    @Override
    public Collection<String> getAliases() {
        return List.of("recipe", "recipes");
    }
}

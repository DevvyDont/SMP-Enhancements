package xyz.devvydont.smprpg.commands.player;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.gui.items.MenuReforge;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;

public class CommandReforge extends CommandBase {

    public CommandReforge(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        if (!(commandSourceStack.getSender() instanceof Player player))
            return;

        // If we provided a reforge type and have the admin reforging permission...
        if (player.permissionValue(permission() + ".admin").toBooleanOrElse(false) && args.length > 0) {

            ReforgeType type;
            try {
                type = ReforgeType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(ComponentUtils.error("Invalid reforge: " + args[0]));
                return;
            }

            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType().equals(Material.AIR)) {
                player.sendMessage(ComponentUtils.error("You are not holding anything!"));
                return;
            }

            ReforgeBase reforge = SMPRPG.getService(ItemService.class).getReforge(type);
            reforge.apply(item);
            player.sendMessage(ComponentUtils.success("Applied the " + type.name() + " reforge!"));
            return;
        }

        new MenuReforge(player).openMenu();
    }

    @Override
    protected Collection<String> determineSuggestions(int argumentIndex, String input) {

        if (argumentIndex == 0) {
            Collection<String> reforges = new ArrayList<>();
            for (ReforgeType type : ReforgeType.values())
                reforges.add(type.name());

            return generateArgumentCollection(input, reforges);
        }

        return super.determineSuggestions(argumentIndex, input);
    }

    @Override
    public @Nullable String permission() {
        return "smprpg.command.reforge";
    }
}

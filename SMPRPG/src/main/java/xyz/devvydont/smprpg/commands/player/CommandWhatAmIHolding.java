package xyz.devvydont.smprpg.commands.player;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.Collection;
import java.util.List;

public class CommandWhatAmIHolding extends CommandBase {

    public CommandWhatAmIHolding(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        if (!(commandSourceStack.getSender() instanceof Player player))
            return;

        ItemStack is = player.getInventory().getItemInMainHand();

        if (is.getType().equals(Material.AIR)) {
            player.sendMessage(Component.text("Must be holding an item!", NamedTextColor.RED));
            return;
        }

        Component name = Component.text(SMPRPG.getInstance().getChatService().getPlayerDisplayname(player));
        Component holding = ComponentUtil.getDefaultText(" is holding ");
        Bukkit.broadcast(name.append(holding).append(is.displayName()));
    }

    @Override
    public Collection<String> getAliases() {
        return List.of(
                "waih"
        );
    }
}

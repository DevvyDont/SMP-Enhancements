package me.devvy.whatamiholding;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WAIHCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player))
            return true;

        Player p = (Player) commandSender;

        ItemStack is = p.getInventory().getItemInMainHand();
        if (is == null || is.getType().equals(Material.AIR)) {
            p.sendMessage(Component.text("Must be holding an item!", NamedTextColor.RED));
            return true;
        }

        Bukkit.broadcast(Component.text(p.getName() + " >> ", NamedTextColor.GRAY).append(is.displayName()));
        return true;
    }
}

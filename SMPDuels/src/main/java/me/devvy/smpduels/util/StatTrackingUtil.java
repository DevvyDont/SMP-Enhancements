package me.devvy.smpduels.util;

import me.devvy.smpduels.SMPDuels;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class StatTrackingUtil {


    public static NamespacedKey getWinsAgainstPlayerKey(Player wonAgainst) {
        return new NamespacedKey(SMPDuels.getInstance(), "duel-wins-" + wonAgainst.getUniqueId());
    }

    public static NamespacedKey getLossesAgainstPlayerKey(Player lostAgainst) {
        return new NamespacedKey(SMPDuels.getInstance(), "duel-wins" + lostAgainst.getUniqueId());
    }

    public static int getWinsAgainst(Player retriever, Player checkingAgainst) {
        return retriever.getPersistentDataContainer().getOrDefault(getWinsAgainstPlayerKey(checkingAgainst), PersistentDataType.INTEGER, 0);
    }

    public static int getLossesAgainst(Player retriever, Player checkingAgainst) {
        return retriever.getPersistentDataContainer().getOrDefault(getLossesAgainstPlayerKey(checkingAgainst), PersistentDataType.INTEGER, 0);
    }

    public static void incrementDuelWinLoss(Player winner, Player loser) {
        winner.getPersistentDataContainer().set(getWinsAgainstPlayerKey(loser), PersistentDataType.INTEGER, getWinsAgainst(winner, loser) + 1);
        loser.getPersistentDataContainer().set(getLossesAgainstPlayerKey(winner), PersistentDataType.INTEGER, getLossesAgainst(loser, winner) + 1);
    }

    public static ItemStack getPlayerHead(Player winner, Player loser) {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwningPlayer(loser);

        int wins = getWinsAgainst(winner, loser);
        int losses = getLossesAgainst(winner, loser);
        headMeta.displayName(Component.text(String.format("%s's Head", loser.getName()), Style.style(NamedTextColor.RED)).decoration(TextDecoration.ITALIC, false));
        headMeta.lore(Arrays.asList(
                    Component.empty(),
                    Component.text(winner.getName(), NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false).append(Component.text("'s proof of victory in a 1v1 against ", NamedTextColor.GRAY).append(Component.text(loser.getName(), NamedTextColor.RED))),
                    Component.text(winner.getName(), NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false).append(Component.text(" is currently ", NamedTextColor.GRAY).append(Component.text(wins, NamedTextColor.GREEN, TextDecoration.BOLD).append(Component.text(" - ", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false).append(Component.text(losses, NamedTextColor.RED, TextDecoration.BOLD).append(Component.text(" against them!", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))))))
                ));

        head.setItemMeta(headMeta);
        return head;

    }

}

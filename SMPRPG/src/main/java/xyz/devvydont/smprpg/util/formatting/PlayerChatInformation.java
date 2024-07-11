package xyz.devvydont.smprpg.util.formatting;

import org.bukkit.OfflinePlayer;

public record PlayerChatInformation(OfflinePlayer player, String prefix, String suffix) {
}

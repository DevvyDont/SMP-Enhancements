package xyz.devvydont.smprpg.util.formatting;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;

public record PlayerChatInformation(OfflinePlayer player, String prefix, String suffix, TextColor nameColor) {
}

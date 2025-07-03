package xyz.devvydont.smprpg.util.chat;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class CustomChatRenderer implements ChatRenderer {

    @Override
    public @NotNull Component render(@NotNull Player source, Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {

        // Retrieve data about the player.
        var playerWrapper = SMPRPG.getInstance().getEntityService().getPlayerInstance(source);
        int level = playerWrapper.getLevel();
        var information = SMPRPG.getInstance().getChatService().getPlayerInfo(source);
        var prefix = information.prefix().isEmpty() ? ComponentUtils.EMPTY : LegacyComponentSerializer.legacyAmpersand().deserialize(information.prefix());
        var suffix = information.suffix().isEmpty() ? ComponentUtils.EMPTY : LegacyComponentSerializer.legacyAmpersand().deserialize(information.suffix());
        var color = playerWrapper.getDifficulty().Color;

        // Construct the message format.
        return ComponentUtils.merge(
                prefix,
                ComponentUtils.powerLevelPrefix(level).append(ComponentUtils.SPACE),
                sourceDisplayName.color(color),
                suffix,
                ComponentUtils.create(" >> "),
                message
        );
    }

}

package xyz.devvydont.smprpg.services;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.chat.CustomChatRenderer;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.PlayerChatInformation;

public class ChatService implements IService, Listener {


    public static final ChatRenderer CHAT_RENDERER = new CustomChatRenderer();

    private final SMPRPG plugin;

    private Chat chat;

    public ChatService(SMPRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean setup() {
        plugin.getLogger().info("Setting up Chat service");

        // If vault isn't installed, we cannot function correctly.
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("Vault is not installed. Please install Vault");
            return false;
        }

        // We need to make sure the economy class is valid
        RegisteredServiceProvider<Chat> provider = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        if (provider == null) {
            plugin.getLogger().severe("Failed to detect Chat service, is Vault installed correctly?");
            return false;
        }

        this.chat = provider.getProvider();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getLogger().info("Successfully hooked into Vault Chat service");
        return true;
    }

    @Override
    public void cleanup() {
        plugin.getLogger().info("Cleaning up ChatService");
    }

    @Override
    public boolean required() {
        return true;
    }

    private TextColor determineNameColor(OfflinePlayer player) {
        var difficulty = SMPRPG.getInstance().getDifficultyService().getDifficulty(player);
        return difficulty.Color;
    }

    public Component getPlayerDisplay(OfflinePlayer player) {
        var info = getPlayerInfo(player);
        return ComponentUtils.merge(
                Component.text(info.prefix(), NamedTextColor.WHITE),
                ComponentUtils.create(player.getName(), info.nameColor())
        );
    }

    public PlayerChatInformation getPlayerInfo(Player player) {
        String prefix = chat.getPlayerPrefix(player);
        return new PlayerChatInformation(player, prefix, chat.getPlayerSuffix(player), determineNameColor(player));
    }

    public PlayerChatInformation getPlayerInfo(OfflinePlayer player) {
        String world = Bukkit.getWorlds().get(0).getName();
        String prefix = chat.getPlayerPrefix(world, player);
        return new PlayerChatInformation(player, prefix, chat.getPlayerSuffix(world, player), determineNameColor(player));
    }

    /**
     * Injects the player level into a chat message no matter what chat plugins are doing.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void __onChat(AsyncChatEvent event) {
        event.renderer(CHAT_RENDERER);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void __onJoin(PlayerJoinEvent event) {
        var name = getPlayerDisplay(event.getPlayer());
        var msg = ComponentUtils.merge(
                name,
                ComponentUtils.create(" has joined the game!", NamedTextColor.YELLOW)
        );
        event.joinMessage(ComponentUtils.alert(msg, NamedTextColor.GREEN));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void __onLeave(PlayerQuitEvent event) {
        var name = getPlayerDisplay(event.getPlayer());
        var msg = ComponentUtils.merge(
                name,
                ComponentUtils.create(" has left the game!", NamedTextColor.YELLOW)
        );
        event.quitMessage(ComponentUtils.alert(msg, NamedTextColor.RED));
    }
}

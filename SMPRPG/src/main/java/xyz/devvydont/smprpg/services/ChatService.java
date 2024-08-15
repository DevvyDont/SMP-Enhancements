package xyz.devvydont.smprpg.services;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.PlayerChatInformation;
import xyz.devvydont.smprpg.util.formatting.Symbols;

public class ChatService implements BaseService, Listener {


    private SMPRPG plugin;

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

    private TextColor translateLegacyChatColor(ChatColor color) {
        return switch (color) {
            case RED -> NamedTextColor.RED;
            case AQUA -> NamedTextColor.AQUA;
            case BLUE -> NamedTextColor.BLUE;
            case GREEN -> NamedTextColor.GREEN;
            case YELLOW -> NamedTextColor.YELLOW;
            case GRAY -> NamedTextColor.GRAY;
            case BLACK -> NamedTextColor.BLACK;
            case DARK_RED -> NamedTextColor.DARK_RED;
            case DARK_AQUA -> NamedTextColor.DARK_AQUA;
            case DARK_BLUE -> NamedTextColor.DARK_BLUE;
            case DARK_GREEN -> NamedTextColor.DARK_GREEN;
            case DARK_GRAY -> NamedTextColor.DARK_GRAY;
            case DARK_PURPLE -> NamedTextColor.DARK_PURPLE;
            case LIGHT_PURPLE -> NamedTextColor.LIGHT_PURPLE;
            case GOLD -> NamedTextColor.GOLD;
            default -> NamedTextColor.WHITE;
        };
    }

    private TextColor determineNameColor(String prefix) {
        int index = prefix.lastIndexOf('&');
        if (index == -1 || index == prefix.length() - 1)
            return NamedTextColor.WHITE;

        ChatColor color = ChatColor.getByChar(prefix.charAt(index+1));
        if (color == null)
            return NamedTextColor.WHITE;

        return translateLegacyChatColor(color);
    }

    public PlayerChatInformation getPlayerInfo(Player player) {
        String prefix = chat.getPlayerPrefix(player);
        return new PlayerChatInformation(player, prefix, chat.getPlayerSuffix(player), determineNameColor(prefix));
    }

    public PlayerChatInformation getPlayerInfo(OfflinePlayer player) {
        String world = Bukkit.getWorlds().get(0).getName();
        String prefix = chat.getPlayerPrefix(world, player);
        return new PlayerChatInformation(player, prefix, chat.getPlayerSuffix(world, player), determineNameColor(prefix));
    }

    public PlayerChatInformation getPlayerInfo(String name) {
        OfflinePlayer p = Bukkit.getPlayer(name);
        if (p == null)
            p = Bukkit.getOfflinePlayerIfCached(name);
        if (p == null)
            p = Bukkit.getOfflinePlayer(name);

        String prefix = chat.getPlayerPrefix("world", p);
        return new PlayerChatInformation(p, prefix, chat.getPlayerSuffix("world", p), determineNameColor(prefix));
    }

    public String getPlayerDisplayname(OfflinePlayer player) {
        PlayerChatInformation information = getPlayerInfo(player);
        return ChatColor.translateAlternateColorCodes('&', (information.prefix() + player.getName() + information.suffix().stripTrailing()));
    }

    /**
     * Injects the player level into a chat message no matter what chat plugins are doing.
     * todo replace with PlaceholdersAPI
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncChatEvent event) {

        // Hack, if power symbol in component don't do anything
        if (PlainTextComponentSerializer.plainText().serialize(event.message()).contains(Symbols.POWER))
            return;

        int level = plugin.getEntityService().getPlayerInstance(event.getPlayer()).getLevel();
        Component component = ChatUtil.getBracketedPowerComponent(level).append(Component.text(" ")).append(event.message());
        event.message(component);
    }
}

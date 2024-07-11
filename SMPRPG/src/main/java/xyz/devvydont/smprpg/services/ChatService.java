package xyz.devvydont.smprpg.services;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.PlayerChatInformation;

public class ChatService implements BaseService {


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
        plugin.getLogger().info("Successfully hooked into Vault Economy service");
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

    public PlayerChatInformation getPlayerInfo(Player player) {
        return new PlayerChatInformation(player, chat.getPlayerPrefix(player), chat.getPlayerSuffix(player));
    }

    public PlayerChatInformation getPlayerInfo(OfflinePlayer player) {
        String world = Bukkit.getWorlds().get(0).getName();
        return new PlayerChatInformation(player, chat.getPlayerPrefix(world, player), chat.getPlayerSuffix(world, player));
    }

    public PlayerChatInformation getPlayerInfo(String name) {
        OfflinePlayer p = Bukkit.getPlayer(name);
        if (p == null)
            p = Bukkit.getOfflinePlayerIfCached(name);
        if (p == null)
            p = Bukkit.getOfflinePlayer(name);

        return new PlayerChatInformation(p, chat.getPlayerPrefix("world", p), chat.getPlayerSuffix("world", p));
    }

}

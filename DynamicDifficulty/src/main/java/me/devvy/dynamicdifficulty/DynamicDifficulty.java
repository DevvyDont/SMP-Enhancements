package me.devvy.dynamicdifficulty;

import me.devvy.dynamicdifficulty.commands.DifficultyCommand;
import me.devvy.dynamicdifficulty.gui.DifficultyPreferenceGUI;
import me.devvy.dynamicdifficulty.listeners.DifficultyUpdateListener;
import me.devvy.dynamicdifficulty.listeners.ExperienceGainListener;
import me.devvy.dynamicdifficulty.storage.DifficultyPreferenceStorageManager;
import me.devvy.dynamicdifficulty.tasks.PlayerUpdateTask;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


public final class DynamicDifficulty extends JavaPlugin implements Listener {

    private static DynamicDifficulty INSTANCE;

    public static DynamicDifficulty getInstance() {
        return INSTANCE;
    }

    private DifficultyUpdateListener difficultyUpdateListener;

    private DifficultyPreferenceStorageManager difficultyPreferenceStorageManager;

    private DifficultyPreferenceGUI difficultyPreferenceGUI;

    public void checkForNewDifficulty() {
        // Set the difficulty to the most voted difficulty
        Difficulty newDifficulty = difficultyPreferenceStorageManager.getDifficultyVoteReport().calculateVotedDifficulty();

        // If the difficulty is not going to change don't do anything
        if (newDifficulty == getCurrentDifficulty())
            return;

        setDifficulty(newDifficulty);
    }

    public DifficultyUpdateListener getDifficultyUpdateListener() {
        return difficultyUpdateListener;
    }

    public DifficultyPreferenceStorageManager getDifficultyPreferenceStorageManager() {
        return difficultyPreferenceStorageManager;
    }

    public DifficultyPreferenceGUI getDifficultyPreferenceGUI() {
        return difficultyPreferenceGUI;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        DifficultyCommand difficultyCommand = new DifficultyCommand();
        getCommand("difficulty").setExecutor(difficultyCommand);
        getCommand("difficulty").setTabCompleter(difficultyCommand);

        difficultyUpdateListener =  new DifficultyUpdateListener();
        new ExperienceGainListener();
        difficultyPreferenceStorageManager = new DifficultyPreferenceStorageManager();
        difficultyPreferenceGUI = new DifficultyPreferenceGUI();

        new PlayerUpdateTask().runTaskTimer(this, 20, 20);

        getServer().getPluginManager().registerEvents(this, this);
    }

    public ChatColor difficultyToColor(Difficulty difficulty) {
        switch (difficulty) {
            case PEACEFUL:
                return ChatColor.AQUA;
            case EASY:
                return ChatColor.GREEN;
            case NORMAL:
                return ChatColor.YELLOW;
            case HARD:
                return ChatColor.RED;
            default:
                return ChatColor.WHITE;
        }
    }

    public void setDifficulty(Difficulty difficulty) {
        for (World world : Bukkit.getWorlds()) {
            world.setDifficulty(difficulty);
        }

        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Difficulty has been set to " + difficultyToColor(difficulty) + difficulty.name());

        if (difficulty.equals(Difficulty.EASY)) {
            Bukkit.broadcastMessage(ChatColor.GRAY + "  - " + ChatColor.GREEN + "-50% mob damage");
            Bukkit.broadcastMessage("");
        } else if (difficulty.equals(Difficulty.NORMAL)) {
            Bukkit.broadcastMessage(ChatColor.GRAY + "  - " + ChatColor.GREEN + "+25% XP gains");
            Bukkit.broadcastMessage("");
        } else if (difficulty.equals(Difficulty.HARD)) {
            Bukkit.broadcastMessage(ChatColor.GRAY + "  - " + ChatColor.GREEN + "+50% XP gains");
            Bukkit.broadcastMessage(ChatColor.GRAY + "  - " + ChatColor.GREEN + "+1 Luck");
            Bukkit.broadcastMessage(ChatColor.GRAY + "  - " + ChatColor.RED + "+50% mob damage");
            Bukkit.broadcastMessage("");
        }
    }

    public Difficulty getCurrentDifficulty() {
        return Bukkit.getWorlds().get(0).getDifficulty();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        difficultyPreferenceGUI.cleanup();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "!" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Difficulty is currently set to " + difficultyToColor(getCurrentDifficulty()) + getCurrentDifficulty().name()));
    }
}

package me.devvy.advancementcompetition;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public final class AdvancementCompetition extends JavaPlugin implements Listener {
    private static AdvancementCompetition INSTANCE;

    static Component prefix =
            Component.text("[", TextColor.color(120, 120, 120))
                    .append(Component.text("!", TextColor.color(255, 255, 0)))
                    .append(Component.text("] ", TextColor.color(120, 120, 120)));

    static TextColor GRAY = TextColor.color(180, 180, 180);
    static TextColor DARK_GRAY = TextColor.color(100, 100, 100);
    static TextColor GOLD = TextColor.color(255, 215, 0);
    static TextColor GREEN = TextColor.color(0, 200, 0);
    static TextColor RED = TextColor.color(200, 50, 0);

    public static void sendAnnouncementMessage (Player player) {



        new BukkitRunnable(){

            int tick = 0;

            Component[] msgs = {
                    prefix.append(Component.text("There is currently an ", GRAY)).append(Component.text("Advancement Hunt ", GOLD, TextDecoration.BOLD)).append(Component.text("in progress!", GRAY)),
                    prefix.append(Component.text("Get the most advancements by ", GRAY)).append(Component.text("Thursday 11:59pm EST!", RED)),
                    prefix.append(Component.text("Top 3 players will be rewarded with unobtainable items :)", GRAY)),
                    prefix.append(Component.text("You can toggle scoreboard visibility at any time using ", GRAY)).append(Component.text("/togglescoreboard", GREEN))
            };

            @Override
            public void run() {

                if (tick >= msgs.length || !player.isOnline()) {
                    cancel();
                    return;
                }

                Component msg = msgs[tick];
                player.sendMessage(msg);
//                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);
                tick++;
            }
        }.runTaskTimer(INSTANCE, 20*5, 20*3);

    }

    public static AdvancementCompetition getInstance() {
        return INSTANCE;
    }

    Scoreboard advancementScoreboard;
    Objective advancementObjective;

    private Set<Advancement> validAdvancements = new HashSet<>();


    @Override
    public void onEnable() {
        INSTANCE = this;

        // Register the scoreboard objective
        advancementScoreboard = getServer().getScoreboardManager().getNewScoreboard();

        // Dupe scoreboard properties to this one
        for (Objective obj: getServer().getScoreboardManager().getMainScoreboard().getObjectives()) {
            advancementScoreboard.registerNewObjective(obj.getName(), obj.getTrackedCriteria(), obj.displayName());
            advancementScoreboard.getObjective(obj.getName()).setDisplaySlot(obj.getDisplaySlot());
            advancementScoreboard.getObjective(obj.getName()).setRenderType(obj.getRenderType());

            // Sync score for all players of this objective
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                Score oldScore = obj.getScore(offlinePlayer);
                Score newScore = advancementScoreboard.getObjective(obj.getName()).getScore(offlinePlayer);

                newScore.setScore(oldScore.getScore());
            }

        }


        advancementObjective = advancementScoreboard.getObjective("advancements-made");
        if (advancementObjective == null)
            advancementObjective = advancementScoreboard.registerNewObjective(
                    "advancements-made",
                    Criteria.DUMMY,
                    Component.text("Advancement Hunt!", GOLD, TextDecoration.BOLD)
            );

        advancementObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Figure out the advancements that we are tracking
        for ( Iterator<Advancement> it = getServer().advancementIterator(); it.hasNext(); ) {
            Advancement advancement = it.next();

            if (advancement.getDisplay() != null && advancement.getDisplay().doesAnnounceToChat())
                validAdvancements.add(advancement);
        }

        getCommand("togglescoreboard").setExecutor(new HideScoreboardCommand());

        // Read the advancement json data for every player and set their score
        try {
            syncAdvancementScoreFromDisk();
        } catch (IOException | ParseException e) {
            getLogger().warning("Failed to sync player advancements, more than likely someone's json file is corrupt");
        } catch (NullPointerException e) {
            getLogger().severe("Somehow tried to modify advancement progress of a player that is not real");
        }

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new CustomItems(), this);
        CustomItems.registerRecipes();

        updateAllPlayers();

        for (Player p : Bukkit.getOnlinePlayers())
            sendAnnouncementMessage(p);
    }

    public boolean validAdvancement(String key) {

        for (Advancement advancement : validAdvancements)
            if (advancement.key().asString().equalsIgnoreCase(key))
                return true;

        return false;

    }

    public void syncAdvancementScoreFromDisk() throws IOException, ParseException {

        File advancementFolder = new File(getServer().getWorlds().get(0).getWorldFolder().getAbsolutePath() + "/advancements");

        if (!advancementFolder.isDirectory())
            return;

        JSONParser parser = new JSONParser();


        for (File playerAdvancementJsonData : advancementFolder.listFiles()) {

            String playerID = playerAdvancementJsonData.getName().replace(".json", "");
            JSONObject advancementArray = (JSONObject) parser.parse(new FileReader(playerAdvancementJsonData));

            int advancementCount = 0;

            for (Object o: advancementArray.keySet()) {

                String key = (String) o;


                if (!validAdvancement(key))
                    continue;


                // Gross but we are essentially seeing if advancement.done == true
                boolean done = (boolean) ((JSONObject)advancementArray.get(key)).get("done");

                if (done)
                    advancementCount += 1;
            }

            // Set the score of the player
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(playerID));
            Score score = advancementObjective.getScore(offlinePlayer);
            score.setScore(advancementCount);
        }


    }

    public void updateAllPlayers() {
        for (Player p : Bukkit.getOnlinePlayers())
            updatePlayerScoreboard(p);
    }

    public void updatePlayerScoreboard(Player player) {
        boolean hide = player.getPersistentDataContainer().getOrDefault(HideScoreboardCommand.HIDE_SCOREBOARD_SETTING_KEY, PersistentDataType.BOOLEAN, false);

        if (hide)
            player.setScoreboard(getServer().getScoreboardManager().getMainScoreboard());
        else
            player.setScoreboard(advancementScoreboard);
    }

    @Override
    public void onDisable() {
        updateAllPlayers();
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {

        if (!validAdvancement(event.getAdvancement().getKey().asString()))
            return;

        Score score = advancementObjective.getScore(event.getPlayer());
        score.setScore(score.getScore() + 1);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        updatePlayerScoreboard(event.getPlayer());
        sendAnnouncementMessage(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        // Hack to keep deaths synced
        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    Objective deathObjective = getServer().getScoreboardManager().getMainScoreboard().getObjective("deaths");
                    advancementScoreboard.getObjective("deaths").getScore(event.getPlayer()).setScore(deathObjective.getScore(event.getPlayer()).getScore());
                } catch (Exception e) {
                    getLogger().warning("failed to sync deaths oopsies");
                }
            }
        }.runTaskLater(this, 1);

    }
}

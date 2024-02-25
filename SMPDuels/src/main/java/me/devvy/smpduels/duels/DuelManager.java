package me.devvy.smpduels.duels;

import me.devvy.smpduels.SMPDuels;
import me.devvy.smpduels.events.DuelCreateEvent;
import me.devvy.smpduels.events.DuelRequestTimeoutEvent;
import me.devvy.smpduels.util.StatTrackingUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.*;

/**
 * Class in charge of creating/destroying/sending events to PlayerDuel instances
 */
public class DuelManager implements Listener {

    private PlayerDuel currentDuel;

    private Map<UUID, DuelRequest> currentDuelRequests = new HashMap<>();

    public DuelManager() {
        SMPDuels.getInstance().getServer().getPluginManager().registerEvents(this, SMPDuels.getInstance());
    }

    public void processDuelRequest(Player wantsToDuel, Player beingAskedToDuel) {

        // Handle the case first where someone is confirming a duel with another player who alraedy initiated a request
        DuelRequest potentialDuelAccept = currentDuelRequests.get(beingAskedToDuel.getUniqueId());
        if (potentialDuelAccept != null && potentialDuelAccept.getRequestee().equals(wantsToDuel.getUniqueId())) {

            // Handle case where we accept a duel
            if (!potentialDuelAccept.validate()) {
                wantsToDuel.sendMessage(Component.text("Player is not online!", NamedTextColor.RED));
                return;
            }

            createDuel(Arrays.asList(wantsToDuel, beingAskedToDuel));
            cancelDuelRequest(beingAskedToDuel.getUniqueId(), "");
            return;

        }

        DuelRequest currentRequest = currentDuelRequests.get(wantsToDuel.getUniqueId());

        // If there is already a request and it is the same person, tell them to stop it
        if (currentRequest != null && currentRequest.getRequestee().equals(beingAskedToDuel.getUniqueId())) {
            wantsToDuel.sendMessage(Component.text("Already requested to duel this player!", NamedTextColor.RED));
            return;
        }

        // If there is already a request and it is another player, cancel the request and process the new one
        if (currentRequest != null)
            cancelDuelRequest(wantsToDuel.getUniqueId(), "Canceled current duel request");

        // Make a new request
        currentRequest = new DuelRequest(wantsToDuel, beingAskedToDuel);

        if (!currentRequest.validate()) {
            wantsToDuel.sendMessage(Component.text("That player is not online!", NamedTextColor.RED));
            return;
        }

        currentRequest.initiate();
        wantsToDuel.sendMessage(Component.text(String.format("Sent duel request to %s!", beingAskedToDuel.getName()), NamedTextColor.GREEN));

        String acceptCmd = String.format("/duel %s", wantsToDuel.getName());
        beingAskedToDuel.sendMessage(
                wantsToDuel.displayName()
                        .append(Component.text(" wants to duel! Type ", NamedTextColor.GRAY))
                        .append(Component.text(acceptCmd, NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand(acceptCmd)))
        );

        currentDuelRequests.put(wantsToDuel.getUniqueId(), currentRequest);
    }

    public void cancelDuelRequest(UUID player, String cancelReason) {
        DuelRequest req = currentDuelRequests.get(player);
        if (req == null)
            return;

        currentDuelRequests.remove(player);

        if (!cancelReason.isEmpty() && Bukkit.getPlayer(player) != null)
            Bukkit.getPlayer(player).sendMessage(Component.text(cancelReason, NamedTextColor.RED));

    }

    @EventHandler
    public void onRequestTimeout(DuelRequestTimeoutEvent event) {
        cancelDuelRequest(event.getDuelRequest().getInitiator(), "Duel request timed out");
    }

    /**
     * Creates a duel with players given and starts it immediately
     *
     * @param players
     * @return
     */
    public PlayerDuel createDuel(Collection<Player> players) {

        if (ongoingDuel())
            throw new IllegalStateException("Duel already happening!");

        if (SMPDuels.getInstance().getDuelArenaLocation() == null)
            throw new IllegalStateException("Arena location not set!");

        DuelCreateEvent duelEvent = new DuelCreateEvent(players.stream().toList());
        SMPDuels.getInstance().getServer().getPluginManager().callEvent(duelEvent);
        if (duelEvent.isCancelled())
            throw new IllegalStateException(duelEvent.getCancelReason());

        // Create the duel
        currentDuel = new PlayerDuel();

        for (Player p : players)
            currentDuel.addPlayer(p);

        Component prefix = Component.text("[", NamedTextColor.GRAY).append(Component.text("!", NamedTextColor.YELLOW)).append(Component.text("] ", NamedTextColor.GRAY));
        Iterator<Player> iterator = players.iterator();
        if (players.size() == 2)
            Bukkit.broadcast(
                    prefix.append(iterator.next().displayName().color(NamedTextColor.RED))
                            .append(Component.text(" is dueling ", NamedTextColor.GRAY))
                            .append(iterator.next().displayName().color(NamedTextColor.RED))
                            .append(Component.text("!", NamedTextColor.GRAY))
            );
        else
            Bukkit.broadcast(prefix.append(Component.text("A duel between multiple players is happening!", NamedTextColor.GRAY)));

        currentDuel.countdown();
        return currentDuel;
    }

    public PlayerDuel getCurrentDuel() {
        if (!ongoingDuel())
            throw new IllegalStateException("No duel happening!");

        return currentDuel;

    }

    public void stopCurrentDuel() {

        if (!ongoingDuel())
            throw new IllegalStateException("No duel happening!");

        currentDuel.stop();
        currentDuel = null;
    }

    public boolean ongoingDuel() {
        return currentDuel != null;
    }

    /**
     * Attempts to announce the winner of a duel, if there isnt one, we dont do anything
     *
     * @param duel
     */
    public void announceDuelWinner(PlayerDuel duel) {

        UUID winner = getCurrentDuel().calculateWinner();
        if (winner == null)
            return;

        Component prefix = Component.text("[", NamedTextColor.GRAY).append(Component.text("!", NamedTextColor.YELLOW)).append(Component.text("] ", NamedTextColor.GRAY));
        Player p = Bukkit.getPlayer(winner);
        Bukkit.broadcast(prefix.append(p.displayName().color(NamedTextColor.RED)).append(Component.text(" won the duel!", NamedTextColor.GREEN)));

        // If this was a 1v1, give a playerhead
        if (duel.getPlayers().size() == 2) {

            Player loser = duel.getPlayers().stream().filter(e -> e.getUniqueId() != winner).findFirst().get();

            StatTrackingUtil.incrementDuelWinLoss(p, loser);
            p.getInventory().addItem(
                    StatTrackingUtil.getPlayerHead(p, loser)
            );
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                stopCurrentDuel();
            }
        }.runTaskLater(SMPDuels.getInstance(), 20*5);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!ongoingDuel())
            return;

        // If the player who died was in a duel mark them as dead
        if (getCurrentDuel().participating(event.getPlayer())) {
            getCurrentDuel().playerDied(event.getPlayer());
            Player killer = event.getPlayer().getKiller();
            if (killer != null) {
                killer.showTitle(Title.title(Component.empty(), Component.text("[X] ", NamedTextColor.GRAY).append(Component.text(event.getPlayer().getName(), NamedTextColor.RED))));
                killer.playSound(killer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f);
            }
            announceDuelWinner(getCurrentDuel());

            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().spigot().respawn();
                }
            }.runTaskLater(SMPDuels.getInstance(), 1);
        }
    }


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        if (!ongoingDuel())
            return;

        // If the player that died was participating in a duel, respawn them above the arena
        if (getCurrentDuel().participating(event.getPlayer())) {
            event.setRespawnLocation(SMPDuels.getInstance().getDuelArenaLocation().clone().add(0, 10, 0));
            event.getPlayer().setInvulnerable(true);
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        if (!ongoingDuel())
            return;

        // If this player is in the duel and has jump and slowness dont let them move
        if (getCurrentDuel().participating(event.getPlayer())) {

            boolean movedBlock = !event.getTo().getBlock().equals(event.getFrom().getBlock());

            if (movedBlock && event.getPlayer().getPotionEffect(PotionEffectType.SLOW) != null && event.getPlayer().getPotionEffect(PotionEffectType.JUMP) != null)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageOpponent(EntityDamageByEntityEvent event) {

        if (!ongoingDuel())
            return;

        // If both entities are playings competing in the duel
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        int targetHP = (int)((damaged.getHealth()-event.getFinalDamage()) * 5) + 1;
        TextColor hpColor = TextColor.lerp(targetHP / 100f, NamedTextColor.RED, NamedTextColor.GREEN);

        if (targetHP <= 0) {
            targetHP = 0;
            hpColor = NamedTextColor.DARK_GRAY;
        }

        if (getCurrentDuel().getPlayers().containsAll(Arrays.asList(damager, damaged)))
            damager.sendActionBar(damaged.displayName().append(Component.text(": ", NamedTextColor.GRAY).append(Component.text(targetHP, hpColor).append(Component.text(" HP", NamedTextColor.RED)))));

    }

}

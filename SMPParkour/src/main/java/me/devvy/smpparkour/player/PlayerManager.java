package me.devvy.smpparkour.player;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.events.AttemptEnterParkourWorldEvent;
import me.devvy.smpparkour.events.AttemptLeaveParkourWorldEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager implements Listener {

    private Map<UUID, ParkourPlayer> playerTracker = new HashMap<>();

    public PlayerManager() {
        SMPParkour.getInstance().getServer().getPluginManager().registerEvents(this, SMPParkour.getInstance());

        new TablistDisplay(this).runTaskTimer(SMPParkour.getInstance(), 1, 10);
    }

    public ParkourPlayer getParkourPlayer(Player player) {
        return playerTracker.get(player.getUniqueId());
    }

    public Collection<ParkourPlayer> getAllParkourPlayers() {
        return playerTracker.values();
    }

    public void warpToParkourWorld(Player player) {

        if (playerTracker.containsKey(player.getUniqueId()))
            throw new IllegalStateException("Player " + player.getName() + " is already at parkour world!");

        AttemptEnterParkourWorldEvent event = new AttemptEnterParkourWorldEvent(player);
        event.callEvent();
        if (event.isCancelled())
            throw new IllegalStateException(event.getReason());

        playerTracker.put(player.getUniqueId(), new ParkourPlayer(player));
        getParkourPlayer(player).enter();
    }

    public void warpOutParkourWorld(Player player) {

        if (!playerTracker.containsKey(player.getUniqueId()))
            throw new IllegalStateException("Player " + player.getName() + " is not at parkour world!");

        AttemptLeaveParkourWorldEvent event = new AttemptLeaveParkourWorldEvent(player);
        event.callEvent();
        if (event.isCancelled())
            throw new IllegalStateException(event.getReason());

        ParkourPlayer pp = playerTracker.remove(player.getUniqueId());
        pp.exit();
    }

    public void clearParkourWorld() {

        for (ParkourPlayer pp : playerTracker.values())
            warpOutParkourWorld(pp.getPlayer());

        playerTracker.clear();
    }


    public void cleanup() {
        clearParkourWorld();
    }

    @EventHandler
    public void onTookDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        ParkourPlayer pkp = getParkourPlayer(player);
        if (pkp == null || pkp.getCurrentCheckpoint() == null)
            return;

        // Never let a parkour player get hurt
        event.setDamage(0);

        // If a player took lava damage cancel and send them to last checkpoint
        if (event.getCause().equals(EntityDamageEvent.DamageCause.LAVA) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
            event.setCancelled(true);
            player.teleport(pkp.getCurrentCheckpoint().getSpawn());
            player.setFireTicks(0);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
        }

    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        ParkourPlayer pkp = getParkourPlayer(player);
        if (pkp == null || pkp.getCurrentCheckpoint() == null)
            return;

        if (event.getDamager() instanceof Firework)
            event.setCancelled(true);

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler
    public void onFellInVoid(PlayerMoveEvent event) {

        ParkourPlayer pkp = getParkourPlayer(event.getPlayer());
        if (pkp == null || pkp.getCurrentCheckpoint() == null)
            return;

        if (event.getTo().y() <= 0) {
            event.getPlayer().teleport(pkp.getCurrentCheckpoint().getSpawn());
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1, 1);
        }

    }

    @EventHandler
    public void onThrow(PlayerDropItemEvent event) {

        ParkourPlayer pkp = getParkourPlayer(event.getPlayer());
        if (pkp == null)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        ParkourPlayer pkp = getParkourPlayer(event.getPlayer());
        if (pkp == null)
            return;

        warpOutParkourWorld(event.getPlayer());
    }

    @EventHandler
    public void onWarpedOut(PlayerChangedWorldEvent event) {

        ParkourPlayer pkp = getParkourPlayer(event.getPlayer());
        if (pkp == null)
            return;

        if (event.getPlayer().getWorld() == SMPParkour.getInstance().getParkourWorld())
            return;

        warpOutParkourWorld(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        ParkourPlayer pkp = getParkourPlayer(event.getPlayer());
        if (pkp == null)
            return;

        event.setRespawnLocation(SMPParkour.getInstance().getParkourWorld().getSpawnLocation());
    }
}

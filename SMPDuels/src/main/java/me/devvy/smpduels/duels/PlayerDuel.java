package me.devvy.smpduels.duels;

import me.devvy.smpduels.SMPDuels;
import me.devvy.smpduels.events.DuelEndedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class in charge for allowing two players to duel each other
 */
public class PlayerDuel {

    public void addPlayerHPDecayStack(Player player) {

        AttributeModifier hpDecayModifier = new AttributeModifier("duels-hp-decay-modifier", -1, AttributeModifier.Operation.ADD_NUMBER);

        // add a stack of hp decay if max HP is above 2
        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() >= 2.0)
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(hpDecayModifier);

        // If hp > max hp correct them
        if (player.getHealth() > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

        player.setHealthScaled(false);

        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, .2f, 1.3f);
    }

    public void clearHPDecayModifier(Player player) {
        Stream<AttributeModifier> toRemove = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().stream().filter(x -> x.getName().equals("duels-hp-decay-modifier"));
        toRemove.forEach(attributeModifier -> player.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(attributeModifier));
    }

    BukkitRunnable countdownTask;

    Set<UUID> players = new HashSet<>();
    Set<UUID> alivePlayers = new HashSet<>();

    /**
     * Generates a list of spawn locations dependent on how many players we have
     *
     * @return
     */
    public Collection<Location> getSpawnLocations() {

        assert SMPDuels.getInstance().getDuelArenaLocation() != null;
        Location origin = SMPDuels.getInstance().getDuelArenaLocation().clone();

        List<Location> locs = new ArrayList<>();

        // If we have two players, just do the basic faceoff
        if (players.size() == 2) {
            Location l1 = origin.clone().add(7, 0, 0);
            l1.setYaw(90);
            locs.add(l1);

            Location l2 = origin.clone().add(-7, 0, 0);
            l2.setYaw(-90);
            locs.add(l2);
        } else {
            //todo use circle math :)
            for (UUID pid : players)
                locs.add(origin.clone().add(Math.random()*10-5, 0, Math.random()*10-5));
        }

        // make all locations face the origin
//        for (Location location : locs)
//            location.setDirection(origin.clone().toVector().subtract(location.toVector()));

        return locs;
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        boolean removed = players.remove(player.getUniqueId());

        if (removed) {
            player.setInvulnerable(false);
            playerDied(player);
        }
    }

    public void playerDied(Player player) {
        alivePlayers.remove(player.getUniqueId());
    }

    public UUID calculateWinner () {
        if (alivePlayers.size() == 1)
            return alivePlayers.iterator().next();

        return null;  // If there is not 1 person left
    }

    public boolean participating(Player player) {
        return players.contains(player.getUniqueId());
    }

    public void freezePlayer(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15*20, 7, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 15*20, 200, false, false));
        player.setInvulnerable(true);
    }

    public void unfreezePlayer(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.setInvulnerable(false);
    }

    public Collection<Player> getPlayers() {
        List<Player> ps = new ArrayList<>();
        for (UUID pid : players)
            if (Bukkit.getPlayer(pid) != null)
                ps.add(Bukkit.getPlayer(pid));

        return ps;
    }

    public void broadcast(Component message) {
        for (Player p : SMPDuels.getInstance().getDuelArenaLocation().getNearbyPlayers(20))
            p.sendMessage(message);
    }

    public void broadcastTitle(Title title) {
        for (Player p : SMPDuels.getInstance().getDuelArenaLocation().getNearbyPlayers(20))
            p.showTitle(title);
    }

    public void broadcastActionBar(Component message) {
        for (Player p : SMPDuels.getInstance().getDuelArenaLocation().getNearbyPlayers(20))
            p.sendActionBar(message);
    }

    public void broadcastSound(Sound sound, float volume, float pitch) {
        for (Player p : SMPDuels.getInstance().getDuelArenaLocation().getNearbyPlayers(20))
            p.playSound(p.getLocation(), sound, volume, pitch);
    }

    public void countdown() {

        alivePlayers.clear();
        alivePlayers.addAll(players);

        Iterator<Location> spawns = getSpawnLocations().iterator();
        for (Player p : getPlayers()) {
            p.teleport(spawns.next());
            freezePlayer(p);
        }

        countdownTask = new BukkitRunnable() {

            int secondsLeft = 10;

            @Override
            public void run() {

                // If time is up then start
                if (secondsLeft <= 0) {
                    start();
                    cancel();
                    return;
                }

                // Announce how much time is left
                float pitch = (float) (.5f + Math.pow((10 - secondsLeft)/7f, 3));
                broadcastTitle(Title.title(Component.text(secondsLeft, TextColor.color(200, 0, 0)), Component.empty()));
                broadcastSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .35f, pitch);
                secondsLeft--;

            }
        };
        countdownTask.runTaskTimer(SMPDuels.getInstance(), 1, 20);

    }

    public void start() {

        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }

        for (Player p : getPlayers()) {
            unfreezePlayer(p);
            p.clearTitle();
        }

        // Overtime task (90s)
        countdownTask = new BukkitRunnable() {

            int ticks = 0;

            @Override
            public void run() {


                if (ticks == 0) {
                    broadcastTitle(Title.title(Component.text("OVERTIME", NamedTextColor.RED), Component.text("HP Decay is active", NamedTextColor.GRAY)));
                    broadcastSound(Sound.ENTITY_WITHER_DEATH, .2f, .5f);
                    ticks++;
                    return;
                }

                else if (ticks > 25) {
                    SMPDuels.getInstance().getDuelArenaLocation().createExplosion(20, false, false);
                    ticks++;
                    return;
                }

                for (Player p : getPlayers())
                    addPlayerHPDecayStack(p);

                ticks++;
            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                for (Player p : getPlayers()) {

                    if (p.getAttribute(Attribute.GENERIC_MAX_HEALTH) == null)
                        continue;

                    clearHPDecayModifier(p);
                }
                super.cancel();
            }
        };
        countdownTask.runTaskTimer(SMPDuels.getInstance(), 20*90, 60);

    }

    public void stop() {

        for (Player p : getPlayers())
            p.teleport(SMPDuels.getInstance().getDuelArenaLocation().clone().add(-10, 10, 0));

        cleanup();
        DuelEndedEvent event = new DuelEndedEvent();
        SMPDuels.getInstance().getServer().getPluginManager().callEvent(event);
    }

    private void cleanup() {

        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }

        for (Player p : getPlayers())
            removePlayer(p);
    }

}

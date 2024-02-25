package me.devvy.smpevents.player;

import me.devvy.smpevents.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Wrapper for a player, used to manage them while we are keeping track of them
 */
public class EventPlayer {

    private final UUID playerID;  // Their ID, this is what we store to keep track of them just in case of logouts
    private final PlayerState smpSnapshot;  // Snapshot of their state before coming here so we can restore them

    // Should we keep them full hunger always?
    private boolean keepFed = true;
    private boolean dropItemsOnDeath = false;

    private boolean playingGame = false;  // Is this player playing a game?

    public EventPlayer(Player player) {
        this.playerID = player.getUniqueId();
        this.smpSnapshot = new PlayerState(player);
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerID);
    }

    /**
     * Call this when we enter the event area from the SMP world
     */
    public void enter() {

        Player p = getPlayer();
        if (p == null)
            return;

        p.setGameMode(GameMode.ADVENTURE);
        heal();
        p.setInvulnerable(true);
        p.closeInventory();
    }

    /**
     * Call this when we leave the event area back into the smp world
     */
    public void exit() {

        Player p = getPlayer();
        if (p == null)
            return;

        smpSnapshot.sync(p);
        p.setGameMode(GameMode.SURVIVAL);

        p.sendMessage(
                ComponentUtil.getEventPrefix()
                        .append(Component.text("Going back to survival! Come back any time with ", ComponentUtil.GRAY))
                        .append(Component.text("/event", ComponentUtil.GREEN, TextDecoration.BOLD))
                        .append(Component.text("!", ComponentUtil.GRAY))
        );
        p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        p.setInvulnerable(false);
        p.closeInventory();
    }

    public void heal() {

        Player p = getPlayer();
        if (p == null)
            return;

        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        feed();

    }

    public void feed() {

        Player p = getPlayer();
        if (p == null)
            return;

        p.setFoodLevel(20);
        p.setSaturation(10);
    }

    public boolean isKeepFed() {
        return keepFed;
    }

    public void setKeepFed(boolean keepFed) {
        this.keepFed = keepFed;
    }

    public boolean isPlayingGame() {
        return playingGame;
    }

    public void setPlayingGame(boolean playingGame) {
        this.playingGame = playingGame;
    }

    public boolean shouldDropItemsOnDeath() {
        return dropItemsOnDeath;
    }

    public void setDropItemsOnDeath(boolean dropItemsOnDeath) {
        this.dropItemsOnDeath = dropItemsOnDeath;
    }
}

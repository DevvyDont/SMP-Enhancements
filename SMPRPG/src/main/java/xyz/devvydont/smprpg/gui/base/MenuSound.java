package xyz.devvydont.smprpg.gui.base;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * A menu sound effect.
 */
public final class MenuSound {
    private final Player player;
    private final Sound sound;
    private final float volume;
    private final float pitch;

    MenuSound(Player player, Sound sound, float volume, float pitch) {
        this.player = player;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Plays the sound effect on the players' client.
     */
    public void play() {
        this.player.playSound(this.player.getLocation(), this.sound, this.volume, this.pitch);
    }
}

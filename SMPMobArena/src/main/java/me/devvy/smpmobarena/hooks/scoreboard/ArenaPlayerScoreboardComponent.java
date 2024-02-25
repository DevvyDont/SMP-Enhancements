package me.devvy.smpmobarena.hooks.scoreboard;

import me.devvy.betterscoreboards.scoreboards.DynamicScoreboardComponent;
import me.devvy.smpmobarena.player.ArenaPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.attribute.Attribute;

public class ArenaPlayerScoreboardComponent extends DynamicScoreboardComponent {

    private final ArenaPlayer arenaPlayer;
    private int tick = 0;  // Hack to allow flashing red hp bar when low hp

    public ArenaPlayerScoreboardComponent(ArenaPlayer arenaPlayer) {
        super();
        this.arenaPlayer = arenaPlayer;
        refresh();
    }

    public ArenaPlayer getArenaPlayer() {
        return arenaPlayer;
    }

    public double getHealth() {

        // If their player state is dead, return 0
        if (arenaPlayer.getState() == ArenaPlayer.ArenaPlayerState.DEAD)
            return 0;

        // Otherwise return their actual health
        return arenaPlayer.getPlayer().getHealth() + arenaPlayer.getPlayer().getAbsorptionAmount();
    }

    public double getMaxHealth() {
        return arenaPlayer.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }

    public float getHealthPercentage() {
        return (float) (getHealth() / getMaxHealth());
    }

    /**
     * Returns the color of the health bar based on the health percentage
     *
     * When we are at over max health, we return aqua
     * When we are dead, we return dark gray
     * When we are below 20% HP, we return a shade of red depending on the current tick to emulate a flashing effect
     * Otherwise, we lerp between dark orange and green based on the health percentage
     *
     * @return
     */
    public TextColor lerpedHealthColor() {

        // Player has overhealth
        if (getHealthPercentage() > 1.0f)
            return NamedTextColor.AQUA;

        // Player is dead
        if (arenaPlayer.getState() == ArenaPlayer.ArenaPlayerState.DEAD)
            return NamedTextColor.DARK_GRAY;

        // Low HP flashing effect
        if (getHealthPercentage() <= 0.2f) {
            // Flashing red effect
            if (tick % 2 == 0)
                return NamedTextColor.DARK_RED;
            else
                return NamedTextColor.DARK_GRAY;
        }

        // Lerp between orange and green depending on health percentage
        float hpPerc = getHealthPercentage();

        if (hpPerc < 0.5f)
            return TextColor.lerp(hpPerc*2, TextColor.color(255, 100, 0), TextColor.color(255, 255, 0));
        else
             return TextColor.lerp((hpPerc-.5f) * 2, TextColor.color(255, 255, 0), TextColor.color(0, 255, 0));

    }

    public Component getIcon() {

        // If the player is dead, return a skull and crossbones
        if (arenaPlayer.getState() == ArenaPlayer.ArenaPlayerState.DEAD)
            return Component.text("\u2620").color(NamedTextColor.GRAY);

        // Otherwise return a heart with the health color
        return Component.text("\u2764").color(NamedTextColor.RED);

    }

    public Component getHealthComponent() {

        // If the player is dead, return DEAD in dark gray
        if (arenaPlayer.getState() == ArenaPlayer.ArenaPlayerState.DEAD)
            return Component.text("DEAD").color(NamedTextColor.DARK_GRAY);

        // Otherwise return the health in the health color
        return Component.text(String.format("%2s", (int) Math.ceil(getHealth()))).color(lerpedHealthColor());
    }

    @Override
    public void refresh() {

        tick++;

        if (arenaPlayer == null)
            return;

        Component name = Component.text("- ", NamedTextColor.DARK_GRAY).append(Component.text(String.format("%-14s", arenaPlayer.getPlayer().getName())).color(NamedTextColor.AQUA));
        setComponent(name.append(getIcon()).append(getHealthComponent()));

    }

}

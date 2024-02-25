package me.devvy.smpmobarena.hooks.scoreboard;

import me.devvy.betterscoreboards.scoreboards.DynamicScoreboardComponent;
import me.devvy.smpmobarena.arena.MobArena;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class EnemiesRemainingScoreboardComponent extends DynamicScoreboardComponent {

    private final MobArena arena;

    public EnemiesRemainingScoreboardComponent(MobArena arena) {
        this.arena = arena;
    }

    public Component getEnemiesRemainingComponent() {
        int enemiesRemaining = arena.getGameplayManager().getWaveManager().getEnemiesRemaining();
        return Component.text("Enemies remaning: ", NamedTextColor.GRAY).append(Component.text(enemiesRemaining, NamedTextColor.RED, TextDecoration.BOLD));
    }


    @Override
    public void refresh() {
        setComponent(getEnemiesRemainingComponent());
    }
}


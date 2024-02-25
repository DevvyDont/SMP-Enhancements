package me.devvy.smpmobarena.hooks.scoreboard;

import me.devvy.betterscoreboards.scoreboards.DynamicScoreboardComponent;
import me.devvy.smpmobarena.arena.MobArena;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ArenaWaveComponent extends DynamicScoreboardComponent {

    private final MobArena arena;

    public ArenaWaveComponent(MobArena arena) {
        super(Component.empty());
        this.arena = arena;
    }

    public Component getWaveComponent() {
        return Component.text("Wave: ", NamedTextColor.GRAY, TextDecoration.BOLD)
                .append(Component.text(arena.getGameplayManager().getWaveManager().getCurrentWave(), NamedTextColor.AQUA, TextDecoration.BOLD))
                .append(Component.text("/", NamedTextColor.DARK_GRAY)).decoration(TextDecoration.BOLD, false)
                .append(Component.text(arena.getGameplayManager().getWaveManager().getMaxWave(), NamedTextColor.DARK_GRAY));
    }

    @Override
    public void refresh() {
        setComponent(getWaveComponent());
    }
}

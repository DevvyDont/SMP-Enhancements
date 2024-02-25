package me.devvy.smpmobarena.hooks.scoreboard;

import me.devvy.betterscoreboards.scoreboards.DynamicScoreboardComponent;
import me.devvy.smpmobarena.arena.MobArena;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.concurrent.TimeUnit;

public class TimeScoreboardComponent extends DynamicScoreboardComponent {

    private MobArena arena;


    public TimeScoreboardComponent(MobArena arena) {
        super();
        this.arena = arena;
    }

    public String formatTimeHMS() {
        long millis = arena.getGameplayManager().getElapsedTimeMillis();
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public String formatTimeMS() {
        long millis = arena.getGameplayManager().getElapsedTimeMillis();
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    @Override
    public void refresh() {
        String timetext = arena.getGameplayManager().getElapsedTimeMillis() > 3600000 ? formatTimeHMS() : formatTimeMS();  // If the game has been going on for more than an hour, show hours
        setComponent(Component.text(String.format("%17s", timetext), NamedTextColor.DARK_GRAY, TextDecoration.ITALIC));
    }
}

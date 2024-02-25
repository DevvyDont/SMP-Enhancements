package me.devvy.smpmobarena.arena;

public class ArenaWaveManager {

    private final MobArena arena;

    public ArenaWaveManager(MobArena arena) {
        this.arena = arena;
    }

    public int getEnemiesRemaining() {
        return 0;
    }

    public int getCurrentWave() {
        return 0;
    }


    public int getMaxWave() {
        return 30;
    }
}

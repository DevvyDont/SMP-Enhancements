package xyz.devvydont.smprpg.services;

import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.unstable.listeners.DamageParticleRemover;

public class UnstableListenersService implements BaseService {

    final SMPRPG plugin;

    public UnstableListenersService(SMPRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean setup() {

        try {
            new DamageParticleRemover(plugin);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to instantiate damage particle remover. - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean required() {
        return false;
    }
}

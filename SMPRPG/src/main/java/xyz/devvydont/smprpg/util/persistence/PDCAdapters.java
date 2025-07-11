package xyz.devvydont.smprpg.util.persistence;

import xyz.devvydont.smprpg.util.persistence.adapters.RarityAdapter;
import xyz.devvydont.smprpg.util.persistence.adapters.SpawnerOptionsAdapter;
import xyz.devvydont.smprpg.util.persistence.adapters.UUIDAdapter;

/**
 * Holds various PDC adapter instances that can be used for easier PDC interfacing.
 */
public class PDCAdapters {

    public static final UUIDAdapter UUID = new UUIDAdapter();
    public static final RarityAdapter RARITY = new RarityAdapter();
    public static final SpawnerOptionsAdapter SPAWNER_OPTIONS = new SpawnerOptionsAdapter();

}

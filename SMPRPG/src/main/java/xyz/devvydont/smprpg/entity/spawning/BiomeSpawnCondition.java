package xyz.devvydont.smprpg.entity.spawning;

import org.bukkit.Location;
import org.bukkit.block.Biome;

public class BiomeSpawnCondition implements EntitySpawnCondition {

    private final Biome biome;

    public BiomeSpawnCondition(Biome biome) {
        this.biome = biome;
    }

    public Biome getBiome() {
        return biome;
    }

    @Override
    public boolean valid(Location location) {
        return location.getWorld().getBiome(location).equals(this.getBiome());
    }
}

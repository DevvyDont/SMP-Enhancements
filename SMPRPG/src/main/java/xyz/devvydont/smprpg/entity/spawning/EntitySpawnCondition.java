package xyz.devvydont.smprpg.entity.spawning;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;

import java.util.ArrayList;
import java.util.List;

public interface EntitySpawnCondition {

    boolean valid(Location location);

    boolean test(Location location);

    float chance();

    abstract class SpawnCondition implements EntitySpawnCondition {

        private float chance = 0;

        @Override
        public float chance() {
            return chance;
        }

        @Override
        public boolean test(Location location) {
            return valid(location) && Math.random() < chance;
        }

        public SpawnCondition withChance(float chance) {
            this.chance = chance;
            return this;
        }

    }

    class ImpossibleSpawnCondition extends SpawnCondition {

        public static ImpossibleSpawnCondition create() {
            return new ImpossibleSpawnCondition();
        }

        @Override
        public boolean valid(Location location) {
            return false;
        }
    }

    class BiomeSpawnCondition extends SpawnCondition {

        public static BiomeSpawnCondition biome(Biome biome) {
            return new BiomeSpawnCondition(biome);
        }

        private Biome _biome;

        public BiomeSpawnCondition(Biome biome) {
            this._biome = biome;
        }

        BiomeSpawnCondition withBiome(Biome biome) {
            this._biome = biome;
            return this;
        }

        @Override
        public boolean valid(Location location) {
            return location.getWorld().getBiome(location).equals(_biome);
        }
    }

    class StructureSpawnCondition extends SpawnCondition {

        public static StructureSpawnCondition structure(Structure structureType) {
            return new StructureSpawnCondition(structureType);
        }

        Structure structureType;

        public StructureSpawnCondition(Structure structureType) {
            this.structureType = structureType;
        }

        StructureSpawnCondition withStructure(Structure structureType) {
            this.structureType = structureType;
            return this;
        }

        @Override
        public boolean valid(Location location) {

            // Attempt to see if this entity is inside the structure
            for (GeneratedStructure structure : location.getChunk().getStructures(structureType))
                if (structure.getBoundingBox().contains(location.toVector()))
                    return true;

            return false;
        }
    }

    class ComplexSpawnCondition extends SpawnCondition {

        static EntitySpawnCondition withConditions(EntitySpawnCondition... conditions) {
            var condition = new ComplexSpawnCondition();
            for (EntitySpawnCondition other : conditions)
                condition = condition.withCondition(other);
            return condition;
        }
        private final List<EntitySpawnCondition> conditions = new ArrayList<>();

        ComplexSpawnCondition withCondition(EntitySpawnCondition condition) {
            this.conditions.add(condition);
            return this;
        }

        @Override
        public boolean test(Location location) {
            for (var condition : conditions)
                if (!condition.test(location))
                    return false;
            return true;
        }

        @Override
        public boolean valid(Location location) {
            for (var condition : this.conditions)
                if (!condition.valid(location))
                    return false;
            return true;
        }
    }

}

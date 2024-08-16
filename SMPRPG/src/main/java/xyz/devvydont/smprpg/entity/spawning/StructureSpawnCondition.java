package xyz.devvydont.smprpg.entity.spawning;

import org.bukkit.Location;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;

public class StructureSpawnCondition implements EntitySpawnCondition {

    Structure structureType;

    public StructureSpawnCondition(Structure structureType) {
        this.structureType = structureType;
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

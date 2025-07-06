package xyz.devvydont.smprpg.listeners;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.LeveledEntitySpawnEvent;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

import java.util.HashMap;
import java.util.Map;

/**
 * When entities are spawned inside of structures, we want to make them certain levels based on the structure.
 */
public class StructureEntitySpawnListener extends ToggleableListener {

    public static Map<Structure, Integer> minimumStructureLevels = new HashMap<>();

    static {
        minimumStructureLevels.put(Structure.ANCIENT_CITY,    100);  // Ancient cities are the hardest content in the game
        minimumStructureLevels.put(Structure.END_CITY,        55);  // The end
        minimumStructureLevels.put(Structure.STRONGHOLD,      45);  // The end
        minimumStructureLevels.put(Structure.BASTION_REMNANT, 40);  // Piglins in bastions are meant to be terrifying to make netherite scary to obtain
        minimumStructureLevels.put(Structure.FORTRESS,        25);  // If the nether is 25, these should be ~10ish above
        minimumStructureLevels.put(Structure.TRIAL_CHAMBERS,  30);  // Trial chambers are endgame-ish
        minimumStructureLevels.put(Structure.MANSION,         25);  // Pillagers are 15-25
        minimumStructureLevels.put(Structure.PILLAGER_OUTPOST,20);  // Pillagers are 15-25
        minimumStructureLevels.put(Structure.MONUMENT,        18);  // Midgame boss
        minimumStructureLevels.put(Structure.MINESHAFT,       10);  // Early game structure
        minimumStructureLevels.put(Structure.MINESHAFT_MESA,  10);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_DESERT,  15);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_PLAINS,  15);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_SAVANNA, 15);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_SNOWY,   15);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_TAIGA,   15);  // Early game structure
        minimumStructureLevels.put(Structure.DESERT_PYRAMID,  10);  // Early game structure
        minimumStructureLevels.put(Structure.IGLOO,           10);  // Early game structure
        minimumStructureLevels.put(Structure.JUNGLE_PYRAMID,  10);  // Early game structure
        minimumStructureLevels.put(Structure.SWAMP_HUT,       10);  // Early game structure
    }


    @Override
    public void start() {
        super.start();

        // Create a task that checks if any players are in a structure. If they are, alert them.
        var plugin = SMPRPG.getInstance();
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers())
                    doPlayerLocationCheck(player);
            }
        }.runTaskTimerAsynchronously(plugin, 1, 2*50);
    }

    /**
     * Given a generated structure, determine the minimum level of entities within
     *
     * @param structure A generated structure from World#getStructures()
     * @return a number from 1-100 representing minimum level for entities. If -1 (or lower) this entity doesn't affect
     * entity levels
     */
    public static int getMinimumEntityLevel(GeneratedStructure structure) {
        return minimumStructureLevels.getOrDefault(structure.getStructure(), -1);
    }

    private Component getStructureComponent(Player player, GeneratedStructure structure, int power) {

        var key = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).getKey(structure.getStructure());
        var name = "???";
        if (key != null)
            name = MinecraftStringUtils.getTitledString(key.asMinimalString());

        // Create the base message.
        Component send = ComponentUtils.merge(
                ComponentUtils.create("Currently in "),
                ComponentUtils.create(name + " ", NamedTextColor.AQUA),
                ComponentUtils.powerLevelPrefix(power)
        );

        var plugin = SMPRPG.getInstance();
        // If the player is underleveled, add a warning label.
        if (power > SMPRPG.getService(EntityService.class).getPlayerInstance(player).getLevel())
            send = ComponentUtils.create("WARNING! ", NamedTextColor.RED).append(send);

        return send;
    }


    private void doPlayerLocationCheck(Player player) {

        var location = player.getLocation();
        var chunk = location.getChunk();

        // Determine highest level structure we are in. If -1, that means we are not in one
        GeneratedStructure mostDangerousStructure = null;
        int highestLevel = -1;
        for (var structure : chunk.getStructures()) {

            // Skip structures we aren't actually in
            if (!structure.getBoundingBox().overlaps(player.getBoundingBox()))
                continue;

            if (highestLevel < getMinimumEntityLevel(structure)) {
                mostDangerousStructure = structure;
                highestLevel = getMinimumEntityLevel(structure);
            }
        }

        // Don't do anything if we aren't in a structure
        if (mostDangerousStructure == null || highestLevel < 1)
            return;

        var plugin = SMPRPG.getInstance();
        SMPRPG.getService(ActionBarService.class).addActionBarComponent(player, ActionBarService.ActionBarSource.STRUCTURE, getStructureComponent(player, mostDangerousStructure, highestLevel), 5);
    }

    @EventHandler
    public void onLeveledEntitySpawn(LeveledEntitySpawnEvent event) {

        Location location = event.getEntity().getEntity().getLocation();
        int originalLevel = event.getEntity().getLevel();
        int structureLevel = originalLevel;

        // Loop through all the structures in this chunk. If this entity spawned inside of a structure, make sure
        // they are at the minimum level at least
        for (GeneratedStructure structure : location.getChunk().getStructures())
            // Is the entity's bounding box in the structures bounding box?
            if (structure.getBoundingBox().overlaps(event.getEntity().getEntity().getBoundingBox()))
                // Does this structure have a minimum level definition?
                if (getMinimumEntityLevel(structure) >= 1)
                    structureLevel = Math.max(structureLevel, getMinimumEntityLevel(structure));

        // If there is a difference in structure level, use that level instead
        if (structureLevel == originalLevel)
            return;

        // After resolving the highest level based on current entity level and structures the entity was in, set it.
        event.getEntity().setLevel(structureLevel);
        event.getEntity().updateAttributes();
    }



}

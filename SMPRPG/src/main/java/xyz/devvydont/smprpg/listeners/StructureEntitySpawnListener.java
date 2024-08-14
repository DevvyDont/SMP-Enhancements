package xyz.devvydont.smprpg.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.LeveledEntitySpawnEvent;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

import java.util.*;

/**
 * When entities are spawned inside of structures, we want to make them certain levels based on the structure.
 */
public class StructureEntitySpawnListener implements Listener {

    public static Map<Structure, Integer> minimumStructureLevels;

    private final SMPRPG plugin;

    public StructureEntitySpawnListener(SMPRPG plugin) {

        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        minimumStructureLevels = new HashMap<>();

        minimumStructureLevels.put(Structure.ANCIENT_CITY,    99);  // Ancient cities are the hardest content in the game
        minimumStructureLevels.put(Structure.BASTION_REMNANT, 80);  // Piglins in bastions are meant to be terrifying to make netherite scary to obtain
        minimumStructureLevels.put(Structure.TRIAL_CHAMBERS,  70);  // Trial chambers are endgame-ish
        minimumStructureLevels.put(Structure.END_CITY,        60);  // The end
        minimumStructureLevels.put(Structure.STRONGHOLD,      45);  // The end
        minimumStructureLevels.put(Structure.FORTRESS,        35);  // If the nether is 25, these should be ~10ish above
        minimumStructureLevels.put(Structure.MONUMENT,        30);  // Midgame boss
        minimumStructureLevels.put(Structure.MANSION,         25);  // Pillagers are 15-25
        minimumStructureLevels.put(Structure.PILLAGER_OUTPOST,20);  // Pillagers are 15-25
        minimumStructureLevels.put(Structure.MINESHAFT,       15);  // Early game structure
        minimumStructureLevels.put(Structure.MINESHAFT_MESA,  15);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_DESERT,  15);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_PLAINS,  15);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_SAVANNA, 15);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_SNOWY,   15);  // Early game structure
        minimumStructureLevels.put(Structure.VILLAGE_TAIGA,   15);  // Early game structure
        minimumStructureLevels.put(Structure.DESERT_PYRAMID,  10);  // Early game structure
        minimumStructureLevels.put(Structure.IGLOO,           10);  // Early game structure
        minimumStructureLevels.put(Structure.JUNGLE_PYRAMID,  10);  // Early game structure
        minimumStructureLevels.put(Structure.SWAMP_HUT,       10);  // Early game structure

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
        Component send = Component.text("Currently in").color(NamedTextColor.GRAY)
                .append(Component.text(" " + MinecraftStringUtils.getTitledString(structure.getStructure().key().value() + " ")).color(NamedTextColor.AQUA)
                        .append(ChatUtil.getBracketedPowerComponent(power)));

        if (power > plugin.getEntityService().getPlayerInstance(player).getLevel())
            send = Component.text("WARNING! ").color(NamedTextColor.RED).append(send);
        return send;
    }


    private void doPlayerLocationCheck(Player player) {

        Location location = player.getLocation();
        Chunk chunk = location.getChunk();

        // Determine highest level structure we are in. If -1, that means we are not in one
        GeneratedStructure mostDangerousStructure = null;
        int highestLevel = -1;
        for (GeneratedStructure structure : chunk.getStructures()) {

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

        plugin.getActionBarService().addActionBarComponent(player, ActionBarService.ActionBarSource.STRUCTURE, getStructureComponent(player, mostDangerousStructure, highestLevel), 5);
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

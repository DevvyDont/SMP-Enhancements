package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.util.world.ChunkUtil;

public class MiningExperienceListener implements Listener {


    public static int getBaseExperienceForBlock(Block block) {

        int dimensionMultiplier = block.getWorld().getEnvironment().equals(World.Environment.THE_END) ? 3 :
                block.getWorld().getEnvironment().equals(World.Environment.NETHER) ? 2 : 1;

        return switch (block.getType()) {

            case END_STONE, STONE, COBBLESTONE, SAND, RED_SAND, SANDSTONE, RED_SANDSTONE, CLAY, MYCELIUM, GRASS_BLOCK, DIRT, GRAVEL, DEEPSLATE, TUFF, NETHERRACK, BLACKSTONE, BASALT, SMOOTH_BASALT, CRIMSON_NYLIUM, WARPED_NYLIUM -> 1;
            case ANDESITE, DIORITE, GRANITE, CALCITE, QUARTZ_BLOCK, BONE_BLOCK, SOUL_SAND, SOUL_SOIL, ICE, PACKED_ICE -> 2;

            case SEA_LANTERN -> 25;
            case PRISMARINE -> 2;
            case PRISMARINE_BRICKS, DARK_PRISMARINE -> 3;
            case WET_SPONGE -> 100;

            case BLUE_ICE -> 4;

            case COAL_ORE -> 8;
            case DEEPSLATE_COAL_ORE -> 15;

            case COPPER_ORE -> 10;
            case DEEPSLATE_COPPER_ORE -> 20;

            case IRON_ORE -> 15;
            case DEEPSLATE_IRON_ORE -> 28;

            case GOLD_ORE -> 24;
            case DEEPSLATE_GOLD_ORE -> 45;

            case OBSIDIAN -> 80;
            case CRYING_OBSIDIAN -> 150;

            case AMETHYST_BLOCK -> 12;
            case LARGE_AMETHYST_BUD -> 55;
            case MEDIUM_AMETHYST_BUD -> 40;
            case SMALL_AMETHYST_BUD -> 22;

            case LAPIS_ORE, REDSTONE_ORE -> 45;
            case DEEPSLATE_LAPIS_ORE, DEEPSLATE_REDSTONE_ORE -> 70;

            case DIAMOND_ORE -> 100;
            case DEEPSLATE_DIAMOND_ORE -> 180;

            case EMERALD_ORE -> 500;
            case DEEPSLATE_EMERALD_ORE -> 850;

            case GOLD_BLOCK -> 110;
            case EMERALD_BLOCK -> 1500;

            case MAGMA_BLOCK -> 40;
            case NETHER_GOLD_ORE -> 65;
            case GLOWSTONE -> 55;
            case NETHER_QUARTZ_ORE -> 60;
            case ANCIENT_DEBRIS -> 900;


            default -> 0;
        } * dimensionMultiplier;

    }


    final SMPRPG plugin;

    public MiningExperienceListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * When a player places a block, we need to mark that block as unable to be farmed for experience
     *
     * @param event
     */
    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {

        if (event.isCancelled())
            return;

        // When any block is placed, it is no longer able to earn skill experience
        ChunkUtil.markBlockSkillInvalid(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGainGeneralMiningExperience(BlockBreakEvent event) {

        if (event.isCancelled())
            return;

        // If this block isn't allowed to retrieve experience
        if (ChunkUtil.isBlockSkillInvalid(event.getBlock()))
            return;

        SkillInstance skill = plugin.getEntityService().getPlayerInstance(event.getPlayer()).getMiningSkill();
        skill.addExperience(getBaseExperienceForBlock(event.getBlock()));
        ChunkUtil.markBlockSkillValid(event.getBlock());
    }
}

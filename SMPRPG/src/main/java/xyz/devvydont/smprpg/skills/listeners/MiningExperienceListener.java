package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.util.world.ChunkUtil;

public class MiningExperienceListener implements Listener {


    public static int getBaseExperienceForDrop(ItemStack item, World.Environment environment) {

        int dimensionMultiplier = environment.equals(World.Environment.THE_END) ? 3 :
                environment.equals(World.Environment.NETHER) ? 2 : 1;

        return switch (item.getType()) {

            case END_STONE, STONE, COBBLESTONE, COBBLED_DEEPSLATE, SAND, RED_SAND, SANDSTONE, RED_SANDSTONE, CLAY, MYCELIUM, GRASS_BLOCK, DIRT, GRAVEL, DEEPSLATE, TUFF, NETHERRACK, BLACKSTONE, BASALT, SMOOTH_BASALT, CRIMSON_NYLIUM, WARPED_NYLIUM -> 1;
            case ANDESITE, DIORITE, GRANITE, CALCITE, BONE_BLOCK, SOUL_SAND, SOUL_SOIL, ICE, PACKED_ICE -> 2;

            case SEA_LANTERN -> 25;
            case PRISMARINE -> 2;
            case PRISMARINE_BRICKS, DARK_PRISMARINE -> 3;
            case WET_SPONGE -> 100;

            case BLUE_ICE -> 4;

            case COAL_ORE, COAL -> 8;
            case DEEPSLATE_COAL_ORE -> 15;

            case COPPER_ORE, RAW_COPPER, COPPER_INGOT -> 10;
            case DEEPSLATE_COPPER_ORE -> 20;

            case IRON_ORE, RAW_IRON, IRON_INGOT -> 15;
            case IRON_BLOCK -> 25;
            case DEEPSLATE_IRON_ORE -> 28;

            case GOLD_ORE, RAW_GOLD, GOLD_INGOT -> 24;
            case DEEPSLATE_GOLD_ORE -> 45;

            case OBSIDIAN -> 80;
            case CRYING_OBSIDIAN -> 150;

            case AMETHYST_BLOCK -> 7;
            case LARGE_AMETHYST_BUD -> 15;
            case MEDIUM_AMETHYST_BUD -> 25;
            case SMALL_AMETHYST_BUD -> 35;

            case LAPIS_LAZULI -> 8;
            case REDSTONE -> 8;

            case REDSTONE_BLOCK -> 35;

            case LAPIS_ORE, REDSTONE_ORE -> 45;
            case DEEPSLATE_LAPIS_ORE, DEEPSLATE_REDSTONE_ORE -> 70;

            case DIAMOND_ORE, DIAMOND -> 100;
            case DEEPSLATE_DIAMOND_ORE -> 180;

            case EMERALD_ORE, EMERALD -> 500;
            case DEEPSLATE_EMERALD_ORE -> 850;

            case GOLD_BLOCK -> 110;
            case EMERALD_BLOCK -> 1500;

            case MAGMA_BLOCK -> 9;
            case GOLD_NUGGET -> 10;
            case NETHER_GOLD_ORE -> 65;
            case GLOWSTONE -> 55;
            case NETHER_QUARTZ_ORE -> 60;
            case QUARTZ -> 15;
            case QUARTZ_BLOCK -> 50;
            case ANCIENT_DEBRIS -> 500;


            default -> 0;
        } * dimensionMultiplier * item.getAmount();

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

        ChunkUtil.markBlockSkillValid(event.getBlock());

        SkillInstance skill = plugin.getEntityService().getPlayerInstance(event.getPlayer()).getMiningSkill();

        int exp = 0;
        for (ItemStack drop : event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer()))
            exp += getBaseExperienceForDrop(drop, event.getPlayer().getWorld().getEnvironment());
        if (exp <= 0)
            return;

        skill.addExperience(exp, SkillExperienceGainEvent.ExperienceSource.ORE);
        ChunkUtil.markBlockSkillValid(event.getBlock());
    }
}

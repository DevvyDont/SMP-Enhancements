package xyz.devvydont.smprpg.skills.listeners;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.craftbukkit.block.impl.CraftReed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.rewards.FarmingSkillRewards;
import xyz.devvydont.smprpg.util.world.ChunkUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class FarmingExperienceListener implements Listener {

    final SMPRPG plugin;

    public static int getExperienceValue(ItemStack item) {

        int experience = switch (item.getType()) {

            case FLOWERING_AZALEA, FLOWERING_AZALEA_LEAVES, POPPY, ROSE_BUSH, TALL_GRASS, SHORT_GRASS, SEAGRASS,
                 TALL_SEAGRASS, DANDELION, BLUE_ORCHID, ALLIUM, AZURE_BLUET, RED_TULIP, ORANGE_TULIP, PINK_TULIP,
                 WHITE_TULIP, OXEYE_DAISY, CORNFLOWER, LILY_OF_THE_VALLEY, LILY_PAD, PINK_PETALS, LILAC, PEONY, KELP, KELP_PLANT -> 1;

            case PITCHER_PLANT, PITCHER_CROP, PITCHER_POD -> 2;

            case COCOA_BEANS -> 1;

            case GLOW_BERRIES, SWEET_BERRIES -> 6;

            case BROWN_MUSHROOM, RED_MUSHROOM, DEAD_BUSH -> 3;

            case SPORE_BLOSSOM -> 34;

            case BAMBOO -> 2;

            case BROWN_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK -> 21;

            case SEA_PICKLE -> 45;

            case CRIMSON_FUNGUS, WARPED_FUNGUS -> 65;

            case WITHER_ROSE -> 20;

            case MELON -> 12;
            case MELON_SEEDS -> 1;

            case PUMPKIN -> 10;
            case PUMPKIN_SEEDS -> 1;

            case BEETROOT, BEETROOTS -> 3;
            case BEETROOT_SEEDS -> 1;

            case TORCHFLOWER, TORCHFLOWER_CROP -> 15;
            case TORCHFLOWER_SEEDS -> 1;

            case WHEAT -> 6;
            case WHEAT_SEEDS -> 1;

            case CARROT, CARROTS, POTATO, POTATOES -> 5;

            case NETHER_WART -> 13;

            case SUGAR_CANE -> 5;
            case CACTUS -> 8;

            default -> 0;
        };

        return experience * item.getAmount();

    }

    public FarmingExperienceListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHarvestCrop(PlayerHarvestBlockEvent event) {

        if (event.isCancelled())
            return;

        int exp = 0;
        for (ItemStack item : event.getItemsHarvested())
            exp += getExperienceValue(item);

        LeveledPlayer player = plugin.getEntityService().getPlayerInstance(event.getPlayer());
        player.getFarmingSkill().addExperience(exp, SkillExperienceGainEvent.ExperienceSource.HARVEST);
    }

    private int getExperienceForDrops(Collection<ItemStack> drops) {

        // Loop through every drop from breaking this block and award XP
        int exp = 0;
        for (ItemStack item : drops)
            exp += getExperienceValue(item);
        return exp;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onHarvestBlock(BlockBreakEvent event) {

        // If this block is marked as skill invalid, (somebody placed this block) don't give xp but set block
        // as valid
        if (ChunkUtil.isBlockSkillInvalid(event.getBlock())) {
            ChunkUtil.markBlockSkillValid(event.getBlock());
            return;
        }

        // Loop through every drop from breaking this block and award XP
        int exp = getExperienceForDrops(event.getBlock().getDrops());
        if (exp <= 0)
            return;

        // If the block is ageable don't give xp unless it is fully matured
        if (event.getBlock().getBlockData() instanceof Ageable ageable)
            if (ageable.getMaximumAge() != ageable.getAge())
                return;

        LeveledPlayer player = plugin.getEntityService().getPlayerInstance(event.getPlayer());
        player.getFarmingSkill().addExperience(exp, SkillExperienceGainEvent.ExperienceSource.HARVEST);
    }

    /**
     * Used to give experience to sugar cane/kelp/bamboo breaks. We completely override vanilla behavior
     * so we can properly hook into the "chain break" effect to give experience
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreakBambooSugarCane(BlockBreakEvent event) {

        Material blockType = event.getBlock().getType();

        // Listen to when sugar cane or bamboo is broken
        if (!(blockType.equals(Material.SUGAR_CANE) || blockType.equals(Material.BAMBOO) || blockType.equals(Material.KELP_PLANT) || blockType.equals(Material.CACTUS)))
            return;

        // We are going to manually break all the blocks.
        event.setCancelled(true);
        SkillInstance farming = plugin.getEntityService().getPlayerInstance(event.getPlayer()).getFarmingSkill();
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();

        // Loop through every block above this and do the same a certain amount of ticks later
        for (int y = event.getBlock().getY(); y <= event.getBlock().getWorld().getMaxHeight(); y++) {

            int yOffset = y - event.getBlock().getY();

            Block block = event.getBlock().getRelative(BlockFace.UP, yOffset);
            // If this block doesn't match the original block, stop checking
            if (!block.getType().equals(blockType))
                return;

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                block.getWorld().playSound(block.getLocation(), block.getBlockSoundGroup().getBreakSound(), 1, 1);
                Collection<ItemStack> laterDrops = block.getDrops(tool);
                farming.addExperience(getExperienceForDrops(laterDrops), SkillExperienceGainEvent.ExperienceSource.HARVEST);
                block.setType(Material.AIR, false);
                for (ItemStack drop : laterDrops)
                    block.getWorld().dropItemNaturally(block.getLocation(), drop);
            }, yOffset);

        }

    }

}

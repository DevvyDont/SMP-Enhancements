package xyz.devvydont.smprpg.util.world;

import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.devvydont.smprpg.SMPRPG;

// todo make all this utilize persistent data containers to persist on server restart. for now its fine to use metadata
public class ChunkUtil {

    public static final String SKILL_INVALID_TAG = "#smprpg:skill_invalid";

    /**
     * Given a block, mark it in the chunk as invalid to be mined for experience
     * This happens when a block is placed in the world by a player
     *
     * @param block
     */
    public static void markBlockSkillInvalid(Block block) {
        block.setMetadata(SKILL_INVALID_TAG, new FixedMetadataValue(SMPRPG.getInstance(), true));
    }

    /**
     * Given a block, mark it as able to earn experience again
     *
     * @param block
     */
    public static void markBlockSkillValid(Block block) {
        block.removeMetadata(SKILL_INVALID_TAG, SMPRPG.getInstance());
    }

    /**
     * Given a block, determine if it is not allowed to earn experience when broken
     *
     * @param block
     * @return
     */
    public static boolean isBlockSkillInvalid(Block block) {
        return block.hasMetadata(SKILL_INVALID_TAG);
    }

}

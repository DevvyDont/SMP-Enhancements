package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class PrismarineShardFamilyBlueprint extends CustomCompressableBlueprint  {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.PRISMARINE_SHARD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_PRISMARINE_SHARD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_PRISMARINE_SHARD))
    );

    public PrismarineShardFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }
    
}

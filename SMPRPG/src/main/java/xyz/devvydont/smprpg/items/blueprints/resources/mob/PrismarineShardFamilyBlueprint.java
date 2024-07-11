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

    public static final CustomItemType[] CUSTOM_PRISMARINE_SHARD_MATERIALS = {
            CustomItemType.PREMIUM_PRISMARINE_SHARD,
            CustomItemType.ENCHANTED_PRISMARINE_SHARD
    };

    private final CustomItemType type;

    public PrismarineShardFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService);
        this.type = type;
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }

    @Override
    public CustomItemType getCustomItemType() {
        return type;
    }
    
}

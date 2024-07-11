package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class EchoShardFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.ECHO_SHARD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_ECHO_SHARD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_ECHO_SHARD))
    );

    public static final CustomItemType[] CUSTOM_ECHO_SHARD_MATERIALS = {
            CustomItemType.PREMIUM_ECHO_SHARD,
            CustomItemType.ENCHANTED_ECHO_SHARD
    };

    private final CustomItemType type;

    public EchoShardFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class RedstoneFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.REDSTONE)),
            new CompressionRecipeMember(new MaterialWrapper(Material.REDSTONE_BLOCK)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_REDSTONE)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_REDSTONE_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_REDSTONE)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_REDSTONE_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.REDSTONE_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_REDSTONE_MATERIALS = {
//            CustomItemType.COMPRESSED_REDSTONE,
//            CustomItemType.COMPRESSED_REDSTONE_BLOCK,
            CustomItemType.ENCHANTED_REDSTONE,
            CustomItemType.ENCHANTED_REDSTONE_BLOCK,
            CustomItemType.REDSTONE_SINGULARITY
    };

    private final CustomItemType type;

    public RedstoneFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class DiamondFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.DIAMOND)),
            new CompressionRecipeMember(new MaterialWrapper(Material.DIAMOND_BLOCK)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_DIAMOND)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_DIAMOND_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_DIAMOND)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_DIAMOND_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.DIAMOND_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_DIAMOND_MATERIALS = {
//            CustomItemType.COMPRESSED_DIAMOND,
//            CustomItemType.COMPRESSED_DIAMOND_BLOCK,
            CustomItemType.ENCHANTED_DIAMOND,
            CustomItemType.ENCHANTED_DIAMOND_BLOCK,
            CustomItemType.DIAMOND_SINGULARITY
    };

    private final CustomItemType type;

    public DiamondFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

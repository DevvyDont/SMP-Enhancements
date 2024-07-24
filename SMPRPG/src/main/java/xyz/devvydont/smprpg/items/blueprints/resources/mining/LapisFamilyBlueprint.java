package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class LapisFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.LAPIS_LAZULI)),
            new CompressionRecipeMember(new MaterialWrapper(Material.LAPIS_BLOCK)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_LAPIS)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_LAPIS_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_LAPIS)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_LAPIS_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.LAPIS_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_LAPIS_MATERIALS = {
//            CustomItemType.COMPRESSED_LAPIS,
//            CustomItemType.COMPRESSED_LAPIS_BLOCK,
            CustomItemType.ENCHANTED_LAPIS,
            CustomItemType.ENCHANTED_LAPIS_BLOCK,
            CustomItemType.LAPIS_SINGULARITY
    };

    private final CustomItemType type;

    public LapisFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

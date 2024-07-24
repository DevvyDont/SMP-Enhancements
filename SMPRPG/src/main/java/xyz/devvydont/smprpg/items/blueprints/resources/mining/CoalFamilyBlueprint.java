package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class CoalFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.COAL)),
            new CompressionRecipeMember(new MaterialWrapper(Material.COAL_BLOCK)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_COAL)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_COAL_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_COAL)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_COAL_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COAL_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_COAL_MATERIALS = {
//            CustomItemType.COMPRESSED_COAL,
//            CustomItemType.COMPRESSED_COAL_BLOCK,
            CustomItemType.ENCHANTED_COAL,
            CustomItemType.ENCHANTED_COAL_BLOCK,
            CustomItemType.COAL_SINGULARITY
    };

    private final CustomItemType type;

    public CoalFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

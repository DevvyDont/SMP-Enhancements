package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class CopperFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.COPPER_INGOT)),
            new CompressionRecipeMember(new MaterialWrapper(Material.COPPER_BLOCK)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_COPPER)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_COPPER_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_COPPER)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_COPPER_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COPPER_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_COPPER_MATERIALS = {
//            CustomItemType.COMPRESSED_COPPER,
//            CustomItemType.COMPRESSED_COPPER_BLOCK,
            CustomItemType.ENCHANTED_COPPER,
            CustomItemType.ENCHANTED_COPPER_BLOCK,
            CustomItemType.COPPER_SINGULARITY
    };

    private final CustomItemType type;

    public CopperFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

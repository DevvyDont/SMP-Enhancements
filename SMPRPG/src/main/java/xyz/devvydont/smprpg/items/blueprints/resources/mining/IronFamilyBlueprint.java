package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class IronFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.IRON_INGOT)),
            new CompressionRecipeMember(new MaterialWrapper(Material.IRON_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_IRON)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_IRON_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_IRON)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_IRON_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.IRON_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_IRON_MATERIALS = {
            CustomItemType.COMPRESSED_IRON,
            CustomItemType.COMPRESSED_IRON_BLOCK,
            CustomItemType.ENCHANTED_IRON,
            CustomItemType.ENCHANTED_IRON_BLOCK,
            CustomItemType.IRON_SINGULARITY
    };

    private final CustomItemType type;

    public IronFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

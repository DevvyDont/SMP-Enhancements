package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class GlowstoneFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.GLOWSTONE_DUST)),
            new CompressionRecipeMember(new MaterialWrapper(Material.GLOWSTONE)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_GLOWSTONE)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_GLOWSTONE_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_GLOWSTONE)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_GLOWSTONE_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.GLOWSTONE_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_GLOWSTONE_MATERIALS = {
            CustomItemType.COMPRESSED_GLOWSTONE,
            CustomItemType.COMPRESSED_GLOWSTONE_BLOCK,
            CustomItemType.ENCHANTED_GLOWSTONE,
            CustomItemType.ENCHANTED_GLOWSTONE_BLOCK,
            CustomItemType.GLOWSTONE_SINGULARITY
    };

    private final CustomItemType type;

    public GlowstoneFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

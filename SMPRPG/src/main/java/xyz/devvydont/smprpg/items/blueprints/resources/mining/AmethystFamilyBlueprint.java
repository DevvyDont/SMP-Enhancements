package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class AmethystFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.AMETHYST_SHARD)),
            new CompressionRecipeMember(new MaterialWrapper(Material.AMETHYST_BLOCK)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_AMETHYST)),
//            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_AMETHYST_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_AMETHYST)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_AMETHYST_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.AMETHYST_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_AMETHYST_MATERIALS = {
//            CustomItemType.COMPRESSED_AMETHYST,
//            CustomItemType.COMPRESSED_AMETHYST_BLOCK,
            CustomItemType.ENCHANTED_AMETHYST,
            CustomItemType.ENCHANTED_AMETHYST_BLOCK,
            CustomItemType.AMETHYST_SINGULARITY
    };

    private final CustomItemType type;

    public AmethystFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

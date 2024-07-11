package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class FlintFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.FLINT)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_FLINT)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_FLINT))
    );

    public static final CustomItemType[] CUSTOM_FLINT_MATERIALS = {
            CustomItemType.COMPRESSED_FLINT,
            CustomItemType.ENCHANTED_FLINT,
    };

    private final CustomItemType type;

    public FlintFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

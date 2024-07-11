package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class GoldFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.GOLD_INGOT)),
            new CompressionRecipeMember(new MaterialWrapper(Material.GOLD_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_GOLD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_GOLD_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_GOLD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_GOLD_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.GOLD_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_GOLD_MATERIALS = {
            CustomItemType.COMPRESSED_GOLD,
            CustomItemType.COMPRESSED_GOLD_BLOCK,
            CustomItemType.ENCHANTED_GOLD,
            CustomItemType.ENCHANTED_GOLD_BLOCK,
            CustomItemType.GOLD_SINGULARITY
    };

    private final CustomItemType type;

    public GoldFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

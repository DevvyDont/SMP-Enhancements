package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class EmeraldFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.EMERALD)),
            new CompressionRecipeMember(new MaterialWrapper(Material.EMERALD_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_EMERALD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COMPRESSED_EMERALD_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_EMERALD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_EMERALD_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.EMERALD_SINGULARITY))
    );

    public static final CustomItemType[] CUSTOM_EMERALD_MATERIALS = {
            CustomItemType.COMPRESSED_EMERALD,
            CustomItemType.COMPRESSED_EMERALD_BLOCK,
            CustomItemType.ENCHANTED_EMERALD,
            CustomItemType.ENCHANTED_EMERALD_BLOCK,
            CustomItemType.EMERALD_SINGULARITY
    };

    private final CustomItemType type;

    public EmeraldFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

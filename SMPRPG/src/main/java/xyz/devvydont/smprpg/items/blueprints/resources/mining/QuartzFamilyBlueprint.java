package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class QuartzFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.QUARTZ), 4),
            new CompressionRecipeMember(new MaterialWrapper(Material.QUARTZ_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_QUARTZ)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_QUARTZ_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.QUARTZ_SINGULARITY))
    );

    public QuartzFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }
    
}

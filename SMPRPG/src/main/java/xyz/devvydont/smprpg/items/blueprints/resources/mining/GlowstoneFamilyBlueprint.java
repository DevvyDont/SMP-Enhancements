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
            new CompressionRecipeMember(new MaterialWrapper(Material.GLOWSTONE_DUST), 4),
            new CompressionRecipeMember(new MaterialWrapper(Material.GLOWSTONE), 9),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_GLOWSTONE), 9),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_GLOWSTONE_BLOCK), 9),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.GLOWSTONE_SINGULARITY), 9)
    );

    public GlowstoneFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }
    
}

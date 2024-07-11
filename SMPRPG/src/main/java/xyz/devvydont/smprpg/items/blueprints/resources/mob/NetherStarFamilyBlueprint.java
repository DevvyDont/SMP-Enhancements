package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class NetherStarFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.NETHER_STAR)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_NETHER_STAR)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_NETHER_STAR))
    );

    public static final CustomItemType[] CUSTOM_NETHER_STAR_MATERIALS = {
            CustomItemType.PREMIUM_NETHER_STAR,
            CustomItemType.ENCHANTED_NETHER_STAR
    };

    private final CustomItemType type;

    public NetherStarFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

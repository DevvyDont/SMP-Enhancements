package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class EnderPearlFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.ENDER_PEARL)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_ENDER_PEARL)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_ENDER_PEARL))
    );

    public static final CustomItemType[] CUSTOM_ENDER_PEARL_MATERIALS = {
            CustomItemType.PREMIUM_ENDER_PEARL,
            CustomItemType.ENCHANTED_ENDER_PEARL
    };

    private final CustomItemType type;

    public EnderPearlFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

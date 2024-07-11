package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class BlazeRodFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.BLAZE_ROD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_BLAZE_ROD)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_BLAZE_ROD))
    );

    public static final CustomItemType[] CUSTOM_BLAZE_ROD_MATERIALS = {
            CustomItemType.PREMIUM_BLAZE_ROD,
            CustomItemType.ENCHANTED_BLAZE_ROD
    };

    private final CustomItemType type;

    public BlazeRodFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

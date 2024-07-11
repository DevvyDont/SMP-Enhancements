package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class LeatherFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.LEATHER)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_LEATHER)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_LEATHER))
    );

    public static final CustomItemType[] CUSTOM_LEATHER_MATERIALS = {
            CustomItemType.PREMIUM_LEATHER,
            CustomItemType.ENCHANTED_LEATHER
    };

    private final CustomItemType type;

    public LeatherFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class RabbitHideFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.RABBIT_HIDE)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_RABBIT_HIDE)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_RABBIT_HIDE))
    );

    public static final CustomItemType[] CUSTOM_RABBIT_HIDE_MATERIALS = {
            CustomItemType.PREMIUM_RABBIT_HIDE,
            CustomItemType.ENCHANTED_RABBIT_HIDE
    };

    private final CustomItemType type;

    public RabbitHideFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class PrismarineCrystalsFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.PRISMARINE_CRYSTALS)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_PRISMARINE_CRYSTAL)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL))
    );

    public static final CustomItemType[] CUSTOM_PRISMARINE_CRYSTALS_MATERIALS = {
            CustomItemType.PREMIUM_PRISMARINE_CRYSTAL,
            CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL
    };

    private final CustomItemType type;

    public PrismarineCrystalsFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

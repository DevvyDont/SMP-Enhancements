package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class AmethystFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.AMETHYST_SHARD), 4),
            new CompressionRecipeMember(new MaterialWrapper(Material.AMETHYST_BLOCK), 9),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_AMETHYST), 9),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_AMETHYST_BLOCK), 9),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.AMETHYST_SINGULARITY), 9)
    );

    public AmethystFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }
}

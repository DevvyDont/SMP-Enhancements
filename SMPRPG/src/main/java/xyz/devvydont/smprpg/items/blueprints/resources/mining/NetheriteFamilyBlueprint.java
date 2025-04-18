package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class NetheriteFamilyBlueprint extends CustomCompressableBlueprint {


    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.NETHERITE_INGOT)),
            new CompressionRecipeMember(new MaterialWrapper(Material.NETHERITE_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_NETHERITE)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_NETHERITE_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.NETHERITE_SINGULARITY))
    );

    public NetheriteFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }

}

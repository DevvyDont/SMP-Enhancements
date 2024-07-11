package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class NautilisShellFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.NAUTILUS_SHELL)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_NAUTILUS_SHELL)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_NAUTILUS_SHELL))
    );

    public static final CustomItemType[] CUSTOM_NAUTILUS_SHELL_MATERIALS = {
            CustomItemType.PREMIUM_NAUTILUS_SHELL,
            CustomItemType.ENCHANTED_NAUTILUS_SHELL
    };

    private final CustomItemType type;

    public NautilisShellFamilyBlueprint(ItemService itemService, CustomItemType type) {
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

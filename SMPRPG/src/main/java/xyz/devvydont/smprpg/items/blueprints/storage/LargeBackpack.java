package xyz.devvydont.smprpg.items.blueprints.storage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.BackpackBase;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class LargeBackpack extends BackpackBase implements IModelOverridden {

    public LargeBackpack(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.BLUE_BUNDLE;
    }

    @Override
    public int getSlots() {
        return 7 * 4 * 2;
    }

    @Override
    public int getStackSize() {
        return 32;
    }

    @Override
    public Component getInterfaceTitleComponent() {
        return ComponentUtils.create("Large Backpack", NamedTextColor.BLACK);
    }
}

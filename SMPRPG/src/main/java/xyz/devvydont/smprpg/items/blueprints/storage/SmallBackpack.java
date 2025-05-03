package xyz.devvydont.smprpg.items.blueprints.storage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.BackpackBase;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class SmallBackpack extends BackpackBase implements IModelOverridden {

    public SmallBackpack(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.GRAY_BUNDLE;
    }

    @Override
    public int getSlots() {
        return 7 * 2;
    }

    @Override
    public int getStackSize() {
        return 8;
    }

    @Override
    public Component getInterfaceTitleComponent() {
        return ComponentUtils.create("Small Backpack", NamedTextColor.BLACK);
    }
}

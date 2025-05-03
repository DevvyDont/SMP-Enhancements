package xyz.devvydont.smprpg.items.blueprints.storage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.BackpackBase;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class ColossalBackpack extends BackpackBase implements IModelOverridden {

    public ColossalBackpack(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getSlots() {
        return 252;  // This is close to the max amount of item stacks the component is allowed to store. (256)
    }

    @Override
    public int getStackSize() {
        return 99;
    }

    @Override
    public Component getInterfaceTitleComponent() {
        return ComponentUtils.create("Colossal Backpack", NamedTextColor.BLACK);
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.ORANGE_BUNDLE;
    }
}

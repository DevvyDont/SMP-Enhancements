package xyz.devvydont.smprpg.gui.sample;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class SampleSubMenu extends MenuBase {
    private final Material toDisplay;

    public SampleSubMenu(@NotNull Player player, MenuBase parentMenu, Material toDisplay) {
        super(player, 3, parentMenu);
        this.toDisplay = toDisplay;
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(ComponentUtils.create("Sample Sub Menu", NamedTextColor.BLACK));

        this.setBorderEdge();
        this.setSlot(13, toDisplay);
        this.setButton(22, BUTTON_BACK, (e) -> this.openParentMenu());
    }
}

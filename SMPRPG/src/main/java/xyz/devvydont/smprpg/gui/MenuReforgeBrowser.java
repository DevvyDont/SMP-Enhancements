package xyz.devvydont.smprpg.gui.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

/**
 * Renders all the reforges in the game for people to browse.
 */
public class MenuReforgeBrowser extends MenuBase {

    public static final int ROWS = 6;

    public MenuReforgeBrowser(@NotNull Player player) {
        super(player, ROWS);
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        super.handleInventoryOpened(event);
        this.render();
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        super.handleInventoryClicked(event);
        event.setCancelled(true);
    }

    public ItemStack generateReforgeButton(ReforgeType type) {
        ItemStack button = createNamedItem(Material.ANVIL, ComponentUtils.create(type.display()));
        return button;
    }

    public void handleButtonClicked(ReforgeType reforgeType) {

    }

    public void render() {
        setBorderEdge();

        int reforgeIndex = 0;
        // todo paginate if there are too many reforges
        for (int slot = 0; slot < getInventorySize(); slot++) {

            // Already occupied?
            if (getItem(slot) != null)
                continue;

            // No more reforges?
            if (reforgeIndex >= ReforgeType.values().length)
                break;

            // Render
            ReforgeType reforgeType = ReforgeType.values()[reforgeIndex];
            setButton(slot, generateReforgeButton(reforgeType), event -> handleButtonClicked(reforgeType));
            reforgeIndex++;
        }

        // Create a back button
        this.setBackButton((ROWS-1)*9 + 4);
    }
}

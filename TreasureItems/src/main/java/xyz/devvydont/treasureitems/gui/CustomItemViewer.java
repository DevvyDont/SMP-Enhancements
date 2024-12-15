package xyz.devvydont.treasureitems.gui;

import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomItemViewer extends RatesViewerGUI {

    @Override
    public Inventory construct(Player player) {
        Inventory inventory = super.construct(player);

        List<CustomItemBlueprint> blueprints = new ArrayList<>(xyz.devvydont.treasureitems.TreasureItems.getInstance().getCustomItemManager().getBlueprints());
        blueprints.sort(Comparator.comparing(CustomItemBlueprint::key));
        for (CustomItemBlueprint blueprint : blueprints) {
            inventory.addItem(blueprint.display());
        }

        return inventory;
    }

}

package me.devvy.customitems.gui;

import me.devvy.customitems.CustomItems;
import me.devvy.customitems.blueprints.CustomItemBlueprint;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomItemViewer extends RatesViewerGUI {

    @Override
    public Inventory construct(Player player) {
        Inventory inventory = super.construct(player);

        List<CustomItemBlueprint> blueprints = new ArrayList<>(CustomItems.getInstance().getCustomItemManager().getBlueprints());
        blueprints.sort(Comparator.comparing(CustomItemBlueprint::key));
        for (CustomItemBlueprint blueprint : blueprints) {
            inventory.addItem(blueprint.display());
        }

        return inventory;
    }

}

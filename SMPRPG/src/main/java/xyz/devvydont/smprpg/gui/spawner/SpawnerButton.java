package xyz.devvydont.smprpg.gui.spawner;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class SpawnerButton {

    public abstract ItemStack getItem(InterfaceSpawnerMainMenu gui);

    public abstract void handleClick(InterfaceSpawnerMainMenu gui, Player player, ClickType clickType);

}

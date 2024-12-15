package xyz.devvydont.treasureitems;

import xyz.devvydont.treasureitems.blueprints.CustomItemManager;
import xyz.devvydont.treasureitems.commands.GiveCustomItemsCommand;
import xyz.devvydont.treasureitems.commands.ViewCustomItemsCommand;
import xyz.devvydont.treasureitems.gui.GUIManager;
import xyz.devvydont.treasureitems.listeners.FishingItems;
import xyz.devvydont.treasureitems.listeners.MobKillListeners;
import xyz.devvydont.treasureitems.listeners.OreMineListeners;
import xyz.devvydont.treasureitems.listeners.RareDropListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TreasureItems extends JavaPlugin {

    private static TreasureItems INSTANCE;

    public static TreasureItems getInstance() {
        return INSTANCE;
    }

    private CustomItemManager customItemManager;

    private GUIManager guiManager;

    public CustomItemManager getCustomItemManager() {
        return customItemManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    @Override
    public void onEnable() {

        INSTANCE = this;

        customItemManager = new CustomItemManager();
        guiManager = new GUIManager();

        new RareDropListener();
        new FishingItems();
        new MobKillListeners();
        new OreMineListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}

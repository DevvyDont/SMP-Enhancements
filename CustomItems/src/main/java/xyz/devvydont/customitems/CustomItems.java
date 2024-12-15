package me.devvy.customitems;

import me.devvy.customitems.blueprints.CustomItemManager;
import me.devvy.customitems.commands.GiveCustomItemsCommand;
import me.devvy.customitems.commands.ViewCustomItemsCommand;
import me.devvy.customitems.gui.GUIManager;
import me.devvy.customitems.listeners.FishingItems;
import me.devvy.customitems.listeners.MobKillListeners;
import me.devvy.customitems.listeners.OreMineListeners;
import me.devvy.customitems.listeners.RareDropListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomItems extends JavaPlugin {

    private static CustomItems INSTANCE;

    public static CustomItems getInstance() {
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

        getCommand("treasure").setExecutor(new ViewCustomItemsCommand());
        GiveCustomItemsCommand giveCommand = new GiveCustomItemsCommand();
        getCommand("givetreasure").setExecutor(giveCommand);
        getCommand("givetreasure").setTabCompleter(giveCommand);

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

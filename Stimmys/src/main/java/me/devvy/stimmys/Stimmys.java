package me.devvy.stimmys;

import me.devvy.stimmys.commands.CommandStimmy;
import me.devvy.stimmys.gui.StimmyShopGUI;
import org.bukkit.plugin.java.JavaPlugin;

public final class Stimmys extends JavaPlugin {

    private static Stimmys INSTANCE;

    private StimmyCurrencyManager stimmyCurrencyManager;
    private StimmyShopGUI stimmyShopGUI;

    public StimmyCurrencyManager getStimmyCurrencyManager() {
        return stimmyCurrencyManager;
    }

    public StimmyShopGUI getStimmyShopGUI() {
        return stimmyShopGUI;
    }

    public static Stimmys getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        stimmyCurrencyManager = new StimmyCurrencyManager();
        stimmyShopGUI = new StimmyShopGUI();

        CommandStimmy commandStimmy = new CommandStimmy();
        getCommand("stimmy").setExecutor(commandStimmy);
        getCommand("stimmy").setTabCompleter(commandStimmy);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        stimmyCurrencyManager.cleanup();
        stimmyShopGUI.cleanup();
    }
}

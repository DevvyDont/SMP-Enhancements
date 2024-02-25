package me.devvy.whatamiholding;

import org.bukkit.plugin.java.JavaPlugin;

public final class WhatAmIHolding extends JavaPlugin {



    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("whatamiholding").setExecutor(new WAIHCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

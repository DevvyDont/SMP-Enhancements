package me.devvy.customitems.gui;

public class GUIManager {

    RatesMainMenuGUI ratesMainMenuGUI;
    MiningRatesGUI miningRatesGUI;
    FishingRatesGUI fishingRatesGUI;
    MobRatesGUI mobRatesGUI;
    CustomItemViewer customItemViewer;

    TradeupGUI tradeupGUI;

    public GUIManager() {
        ratesMainMenuGUI = new RatesMainMenuGUI();
        miningRatesGUI = new MiningRatesGUI();
        fishingRatesGUI = new FishingRatesGUI();
        mobRatesGUI = new MobRatesGUI();
        customItemViewer = new CustomItemViewer();
        tradeupGUI = new TradeupGUI();

    }

    public RatesMainMenuGUI getRatesMainMenuGUI() {
        return ratesMainMenuGUI;
    }

    public MiningRatesGUI getMiningRatesGUI() {
        return miningRatesGUI;
    }

    public FishingRatesGUI getFishingRatesGUI() {
        return fishingRatesGUI;
    }

    public MobRatesGUI getMobRatesGUI() {
        return mobRatesGUI;
    }

    public CustomItemViewer getCustomItemViewer() {
        return customItemViewer;
    }

    public TradeupGUI getTradeupGUI() {
        return tradeupGUI;
    }

    public boolean isCustomGUI(String title) {
        return title.toLowerCase().contains("treasure items");
    }
}

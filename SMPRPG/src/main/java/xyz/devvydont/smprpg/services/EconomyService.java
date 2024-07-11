package xyz.devvydont.smprpg.services;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.text.DecimalFormat;

/**
 * Acts as a middleman between performing transactions for player currency
 */
public class EconomyService implements BaseService {

    private SMPRPG plugin;

    private Economy economy;

    public EconomyService(SMPRPG plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean setup() {
        plugin.getLogger().info("Setting up Economy service");

        // If vault isn't installed, we cannot function correctly.
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("Vault is not installed. Please install Vault");
            return false;
        }

        // We need to make sure the economy class is valid
        RegisteredServiceProvider<Economy> provider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider == null) {
            plugin.getLogger().severe("Failed to detect Economy service, is Vault installed correctly?");
            return false;
        }


        this.economy = provider.getProvider();
        plugin.getLogger().info("Successfully hooked into Vault Economy service");
        return true;
    }

    @Override
    public void cleanup() {
        plugin.getLogger().info("Cleaning up EconomyService");
    }

    @Override
    public boolean required() {
        return true;
    }

    /**
     * Give a player money. This is for generating money into the economy
     * Will always round to the nearest whole number
     *
     * @param player The player to give money to, doesn't need to be online at the moment
     * @param amount The amount of coins to give a player
     * @return boolean, true if successful
     */
    public boolean addMoney(OfflinePlayer player, double amount) {
        EconomyResponse response = economy.depositPlayer(player, (int)Math.round(amount));
        plugin.getLogger().finest(String.format("Server has paid %s %.0f coins, balance is now %.0f", player.getName(), response.amount, response.balance));
        return response.transactionSuccess();
    }

    /**
     * Take a player's money as a transaction to the server. This is used for various methods of fees and money
     * transfer across the server. Will always round to the nearest whole number
     *
     * @param player The player to take money from, doesn't need to be online at the moment
     * @param amount The amount of coins to take from a player
     * @return boolean, true if successful
     */
    public boolean takeMoney(OfflinePlayer player, double amount) {
        EconomyResponse response = economy.withdrawPlayer(player, (int)Math.round(amount));
        plugin.getLogger().info(String.format("Server has attempted to take %s's %.0f coins, balance is now %.0f (%s-%s)", player.getName(), response.amount, response.balance, response.type, response.errorMessage));
        return response.transactionSuccess();
    }

    /**
     * See a player's balance, this is represented in "coins" and is always an integer.
     *
     * @param player Player to query balance of
     * @return an int represented rounded balance of a player
     */
    public int getMoney(OfflinePlayer player) {
        return (int) Math.round(economy.getBalance(player));
    }

    /**
     * Formats a string for display across the plugin, this method specifically some number
     *
     * @param amount
     * @return
     */
    public static String formatMoney(int amount) {
        String number =  new DecimalFormat("#,###,###,###,###").format(amount);
        return String.format("%s%s", number, Symbols.COIN);
    }

    /**
     * Formats a string for display across the plugin, this method specifically some number
     *
     * @param amount
     * @return
     */
    public static String formatMoney(long amount) {
        String number =  new DecimalFormat("###,###,###,###,###,###,###").format(amount);
        return String.format("%s%s", number, Symbols.COIN);
    }

    /**
     * Formats a string for display across the plugin, this method specifically displays a user's balance
     *
     * @param player
     * @return
     */
    public String formatMoney(OfflinePlayer player) {
        return formatMoney(getMoney(player));
    }


}

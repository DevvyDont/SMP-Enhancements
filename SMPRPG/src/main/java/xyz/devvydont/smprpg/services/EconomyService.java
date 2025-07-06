package xyz.devvydont.smprpg.services;

import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.text.DecimalFormat;

/**
 * Acts as a middleman between performing transactions for player currency
 */
public class EconomyService implements IService {

    private Economy economy;

    @Override
    public void setup() throws RuntimeException {
        // If vault isn't installed, we cannot function correctly.
        var plugin = SMPRPG.getInstance();
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("Vault is not installed. Please install Vault");
            throw new RuntimeException("Vault is not installed. Please install Vault");
        }

        // We need to make sure the economy class is valid
        RegisteredServiceProvider<Economy> provider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider == null) {
            plugin.getLogger().severe("Failed to detect Economy service, is Vault installed correctly?");
            throw new RuntimeException("Failed to detect Economy service. Vault is not installed correctly.");
        }

        this.economy = provider.getProvider();
        plugin.getLogger().info("Successfully hooked into Vault Economy service");
    }

    @Override
    public void cleanup() {
        SMPRPG.getInstance().getLogger().info("Cleaning up EconomyService");
    }

    /**
     * Formats a string for display across the plugin, this method specifically some number
     * @param amount The amount you want to format.
     * @return A clean user readable formatted representation of the money amount.
     */
    public static String formatMoney(int amount) {
        String number =  new DecimalFormat("#,###,###,###,###").format(amount);
        return String.format("%s%s", number, Symbols.COIN);
    }

    /**
     * Formats a string for display across the plugin, this method specifically some number
     * @param amount The amount you want to format.
     * @return A clean user readable formatted representation of the money amount.
     */
    public static String formatMoney(long amount) {
        String number =  new DecimalFormat("###,###,###,###,###,###,###").format(amount);
        return String.format("%s%s", number, Symbols.COIN);
    }

    /**
     * Give a player money. This is for generating money into the economy
     * Will always round to the nearest whole number
     * @param player The player to give money to, doesn't need to be online at the moment
     * @param amount The amount of coins to give a player
     * @return boolean, true if successful
     */
    public boolean addMoney(OfflinePlayer player, double amount) {
        EconomyResponse response = economy.depositPlayer(player, (int)Math.round(amount));
        SMPRPG.getInstance().getLogger().finest(String.format("Server has paid %s %.0f coins, balance is now %.0f", player.getName(), response.amount, response.balance));
        return response.transactionSuccess();
    }

    /**
     * Take a player's money as a transaction to the server. This is used for various methods of fees and money
     * transfer across the server. Will always round to the nearest whole number
     * @param player The player to take money from, doesn't need to be online at the moment
     * @param amount The amount of coins to take from a player
     * @return boolean, true if successful
     */
    public boolean takeMoney(OfflinePlayer player, double amount) {
        EconomyResponse response = economy.withdrawPlayer(player, (int)Math.round(amount));
        SMPRPG.getInstance().getLogger().info(String.format("Server has attempted to take %s's %.0f coins, balance is now %.0f (%s-%s)", player.getName(), response.amount, response.balance, response.type, response.errorMessage));
        return response.transactionSuccess();
    }

    /**
     * A variation of EconomyService#takeMoney()
     * This version also calls takeMoney(), but also alerts the user in chat that their balance was updated.
     * As a consequence of this, an online player is required so that we can send them a message.
     * <p>
     * This method also assumes that the player already has enough money, and this method may behave unexpectedly if
     * the process of checking for sufficient funds is skipped.
     * @param player The player to take money from.
     * @param cost The amount of money to take from the player.
     */
    public void spendMoney(Player player, int cost) {
        this.takeMoney(player, cost);
        player.sendMessage(ComponentUtils.merge(
                ComponentUtils.create(formatMoney(cost), NamedTextColor.GOLD),
                ComponentUtils.create(" has been taken from your account. Your balance is now "),
                ComponentUtils.create(formatMoney(getMoney(player)), NamedTextColor.GOLD)
        ));
    }

    /**
     * See a player's balance, this is represented in "coins" and is always an integer.
     * @param player Player to query balance of
     * @return an int represented rounded balance of a player
     */
    public int getMoney(OfflinePlayer player) {
        return (int) Math.round(economy.getBalance(player));
    }

    /**
     * Formats a string for display across the plugin, this method specifically some number
     * @param player The player you want to format money for.
     * @return A clean user readable formatted representation of the money amount.
     */
    public String formatMoney(OfflinePlayer player) {
        return formatMoney(getMoney(player));
    }


}

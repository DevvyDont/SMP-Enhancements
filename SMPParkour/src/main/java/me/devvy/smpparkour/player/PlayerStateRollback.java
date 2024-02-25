package me.devvy.smpparkour.player;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

public class PlayerStateRollback {

    private final double health;
    private final double healthScale;
    private final int hunger;
    private final float saturation;
    private final Location location;
    private final int xpLevel;
    private final float xpProgress;
    private final GameMode gameMode;
    private final Scoreboard scoreboard;

    // Inventory
    private final ItemStack[] contents;
    private final ItemStack[] extraContents;
    private final ItemStack[] armorContents;

    // Potions
    private final Collection<PotionEffect> potionEffects;

    public PlayerStateRollback(Player player) {

        this.health = player.getHealth();
        this.healthScale = player.getHealthScale();
        this.hunger = player.getFoodLevel();
        this.saturation = player.getSaturation();
        this.location = player.getLocation().clone();
        this.xpLevel = player.getLevel();
        this.xpProgress = player.getExp();
        this.gameMode = player.getGameMode();
        this.scoreboard = player.getScoreboard();

        this.contents = player.getInventory().getContents();
        this.extraContents = player.getInventory().getExtraContents();
        this.armorContents = player.getInventory().getArmorContents();

        this.potionEffects = player.getActivePotionEffects();

    }

    public void restore(Player player) {

        player.setHealth(health);
        player.setHealthScale(healthScale);
        player.setFoodLevel(hunger);
        player.setSaturation(saturation);
        player.setLevel(xpLevel);
        player.setExp(xpProgress);
        player.teleport(location);
        player.setGameMode(gameMode);
        player.setScoreboard(scoreboard);

        player.getInventory().setContents(contents);
        player.getInventory().setExtraContents(extraContents);
        player.getInventory().setArmorContents(armorContents);

        for (PotionEffect pot : potionEffects)
            player.addPotionEffect(pot);
    }
}

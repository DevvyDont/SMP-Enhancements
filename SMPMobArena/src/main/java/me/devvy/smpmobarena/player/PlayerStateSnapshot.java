package me.devvy.smpmobarena.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;

/**
 * Stores things like hunger, health, inventory, etc. for a player
 * so we can clear their inventory before entering the arena and restore it when they leave
 */
public class PlayerStateSnapshot {

    private final double health;
    private final double healthScale;
    private final int hunger;
    private final float saturation;
    private final int level;
    private final float exp;

    private final ItemStack[] inventoryContents;
    private final ItemStack[] armorContents;
    private final ItemStack[] extraContents;

    private final GameMode gameMode;
    private final Scoreboard  scoreboard;

    private Collection<PotionEffect> potionEffects;

    public PlayerStateSnapshot(Player player) {

            this.health = player.getHealth();
            this.healthScale = player.getHealthScale();
            this.hunger = player.getFoodLevel();
            this.saturation = player.getSaturation();
            this.level = player.getLevel();
            this.exp = player.getExp();

            this.inventoryContents = player.getInventory().getContents();
            this.armorContents = player.getInventory().getArmorContents();
            this.extraContents = player.getInventory().getExtraContents();

            this.gameMode = player.getGameMode();
            this.scoreboard = player.getScoreboard();

            this.potionEffects = player.getActivePotionEffects();

    }

    public void restore(Player player) {

            player.setHealth(health);
            player.setHealthScale(healthScale);
            player.setFoodLevel(hunger);
            player.setSaturation(saturation);
            player.setLevel(level);
            player.setExp(exp);

            player.getInventory().setContents(inventoryContents);
            player.getInventory().setArmorContents(armorContents);
            player.getInventory().setExtraContents(extraContents);

            player.setGameMode(gameMode);
            player.setScoreboard(scoreboard);

            for (PotionEffect potionEffect : potionEffects)
                player.addPotionEffect(potionEffect);
    }
}

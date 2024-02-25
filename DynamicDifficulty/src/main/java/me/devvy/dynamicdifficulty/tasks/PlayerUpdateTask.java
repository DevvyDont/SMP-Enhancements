package me.devvy.dynamicdifficulty.tasks;

import me.devvy.dynamicdifficulty.DynamicDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerUpdateTask extends BukkitRunnable {


    @Override
    public void run() {

        if (DynamicDifficulty.getInstance().getCurrentDifficulty().equals(Difficulty.HARD)) {

            for ( Player p : Bukkit.getOnlinePlayers())
                p.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 60, 0, true, true, true));

        }

    }
}

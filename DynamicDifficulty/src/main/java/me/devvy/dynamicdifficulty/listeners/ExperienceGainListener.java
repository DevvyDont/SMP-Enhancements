package me.devvy.dynamicdifficulty.listeners;

import me.devvy.dynamicdifficulty.DynamicDifficulty;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class ExperienceGainListener implements Listener {

    public static float getExperienceMultiplier(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 1f;
            case NORMAL:
                return 1.25f;
            case HARD:
                return 1.5f;
            default:
                return 1;
        }
    }

    public ExperienceGainListener() {
        DynamicDifficulty.getInstance().getServer().getPluginManager().registerEvents(this, DynamicDifficulty.getInstance());
    }

    @EventHandler
    public void onExperienceGainFromMob(EntityDeathEvent event) {

        // If the entity is a player don't do anything
        if (event.getEntity() instanceof Player)
            return;

        int old = event.getDroppedExp();
        int newExp = (int) (old * getExperienceMultiplier(DynamicDifficulty.getInstance().getCurrentDifficulty()));

        event.setDroppedExp(newExp);

    }

    @EventHandler
    public void onExperienceGainFromBlock(BlockBreakEvent event) {

        if (event.getExpToDrop() <= 0)
            return;

        int old = event.getExpToDrop();
        int newExp = (int) (old * getExperienceMultiplier(DynamicDifficulty.getInstance().getCurrentDifficulty()));

        event.setExpToDrop(newExp);
    }

    @EventHandler
    public void onExperienceGainFromFurnace(FurnaceExtractEvent event) {

        if (event.getExpToDrop() <= 0)
            return;

        int old = event.getExpToDrop();
        int newExp = (int) (old * getExperienceMultiplier(DynamicDifficulty.getInstance().getCurrentDifficulty()));

        event.setExpToDrop(newExp);
    }

    @EventHandler
    public void onExperienceGainFromFish(PlayerFishEvent event) {

        if (event.getExpToDrop() <= 0)
            return;

        int old = event.getExpToDrop();
        int newExp = (int) (old * getExperienceMultiplier(DynamicDifficulty.getInstance().getCurrentDifficulty()));

        event.setExpToDrop(newExp);

    }


}

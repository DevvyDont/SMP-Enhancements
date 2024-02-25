package me.devvy.dynamicdifficulty.storage;

import me.devvy.dynamicdifficulty.DynamicDifficulty;
import org.bukkit.Difficulty;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class DifficultyPreferenceStorageManager {

    private static final NamespacedKey DIFFICULTY_STORAGE_KEY = new NamespacedKey(DynamicDifficulty.getInstance(), "difficulty-preference");

    public Difficulty getPreferredDifficulty(Player player) {
        return Difficulty.values()[player.getPersistentDataContainer().getOrDefault(DIFFICULTY_STORAGE_KEY, PersistentDataType.INTEGER, Difficulty.EASY.ordinal())];
    }

    public void setPreferredDifficulty(Player player, Difficulty difficulty) {
        player.getPersistentDataContainer().set(DIFFICULTY_STORAGE_KEY, PersistentDataType.INTEGER, difficulty.ordinal());
    }

    public DifficultyVoteReport getDifficultyVoteReport() {
        return new DifficultyVoteReport();
    }

}

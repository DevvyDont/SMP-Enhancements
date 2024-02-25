package me.devvy.dynamicdifficulty.storage;

import me.devvy.dynamicdifficulty.DynamicDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;

import java.util.*;

public class DifficultyVoteReport {

    private HashMap<Player, Difficulty> picks;
    private HashMap<Difficulty, Integer> votes;

    public DifficultyVoteReport() {
        picks = new HashMap<>();
        votes = new HashMap<>();
        votes.put(Difficulty.EASY, 0);
        votes.put(Difficulty.NORMAL, 0);
        votes.put(Difficulty.HARD, 0);

        for (Player p : Bukkit.getOnlinePlayers()) {
            Difficulty preferredDifficulty = DynamicDifficulty.getInstance().getDifficultyPreferenceStorageManager().getPreferredDifficulty(p);
            votes.put(preferredDifficulty, votes.get(preferredDifficulty) + 1);
            picks.put(p, preferredDifficulty);
        }
    }

    public HashMap<Player, Difficulty> getPicks() {
        return picks;
    }

    public int getVotesFor(Difficulty difficulty) {
        return votes.getOrDefault(difficulty, 0);
    }

    public Difficulty calculateVotedDifficulty() {
        int[] difficultyVotes = new int[Difficulty.values().length];

        // Count up all the votes for each difficulty
        for (Map.Entry<Difficulty, Integer> entry : votes.entrySet())
            difficultyVotes[entry.getKey().ordinal()] = entry.getValue();

        // Find the difficulty with the most votes and solve tiebreakers by prioritizing the lowest index
        Difficulty mostVotedDifficulty = Difficulty.EASY;
        int mostVotes = 0;

        for (int i = 0; i < difficultyVotes.length; i++) {
            if (difficultyVotes[i] > mostVotes) {
                mostVotedDifficulty = Difficulty.values()[i];
                mostVotes = difficultyVotes[i];
            }
        }


        return mostVotedDifficulty;
    }

    public Collection<String> getVotersFor(Difficulty difficulty) {
        List<String> voters = new ArrayList<>();

        for (Map.Entry<Player, Difficulty> entry : picks.entrySet()) {
            if (entry.getValue().equals(difficulty))
                voters.add(entry.getKey().getName());
        }

        return voters;
    }
}

package xyz.devvydont.smprpg.util.rng;

import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class to randomly select an element according to weights.
 * @param <T> Any instance that is being selected from. Represents the items in the collection.
 */
public class WeightedSelector<T> {

    /**
     * Utility record that also contains the probability along with the element that was selected.
     * Can be used to probability calculations.
     * @param Element The selected element.
     * @param Probability The probability that it was selected.
     * @param <T>
     */
    public record Result<T>(T Element, double Probability){}

    /**
     * Utility record that keeps item and weight management clean when using the {@link WeightedSelector}.
     * @param Item The item that would return if selected.
     * @param Weight The weight of this item.
     * @param <T> The type of Item. Can be any object.
     */
    public record Entry<T> (T Item, double Weight){ }

    private final List<Entry<T>> entries = new ArrayList<>();
    private double totalWeight;

    public void add(T item, double weight) {
        if (weight <= 0)
            return;
        totalWeight += weight;
        entries.add(new Entry<>(item, weight));
    }

    /**
     * Retrieve a random element taking weights into consideration.
     * Throws {@link IllegalStateException} if called with no elements.
     * @return A random weighted selection.
     */
    public Result<T> roll() {
        return roll(new Random());
    }

    /**
     * Retrieve a random element taking weights into consideration.
     * Throws {@link IllegalStateException} if called with no elements.
     * @param random A {@link Random} instance if you want seeded RNG.
     * @return A random weighted selection.
     */
    public Result<T> roll(Random random) {
        if (entries.isEmpty())
            throw new IllegalStateException("No entries to roll from");
        double r = random.nextDouble() * totalWeight;
        double cumulative = 0;
        for (Entry<T> entry : entries) {
            cumulative += entry.Weight;
            if (r < cumulative)
                return new Result<>(entry.Item, entry.Weight / totalWeight);
        }
        SMPRPG.broadcastToOperators(ComponentUtils.create("WeightedSelector roll failed. Double check logic."));
        return new Result<>(entries.get(entries.size() - 1).Item, 1.0); // Fallback, should never happen.
    }

    /**
     * Checks if the underlying elements added is empty. If this is true, the {@link WeightedSelector#roll()} will
     * throw an exception.
     * @return True if there are no elements in the underlying elements.
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * Checks the size of the added elements.
     * @return The size of elements added.
     */
    public int size() {
        return entries.size();
    }



}

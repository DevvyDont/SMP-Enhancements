package xyz.devvydont.smprpg.util.animations;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.services.IService;
import xyz.devvydont.smprpg.util.animations.iterators.AnimationFrame;
import xyz.devvydont.smprpg.util.animations.iterators.AnimationIterator;
import xyz.devvydont.smprpg.util.animations.iterators.InfiniteIterator;
import xyz.devvydont.smprpg.util.animations.iterators.NumberedIterator;
import xyz.devvydont.smprpg.util.animations.playback.AnimationHandle;
import xyz.devvydont.smprpg.util.animations.playback.AnimationPlayer;
import xyz.devvydont.smprpg.util.animations.playback.AnimationPlaybackState;
import xyz.devvydont.smprpg.util.animations.utils.AnimationBuilder;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.ArrayList;
import java.util.List;

public final class AnimationService implements Runnable, IService {
    private int taskId;
    private final List<AnimationPlayer> activeAnimations = new ArrayList<>();

    @Override
    public void setup() throws RuntimeException {
        var task = Bukkit.getScheduler().runTaskTimer(SMPRPG.getInstance(), this, TickTime.INSTANTANEOUSLY, TickTime.TICK);
        taskId = task.getTaskId();
    }

    @Override
    public void cleanup() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    /**
     * Plays the provided animation.
     *
     * @param builder The animation builder
     * @return The handle to control the animation.
     */
    public AnimationHandle play(@NotNull AnimationBuilder builder) {
        return this.addAnimation(builder.build());
    }

    /**
     * Plays the provided animation.
     *
     * @param iterator The animation frame iterator
     * @return The handle to control the animation.
     */
    public AnimationHandle play(@NotNull AnimationIterator iterator) {
        return this.addAnimation(iterator);
    }

    /**
     * Plays the provided animation once.
     *
     * @param frames The frames of the animation.
     * @return The handle to control the animation.
     */
    public AnimationHandle playOnce(@NotNull AnimationFrame @NotNull ... frames) {
        return this.addAnimation(new NumberedIterator(List.of(frames), 1));
    }

    /**
     * Plays the provided animation once.
     *
     * @param frames The frames of the animation.
     * @return The handle to control the animation.
     */
    public AnimationHandle playOnce(@NotNull List<@NotNull AnimationFrame> frames) {
        return this.addAnimation(new NumberedIterator(frames, 1));
    }

    /**
     * Plays the provided animation a set amount of times.
     *
     * @param iterations The amount of times the animation should play.
     * @param frames     The frames of the animation.
     * @return The handle to control the animation.
     */
    public AnimationHandle loop(int iterations, @NotNull AnimationFrame @NotNull ... frames) {
        return this.addAnimation(new NumberedIterator(List.of(frames), iterations));
    }

    /**
     * Plays the provided animation a set amount of times.
     *
     * @param iterations The amount of times the animation should play.
     * @param frames     The frames of the animation.
     * @return The handle to control the animation.
     */
    public AnimationHandle loop(int iterations, @NotNull List<@NotNull AnimationFrame> frames) {
        return this.addAnimation(new NumberedIterator(frames, iterations));
    }

    /**
     * Plays the provided animation endlessly.
     *
     * @param frames The frames of the animation.
     * @return The handle to control the animation.
     */
    public AnimationHandle loopEndlessly(@NotNull AnimationFrame @NotNull ... frames) {
        return this.addAnimation(new InfiniteIterator(List.of(frames)));
    }

    /**
     * Plays the provided animation endlessly.
     *
     * @param frames The frames of the animation.
     * @return The handle to control the animation.
     */
    public AnimationHandle loopEndlessly(@NotNull List<@NotNull AnimationFrame> frames) {
        return this.addAnimation(new InfiniteIterator(frames));
    }

    /**
     * Adds an animation into the animation system.
     */
    private AnimationHandle addAnimation(AnimationIterator iterator) {
        var animation = new AnimationPlayer(iterator);
        this.activeAnimations.add(animation);
        return animation;
    }

    @Override
    public void run() {
        for (var animation : this.activeAnimations.stream().toList()) {
            var hasFinished = animation.getPlaybackStatus() == AnimationPlaybackState.FINISHED;
            if (hasFinished) {
                this.activeAnimations.remove(animation);
                continue;
            }

            animation.handleTick();
        }
    }
}

package xyz.devvydont.smprpg.util.animations;

import org.bukkit.Bukkit;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.animations.containers.AnimationContainer;
import xyz.devvydont.smprpg.util.animations.containers.AnimationFrame;
import xyz.devvydont.smprpg.util.animations.containers.AnimationHandle;
import xyz.devvydont.smprpg.util.animations.containers.AnimationState;
import xyz.devvydont.smprpg.util.animations.utils.AnimationBuilder;

import java.util.ArrayList;
import java.util.List;

public final class AnimationService implements Runnable {
    private final List<AnimationContainer> activeAnimations = new ArrayList<>();

    public AnimationService(SMPRPG plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 1);
    }

    /**
     * Adds an animation into the animation system and runs it.
     *
     * @param frames The animation frames to run.
     * @return The handle to control the animation.
     */
    public AnimationHandle playAnimation(AnimationFrame... frames) {
        var animation = new AnimationContainer(List.of(frames));
        activeAnimations.add(animation);
        return animation;
    }

    /**
     * Adds an animation into the animation system and runs it.
     *
     * @param frames The animation frames to run.
     * @return The handle to control the animation.
     */
    public AnimationHandle playAnimation(List<AnimationFrame> frames) {
        var animation = new AnimationContainer(frames);
        activeAnimations.add(animation);
        return animation;
    }

    /**
     * Adds an animation into the animation system and runs it.
     *
     * @param builder The animation to run.
     * @return The handle to control the animation.
     */
    public AnimationHandle playAnimation(AnimationBuilder builder) {
        var animation = new AnimationContainer(builder.build());
        activeAnimations.add(animation);
        return animation;
    }

    @Override
    public void run() {
        for (var animation : activeAnimations.stream().toList()) {
            var hasFinished = animation.getPlaybackStatus() == AnimationState.FINISHED;
            if (hasFinished) {
                activeAnimations.remove(animation);
                continue;
            }

            animation.handleTick();
        }
    }
}

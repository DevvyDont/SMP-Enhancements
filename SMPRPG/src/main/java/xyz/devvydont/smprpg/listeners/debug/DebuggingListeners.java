package xyz.devvydont.smprpg.listeners.debug;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import xyz.devvydont.smprpg.events.skills.SkillExperiencePostGainEvent;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

import java.text.DecimalFormat;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.create;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.merge;

/**
 * Listeners intended to be used for debugging. Very useful to make sure certain game functions
 * are working correctly.
 */
public class DebuggingListeners extends ToggleableListener {

    /**
     * A useful chat function that tells you how much experience you earned, the source, how many
     * more of that exact instance you need to level up, and how many you need to max. This is meant
     * to check balancing of skill experience.
     * @param event The {@link SkillExperiencePostGainEvent} event that provides relevant context.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    private void __onSkillExperienceEarn(SkillExperiencePostGainEvent event) {

        /*
        Extract all the relevant numbers we need.
        We want the following 3 things:
        - EXP to level up.
        - EXP to level up 10 times. (Only if 10 level ups are lower than skill cap!)
        - EXP to max.
         */
        var expToLevelUp = event.getSkill().getExperienceForNextLevel();
        var intermediateLevelTarget = Math.min(SkillGlobals.getMaxSkillLevel(), event.getSkill().getLevel()+10);
        var intermediateLevelTarget2 = Math.min(SkillGlobals.getMaxSkillLevel(), event.getSkill().getLevel()+50);
        var expToIntermediateLevelTarget = SkillGlobals.getCumulativeExperienceForLevel(intermediateLevelTarget) - event.getSkill().getExperience();
        var expToIntermediateLevelTarget2 = SkillGlobals.getCumulativeExperienceForLevel(intermediateLevelTarget2) - event.getSkill().getExperience();
        var expToMax = SkillGlobals.getTotalExperienceCap() - event.getSkill().getExperience();

        // Now calculate how many times we need to earn this experience AGAIN to hit the thresholds.
        var oneLevelRepetitions = expToLevelUp / event.getExperienceEarned();
        var intermediateRepetitions = expToIntermediateLevelTarget / event.getExperienceEarned();
        var intermediateRepetitions2 = expToIntermediateLevelTarget2 / event.getExperienceEarned();
        var maxRepetitions = expToMax / event.getExperienceEarned();

        var df = new DecimalFormat("#,###,###");

        // Show them the data.
        event.getPlayer().sendMessage(merge(
                create("------------------------------\n", NamedTextColor.GRAY),
                create("Gained "), create(df.format(event.getExperienceEarned()) + "EXP", GREEN), create(" for "), create(event.getSkillType().getDisplayName(), GOLD), create(" from "), create(event.getSource().name(), AQUA), create("!\n"),
                create("To level  1x: "), create(df.format(expToLevelUp) + " ", YELLOW), create("~" + df.format(oneLevelRepetitions) + " reps\n", GRAY),
                create("To level 10x: "), create(df.format(expToIntermediateLevelTarget) + " ", YELLOW), create("~" + df.format(intermediateRepetitions) + " reps\n", GRAY),
                create("To level 50x: "), create(df.format(expToIntermediateLevelTarget2) + " ", YELLOW), create("~" + df.format(intermediateRepetitions2) + " reps\n", GRAY),
                create("To level MAX: "), create(df.format(expToMax) + " ", YELLOW), create("~" + df.format(maxRepetitions) + " reps\n", GRAY),
                create("------------------------------", NamedTextColor.GRAY)
        ));
    }

}

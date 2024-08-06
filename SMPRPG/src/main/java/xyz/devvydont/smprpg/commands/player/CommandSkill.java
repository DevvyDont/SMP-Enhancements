package xyz.devvydont.smprpg.commands.player;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

public class CommandSkill extends CommandBase {

    public CommandSkill(String name) {
        super(name);
    }

    private void setSkillExp(LeveledPlayer player, SkillType skill, int level) {
        SkillInstance inst = SMPRPG.getInstance().getSkillService().getNewSkillInstance(player.getPlayer(), skill);
        int targetExp = SkillGlobals.getCumulativeExperienceForLevel(level);
        if (targetExp > inst.getExperience())
            inst.addExperience(targetExp - inst.getExperience(), SkillExperienceGainEvent.ExperienceSource.UNKNOWN);
        else
            inst.setExperience(targetExp);
        player.getPlayer().sendMessage(ChatUtil.getSuccessMessage("Set your " + skill.getDisplayName() + " skill to level " + inst.getLevel()));
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        if (!(commandSourceStack.getSender() instanceof Player p))
            return;

        LeveledPlayer player = SMPRPG.getInstance().getEntityService().getPlayerInstance(p);

        if (strings.length >= 1) {

            if (strings[0].toLowerCase().equalsIgnoreCase("reset")) {
                for (SkillInstance skill : player.getSkills())
                    skill.setExperience(0);
                player.getPlayer().sendMessage(ChatUtil.getSuccessMessage("Reset all of your skills!"));
                SMPRPG.getInstance().getSkillService().syncSkillAttributes(player);
            }
        }

        if (strings.length >= 2) {

            try {
                String skill = strings[0].toUpperCase();
                int level = Integer.parseInt(strings[1]);

                if (skill.equalsIgnoreCase("ALL"))
                    for (SkillType type : SkillType.values())
                        setSkillExp(player, type, level);
                else {
                    SkillType type = SkillType.valueOf(skill);
                    setSkillExp(player, type, level);
                }

                SMPRPG.getInstance().getSkillService().syncSkillAttributes(player);
            } catch (NumberFormatException ignored) {
                p.sendMessage(ChatUtil.getErrorMessage("Invalid level provided. Please provide an actual number"));
                return;
            } catch (IllegalArgumentException ignored) {
                p.sendMessage(ChatUtil.getErrorMessage("Invalid argument provided. Please provide skill type then a level to set it to"));
                return;
            }

        }

        p.sendMessage(Component.empty());
        for (SkillInstance skill : player.getSkills())
            p.sendMessage(skill.getType().getDisplayName() + " " + skill.getLevel() + " " + MinecraftStringUtils.formatNumber(skill.getExperience()) + "XP " + "(" + MinecraftStringUtils.formatNumber(skill.getExperienceProgress()) + "/" + MinecraftStringUtils.formatNumber(skill.getNextExperienceThreshold()) + ")");
    }
}

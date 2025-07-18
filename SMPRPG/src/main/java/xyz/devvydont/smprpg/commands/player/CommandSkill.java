package xyz.devvydont.smprpg.commands.player;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.TriState;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.services.SkillService;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandSkill extends CommandBase {

    public CommandSkill(String name) {
        super(name);
    }

    private void setSkillExp(LeveledPlayer player, SkillType skill, int level) {
        SkillInstance inst = SMPRPG.getService(SkillService.class).getNewSkillInstance(player.getPlayer(), skill);
        int targetExp = SkillGlobals.getCumulativeExperienceForLevel(level);
        if (targetExp > inst.getExperience())
            inst.addExperience(targetExp - inst.getExperience(), SkillExperienceGainEvent.ExperienceSource.COMMANDS);
        else
            inst.setExperience(targetExp);
        player.getEntity().sendMessage(ComponentUtils.success("Set your " + skill.getDisplayName() + " skill to level " + inst.getLevel()));
    }

    private List<Component> getSkillDisplay(LeveledPlayer player) {
        var output = new ArrayList<Component>();
        output.add(ComponentUtils.EMPTY);
        for (var skill : player.getSkills()) {
            output.add(ComponentUtils.merge(
                    ComponentUtils.create(skill.getType().getDisplayName() + " " + skill.getLevel(), NamedTextColor.AQUA),
                    ComponentUtils.create(" - "),
                    ComponentUtils.create(MinecraftStringUtils.formatNumber(skill.getExperienceProgress()), NamedTextColor.GREEN),
                    ComponentUtils.create("/"),
                    ComponentUtils.create(MinecraftStringUtils.formatNumber(skill.getNextExperienceThreshold()), NamedTextColor.GOLD),
                    ComponentUtils.create(" ("),
                    ComponentUtils.create(MinecraftStringUtils.formatNumber(skill.getExperience()) + "XP", NamedTextColor.DARK_GRAY),
                    ComponentUtils.create(")")
            ));
        }
        output.add(ComponentUtils.EMPTY);
        output.add(ComponentUtils.create("Skill Average: "));
        output.add(ComponentUtils.create(String.format("%.2f", player.getAverageSkillLevel()), NamedTextColor.GOLD));
        return output;
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        if (!(commandSourceStack.getSender() instanceof Player p))
            return;

        boolean isAdmin = commandSourceStack.getSender().permissionValue("smprpg.command.skill.admin").equals(TriState.TRUE) || commandSourceStack.getSender().isOp();
        var player = SMPRPG.getService(EntityService.class).getPlayerInstance(p);

        if (!isAdmin) {
            for (Component component : getSkillDisplay(player))
                player.getPlayer().sendMessage(component);
            return;
        }

        if (strings.length >= 1) {

            if (strings[0].toLowerCase().equalsIgnoreCase("reset")) {
                for (SkillInstance skill : player.getSkills())
                    skill.setExperience(0);
                player.getPlayer().sendMessage(ComponentUtils.success("Reset all of your skills!"));
                SMPRPG.getService(SkillService.class).syncSkillAttributes(player);
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

                SMPRPG.getService(SkillService.class).syncSkillAttributes(player);
            } catch (NumberFormatException ignored) {
                p.sendMessage(ComponentUtils.error("Invalid level provided. Please provide an actual number"));
                return;
            } catch (IllegalArgumentException ignored) {
                p.sendMessage(ComponentUtils.error("Invalid argument provided. Please provide skill type then a level to set it to"));
                return;
            }

        }

        for (Component component : getSkillDisplay(player))
            player.getPlayer().sendMessage(component);
    }
}

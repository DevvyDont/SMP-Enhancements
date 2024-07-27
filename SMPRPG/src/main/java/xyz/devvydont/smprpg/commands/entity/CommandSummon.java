package xyz.devvydont.smprpg.commands.entity;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandSummon extends CommandBase {

    public CommandSummon(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        Entity executor = commandSourceStack.getExecutor();
        if (executor == null) {
            commandSourceStack.getSender().sendMessage(ChatUtil.getErrorMessage("You cannot execute this as the console!"));
            return;
        }

        if (args.length == 0) {
            executor.sendMessage(ChatUtil.getErrorMessage("You must provide an entity to spawn!"));
            return;
        }

        String toSpawn = args[0].toLowerCase();
        // First look for a custom mob
        for (CustomEntityType type : CustomEntityType.values()) {
            if (type.name().equalsIgnoreCase(toSpawn)) {
                LeveledEntity entity = SMPRPG.getInstance().getEntityService().spawnCustomEntity(type, commandSourceStack.getLocation());
                if (entity == null) {
                    executor.sendMessage(ChatUtil.getErrorMessage("Failed to spawn a " + toSpawn + ". Check console for details"));
                    return;
                }
                entity.setup();
                executor.sendMessage(ChatUtil.getSuccessMessage("Successfully spawned a " + toSpawn));
                return;
            }
        }

        // Look for a vanilla mob
        for (EntityType type : EntityType.values()) {
            if (type.name().equalsIgnoreCase(toSpawn)) {
                commandSourceStack.getLocation().getWorld().spawnEntity(commandSourceStack.getLocation(), type, CreatureSpawnEvent.SpawnReason.CUSTOM);
                executor.sendMessage("Successfully spawned a " + toSpawn);
                return;
            }
        }

        executor.sendMessage(ChatUtil.getErrorMessage("Failed to find an entity with name: " + toSpawn));
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (args.length != 1)
            return super.suggest(commandSourceStack, args);

        List<String> OPTIONS = new ArrayList<>();
        for (CustomEntityType customEntityType : CustomEntityType.values())
            OPTIONS.add(customEntityType.name().toLowerCase());
        for (EntityType entityType : EntityType.values())
            OPTIONS.add(entityType.name().toLowerCase());

        String soFar = args[0].toLowerCase();
        if (soFar.isEmpty())
            return OPTIONS;

        List<String> valid = new ArrayList<>();
        for (String option : OPTIONS)
            if (option.contains(soFar))
                valid.add(option);

        return valid;
    }
}

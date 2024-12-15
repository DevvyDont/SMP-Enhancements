package xyz.devvydont.treasureitems;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.treasureitems.commands.BaseCommand;
import xyz.devvydont.treasureitems.commands.GiveCustomItemsCommand;
import xyz.devvydont.treasureitems.commands.ViewCustomItemsCommand;

public class TreasureItemsBootstrapper implements PluginBootstrap {

    private final BaseCommand[] COMMANDS = {
            new GiveCustomItemsCommand(),
            new ViewCustomItemsCommand()
    };

    @Override
    public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
        LifecycleEventManager<BootstrapContext> manager = bootstrapContext.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            for (BaseCommand command : COMMANDS)
                commands.register(command.getName(), "todo", command);
        });
    }

}

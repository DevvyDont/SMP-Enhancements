package xyz.devvydont.smprpg;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.commands.economy.CommandBalance;
import xyz.devvydont.smprpg.commands.economy.CommandBalanceTop;
import xyz.devvydont.smprpg.commands.economy.CommandDeposit;
import xyz.devvydont.smprpg.commands.economy.CommandWithdrawal;
import xyz.devvydont.smprpg.commands.entity.CommandSummon;
import xyz.devvydont.smprpg.commands.items.CommandGiveItem;
import xyz.devvydont.smprpg.commands.items.CommandSearchItem;
import xyz.devvydont.smprpg.commands.player.CommandReforge;
import xyz.devvydont.smprpg.commands.player.CommandSkill;
import xyz.devvydont.smprpg.commands.player.CommandStatistics;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.services.EnchantmentService;

public class SMPRPGBootstrapper implements PluginBootstrap {

    private void bootstrapCommands(BootstrapContext context) {
        CommandBase[] commandsToRegister = new CommandBase[] {
                new CommandBalance("balance"),
                new CommandBalanceTop("balancetop"),
                new CommandDeposit("deposit"),
                new CommandWithdrawal("withdrawal"),
                new CommandGiveItem("give"),
                new CommandSearchItem("search"),
                new CommandStatistics("statistics"),
                new CommandSkill("skill"),
                new CommandSummon("summon"),
                new CommandReforge("reforge")
        };

        LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            for (CommandBase command : commandsToRegister)
                commands.register(command.getName(), command.getDescription(), command.getAliases(), command);
        });
    }

    private void bootstrapEnchantments(BootstrapContext context) {
        // Register a new handled for the freeze lifecycle event on the enchantment registry
        for (CustomEnchantment enchantment : EnchantmentService.CUSTOM_ENCHANTMENTS)
            enchantment.bootstrap(context);
    }

    @Override
    public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
        bootstrapCommands(bootstrapContext);
        bootstrapEnchantments(bootstrapContext);
    }
}

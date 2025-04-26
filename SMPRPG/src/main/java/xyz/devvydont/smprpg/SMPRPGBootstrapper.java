package xyz.devvydont.smprpg;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.commands.economy.*;
import xyz.devvydont.smprpg.commands.enchantments.CommandEnchantments;
import xyz.devvydont.smprpg.commands.entity.CommandSummon;
import xyz.devvydont.smprpg.commands.inventory.CommandPeek;
import xyz.devvydont.smprpg.commands.items.CommandGiveItem;
import xyz.devvydont.smprpg.commands.items.CommandSearchItem;
import xyz.devvydont.smprpg.commands.items.CommandTrashItems;
import xyz.devvydont.smprpg.commands.player.*;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.services.EnchantmentService;

public class SMPRPGBootstrapper implements PluginBootstrap {

    private void bootstrapCommands(BootstrapContext context) {
        CommandBase[] commandsToRegister = new CommandBase[] {
                new CommandEcoAdmin("eco"),
                new CommandBalance("balance"),
                new CommandBalanceTop("balancetop"),
                new CommandDifficulty("difficulty"),
                new CommandDeposit("deposit"),
                new CommandWithdrawal("withdrawal"),
                new CommandGiveItem("give"),
                new CommandSearchItem("search"),
                new CommandStatistics("statistics"),
                new CommandSkill("skill"),
                new CommandSummon("summon"),
                new CommandReforge("reforge"),
                new CommandWhatAmIHolding("whatamiholding"),
                new CommandPeek("peek"),
                new CommandTrashItems("trash"),
                new CommandEnchantments("enchantments")
        };

        var manager = context.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final var commands = event.registrar();

            for (var command : commandsToRegister)
                commands.register(command.getName(), command.getDescription(), command.getAliases(), command);
        });
    }

    private void bootstrapEnchantments(BootstrapContext context) {
        // Register a new handled for the freeze lifecycle event on the enchantment registry
        for (var enchantment : EnchantmentService.CUSTOM_ENCHANTMENTS)
            enchantment.bootstrap(context);
    }

    @Override
    public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
        bootstrapCommands(bootstrapContext);
        bootstrapEnchantments(bootstrapContext);
    }
}

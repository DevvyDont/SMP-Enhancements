package xyz.devvydont.smprpg.commands.entity;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandAttribute extends CommandBase {

    public static final List<String> SUBCOMMANDS = List.of("base", "get", "modifier");
    
    public record Context(CommandSender sender, LivingEntity target, AttributeWrapper attribute, String[] args){}

    public CommandAttribute(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {

        if (!(commandSourceStack.getExecutor() instanceof Player)) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.alert("You must be a player to use this command!"));
            return;
        }

        // We need an attribute.
        if (args.length <= 0) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.alert("You must specify an attribute!"));
            return;
        }

        AttributeWrapper attribute;
        try {
            attribute = AttributeWrapper.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.alert(args[0] + " is not a valid attribute!"));
            return;
        }

        // We need a subcommand.
        if (args.length <= 1 || !SUBCOMMANDS.contains(args[1].toLowerCase())) {
            commandSourceStack.getSender().sendMessage(ComponentUtils.alert("You must provide a valid subcommand! < base | get | modifier >"));
            return;
        }
        
        var context = new Context(commandSourceStack.getSender(), (Player)commandSourceStack.getSender(), attribute, args);

        // Handle subcommand.
        switch (args[1].toLowerCase()) {
            case "base":
                this.executeBase(context);
                break;
            case "modifier":
                this.executeModifier(context);
                break;
            case "get":
                this.executeGet(context);
                break;
        }
        
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        // Provide attributes for the first argument.
        if (args.length == 0 || args.length == 1) {
            var attributes = Arrays.stream(AttributeWrapper.values()).map(a -> a.name().toLowerCase());
            return this.generateArgumentCollection(args.length != 0 ? args[0] : "", attributes.toList());
        }

        // Provide subcommands for the second argument.
        if (args.length == 2) {
            return this.generateArgumentCollection(args[1], SUBCOMMANDS);
        }

        // Sub-sub arguments.
        if (args.length == 3 && args[1].equalsIgnoreCase("base")) {
            return this.generateArgumentCollection(args[2], "set", "get");
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("modifier")) {
            return this.generateArgumentCollection(args[2], "add", "remove", "clear");
        }

        return super.suggest(commandSourceStack, args);
    }

    private void executeBase(Context context) {

        if (context.args().length <= 2) {
            context.sender().sendMessage(ComponentUtils.error("Missing argument! < get | set >"));
            return;
        }

        var attribute = SMPRPG.getInstance().getAttributeService().getAttribute(context.target(), context.attribute());
        if (attribute == null) {
            context.sender().sendMessage(ComponentUtils.error("Attribute is not registered to the target!"));
            return;
        }
        
        if (context.args()[2].equalsIgnoreCase("set")) {
            try {
                var newValue = Double.parseDouble(context.args()[3]);
                attribute.setBaseValue(newValue);
                attribute.save(context.target(), context.attribute());
                context.sender().sendMessage(ComponentUtils.success("Base value set to " + newValue));
            }
            catch (NumberFormatException e) {
                context.sender().sendMessage(ComponentUtils.alert("Invalid number provided!"));
            }
            catch (ArrayIndexOutOfBoundsException e) {
                context.sender().sendMessage(ComponentUtils.alert("Please provide a number!"));
            }
            return;
        }

        if (context.args()[2].equalsIgnoreCase("get")) {

            context.sender().sendMessage(ComponentUtils.alert(ComponentUtils.merge(
                    ComponentUtils.create("The attribute"), ComponentUtils.SPACE,
                    ComponentUtils.create(context.attribute().DisplayName, NamedTextColor.GOLD), ComponentUtils.SPACE,
                    ComponentUtils.create("has a base value of"), ComponentUtils.SPACE,
                    ComponentUtils.create(String.format("%.2f", attribute.getBaseValue()), NamedTextColor.GREEN)
            )));

            return;
        }

        context.sender().sendMessage(ComponentUtils.error("Invalid argument provided! < get | set >"));

    }

    private void executeGet(Context context) {

        var attribute = SMPRPG.getInstance().getAttributeService().getAttribute(context.target(), context.attribute());
        if (attribute == null) {
            context.sender.sendMessage(ComponentUtils.error("The attribute is not registered to your target."));
            return;
        }

        // Simply spit back what the attribute is.
        context.sender().sendMessage(ComponentUtils.alert(ComponentUtils.merge(
                ComponentUtils.create("The attribute"), ComponentUtils.SPACE,
                ComponentUtils.create(context.attribute().DisplayName, NamedTextColor.GOLD), ComponentUtils.SPACE,
                ComponentUtils.create("has a final value of"), ComponentUtils.SPACE,
                ComponentUtils.create(String.format("%.2f", attribute.getValue()), NamedTextColor.GREEN)
        )));
    }

    private void executeModifier(Context context) {

        var attribute = SMPRPG.getInstance().getAttributeService().getAttribute(context.target(), context.attribute());
        if (attribute == null) {
            context.sender.sendMessage(ComponentUtils.error("The attribute is not registered to your target."));
            return;
        }

        if (context.args().length <= 2) {
            context.sender.sendMessage(ComponentUtils.error("Missing argument! < add | remove | clear >"));
            return;
        }

        if (context.args()[2].equalsIgnoreCase("clear")) {
            attribute.clearModifiers();
            attribute.save(context.target(), context.attribute());
            context.sender().sendMessage(ComponentUtils.success("Modifiers cleared!"));
            return;
        }

        context.sender.sendMessage(ComponentUtils.error("Invalid argument provided! < add | remove | clear >"));
    }

    @Override
    public @Nullable String permission() {
        return "smprpg.command.attribute";
    }
}

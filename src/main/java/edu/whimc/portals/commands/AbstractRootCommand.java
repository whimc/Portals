package edu.whimc.portals.commands;

import edu.whimc.portals.utils.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A blank template for any root command within this plugin, designed
 * to be the first tier of a two-tier command tree, of which
 * {@link AbstractSubCommand}s constitute the second tier.
 */
public abstract class AbstractRootCommand implements CommandExecutor, TabCompleter {

    private final Map<String, AbstractSubCommand> subCommands = new HashMap<>();

    /**
     * Register a sub command below this root command.
     *
     * @param subCommand a sub command
     */
    protected final void addSubCommand(final AbstractSubCommand subCommand) {
        this.subCommands.put(subCommand.getSubCommand(), subCommand);
    }

    /**
     * Get the sub command associated with this name, or null if cannot be found.
     *
     * @param subCommand the sub command name
     * @return the appropriate sub command object, or null if cannot be found
     */
    @Nullable
    protected final AbstractSubCommand getSubCommand(final String subCommand) {
        return subCommands.getOrDefault(subCommand, null);
    }

    protected final Set<String> getSubCommandNames() {
        return subCommands.keySet();
    }

    /**
     * Message a command sender a list of sub commands.
     *
     * @param sender the command sender
     */
    protected final void sendCommands(final CommandSender sender) {
        Messenger.msg(sender, Messenger.Message.LINE_COMMAND_LIST.toString());
        subCommands.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(e -> Messenger.msg(sender, e.getValue().getHelpLine()));
        Messenger.msg(sender, "&7/destination &8- &fList commands for destinations");
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender,
                                   @NotNull Command cmd,
                                   @NotNull String commandLabel,
                                   String[] args) {
        if (args.length == 0){
            sendCommands(sender);
            return true;
        }

        AbstractSubCommand subCmd = getSubCommand(args[0].toLowerCase());
        if (subCmd == null) {
            sendCommands(sender);
            return true;
        }

        return subCmd.executeSubCommand(sender, args);
    }

    @Override
    public final List<String> onTabComplete(@NotNull CommandSender sender,
                                            @NotNull Command command,
                                            @NotNull String alias,
                                            String[] args) {
        if (args.length == 0) {
            return getSubCommandNames().stream().sorted().collect(Collectors.toList());
        }

        if (args.length == 1) {
            return getSubCommandNames()
                    .stream()
                    .filter(v -> v.startsWith(args[0].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList());
        }

        AbstractSubCommand subCmd = getSubCommand(args[0].toLowerCase());
        if (subCmd == null) {
            return null;
        }

        if (!sender.hasPermission(subCmd.getPermission()) || args.length > subCmd.getArguments().length) {
            return Collections.emptyList();
        }
        return subCmd.onTabComplete(sender, args);
    }

}

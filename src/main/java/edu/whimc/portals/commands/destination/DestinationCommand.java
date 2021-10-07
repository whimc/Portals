package edu.whimc.portals.commands.destination;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;

import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * Main handler for the "/destination" root command.
 */
public class DestinationCommand implements CommandExecutor,TabCompleter {

    private Map<String, AbstractSubCommand> subCommands = new HashMap<>();

    /**
     * Constructs a DestinationCommand.
     *
     * @param plugin the instance of the plugin.
     */
    public DestinationCommand(Main plugin) {
        // set up permissions
        Permission perm = new Permission(Main.PERM_PREFIX + ".destination.*");
        perm.addParent(Main.PERM_PREFIX + ".*", true);
        Bukkit.getPluginManager().addPermission(perm);

        // add all subcommands
        subCommands.put("change", new DestinationChange(plugin, "destination", "change"));
        subCommands.put("clear", new DestinationClear(plugin, "destination", "clear"));
        subCommands.put("create", new DestinationCreate(plugin, "destination", "create"));
        subCommands.put("info", new DestinationInfo(plugin, "destination", "info"));
        subCommands.put("list", new DestinationList(plugin, "destination", "list"));
        subCommands.put("purge", new DestinationPurge(plugin, "destination", "purge"));
        subCommands.put("remove", new DestinationRemove(plugin, "destination", "remove"));
        subCommands.put("set", new DestinationSet(plugin, "destination", "set"));
        subCommands.put("sethere", new DestinationSetHere(plugin, "destination", "sethere"));
        subCommands.put("teleport", new DestinationTeleport(plugin, "destination", "teleport"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // send command usages to user if no arguments provided
        if (args.length == 0){
            sendCommands(sender);
            return true;
        }

        // send command usages to user if subcommand invalid
        AbstractSubCommand subCmd = subCommands.getOrDefault(args[0].toLowerCase(), null);
        if (subCmd == null) {
            sendCommands(sender);
            return true;
        }

        return subCmd.executeSubCommand(sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return subCommands.keySet().stream().sorted().collect(Collectors.toList());
        }

        if (args.length == 1) {
            return subCommands.keySet()
                    .stream()
                    .filter(v -> v.startsWith(args[0].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList());
        }

        AbstractSubCommand subCmd = subCommands.getOrDefault(args[0].toLowerCase(), null);
        if (subCmd == null) {
            return null;
        }

        return subCmd.executeOnTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    /**
     * Outputs all commands and their usages in the chat / server logs.
     *
     * @param sender the command's sender.
     */
    private void sendCommands(CommandSender sender){
        Messenger.msg(sender, Message.LINE_COMMAND_LIST.toString());
        subCommands.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .forEachOrdered(e -> Messenger.msg(sender, e.getValue().getHelpLine()));
        Messenger.msg(sender, "&7/portal &8- &fList commands for portals");
    }

}

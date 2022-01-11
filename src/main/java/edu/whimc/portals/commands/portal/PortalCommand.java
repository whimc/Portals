package edu.whimc.portals.commands.portal;

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
 * Main handler for "/portal" root command.
 */
public class PortalCommand implements CommandExecutor, TabCompleter {

    private Map<String, AbstractSubCommand> subCommands = new HashMap<>();

    /**
     * Constructs a PortalCommand.
     *
     * @param plugin the instance of the plugin.
     */
    public PortalCommand(Main plugin) {
        // set up permissions
        Permission perm = new Permission(Main.PERM_PREFIX + ".portal.*");
        perm.addParent(Main.PERM_PREFIX + ".*", true);
        Bukkit.getPluginManager().addPermission(perm);

        // add all subcommands
        subCommands.put("create", new PortalCreate(plugin, "portal", "create"));
        subCommands.put("debug", new PortalDebug(plugin, "portal", "debug"));
        subCommands.put("info", new PortalInfo(plugin, "portal", "info"));
        subCommands.put("list", new PortalList(plugin, "portal", "list"));
        subCommands.put("permission", new PortalPermission(plugin, "portal", "permission"));
        subCommands.put("purge", new PortalPurge(plugin, "portal", "purge"));
        subCommands.put("refill", new PortalRefill(plugin, "portal", "refill"));
        subCommands.put("remove", new PortalRemove(plugin, "portal", "remove"));
        subCommands.put("reshape", new PortalReshape(plugin, "portal", "reshape"));
        subCommands.put("setfiller", new PortalSetFiller(plugin, "portal", "setfiller"));
        subCommands.put("teleport", new PortalTeleport(plugin, "portal", "teleport"));
        subCommands.put("tool", new PortalTool(plugin, "portal", "tool"));
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
        Messenger.msg(sender, "&7/destination &8- &fList commands for destinations");
    }

}
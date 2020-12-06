package edu.whimc.portals.commands.portal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

public class PortalCommand implements CommandExecutor, TabCompleter {

    private Map<String, AbstractSubCommand> subCommands = new HashMap<>();

    public PortalCommand(Main plugin) {
        subCommands.put("create", new PortalCreate(plugin, "portal", "create"));
        subCommands.put("debug", new PortalDebug(plugin, "portal", "debug"));
        subCommands.put("info", new PortalInfo(plugin, "portal", "info"));
        subCommands.put("list", new PortalList(plugin, "portal", "list"));
        subCommands.put("refill", new PortalRefill(plugin, "portal", "refill"));
        subCommands.put("remove", new PortalRemove(plugin, "portal", "remove"));
        subCommands.put("setfiller", new PortalSetFiller(plugin, "portal", "setfiller"));
        subCommands.put("tool", new PortalTool(plugin, "portal", "tool"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 0){
            sendCommands(sender);
            return true;
        }

        AbstractSubCommand subCmd = subCommands.getOrDefault(args[0].toLowerCase(), null);
        if (subCmd == null) {
            sendCommands(sender);
            return true;
        }

        return subCmd.executeSubCommand(sender, args);
    }

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

    private void sendCommands(CommandSender sender){
        Messenger.msg(sender, Message.LINE_COMMAND_LIST.toString());
        subCommands.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .forEachOrdered(e -> Messenger.msg(sender, e.getValue().getHelpLine()));
        Messenger.msg(sender, "&7/destination &8- &fList commands for destinations");
    }

}
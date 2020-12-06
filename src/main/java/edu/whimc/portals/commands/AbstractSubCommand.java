package edu.whimc.portals.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Main;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

public abstract class AbstractSubCommand {

    private static final String PRIMARY = "&7";
    private static final String SECONDARY = "&b";
    private static final String ACCENT = "&3";

    protected Main plugin;
    private String baseCommand;
    private String subCommand;

    private String description = "";
    private String[] arguments = {};
    private boolean requiresPlayer = false;

    public AbstractSubCommand(Main plugin, String baseCommand, String subCommand) {
        this.plugin = plugin;
        this.baseCommand = baseCommand;
        this.subCommand = subCommand;
    }

    protected void description(String desc) { this.description = desc; }
    protected void arguments(String args) { this.arguments = args.split(" "); }
    protected void requiresPlayer() { this.requiresPlayer = true; }

    protected List<String> onTabComplete(CommandSender sender, String[] args) { return null; }

    public List<String> executeOnTabComplete(CommandSender sender, String args[]) {
        if (args.length > arguments.length) {
            return null;
        }
        return onTabComplete(sender, args);
    }

    private String formatArg(String arg) {
        return PRIMARY + "<" + ACCENT + arg + PRIMARY + ">";
    }

    public String getCommand() {
        return PRIMARY + "/" + this.baseCommand + " " + SECONDARY + this.subCommand;
    }

    public String getUsage() {
        String usage = getCommand() + " ";
        for (String arg : this.arguments) {
            usage += formatArg(arg) + " ";
        }
        return usage.trim();
    }

    public String getHelpLine() {
        return this.getCommand() + " &8- &f" + this.description;
    }

    public String getPermission() {
        return "whimc-portals." + baseCommand.toLowerCase() + "." + subCommand.toLowerCase();
    }

    protected abstract boolean onCommand(CommandSender sender, String[] args);

    public boolean executeSubCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            Messenger.msg(sender, Message.NO_PERMISSION);
            return true;
        }

        if (this.requiresPlayer && !(sender instanceof Player)) {
            Messenger.msg(sender, Message.MUST_BE_PLAYER);
            return true;
        }

        if (args.length - 1 < this.arguments.length) {
            List<String> missingArgsList = new ArrayList<>();
            for (int ind = args.length - 1; ind < arguments.length; ind++) {
                missingArgsList.add(formatArg(arguments[ind]));
            }
            String missingArgs = String.join("&7, ", missingArgsList);

            Messenger.usage(sender, missingArgs, description, getUsage());
            return true;
        }

        return onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
    }

}

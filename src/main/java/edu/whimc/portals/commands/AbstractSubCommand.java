package edu.whimc.portals.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import edu.whimc.portals.Main;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * This class is a template for all SubCommands.
 */
public abstract class AbstractSubCommand {

    /* Text color presets. */
    private static final String PRIMARY = "&7";
    private static final String SECONDARY = "&b";
    private static final String ACCENT = "&3";
    private static final String SEPARATOR = "&8";
    private static final String TEXT = "&f";

    /* The instance of the plugin. */
    protected Main plugin;
    /* The base command keyword. */
    private String baseCommand;
    /* The sub command keyword. */
    private String subCommand;
    /* The permission level required to use the command. */
    private String permission;

    /* The description of the command. */
    private String description = "";
    /* The arguments that the command takes. */
    private String[] arguments = {};
    /* If the command requires the sender to be a player. */
    private boolean requiresPlayer = false;

    /**
     * Constructs a command with the given arguments.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public AbstractSubCommand(Main plugin, String baseCommand, String subCommand) {
        this.plugin = plugin;
        this.baseCommand = baseCommand;
        this.subCommand = subCommand;

        this.permission = Main.PERM_PREFIX + "." + baseCommand.toLowerCase() + "." + subCommand.toLowerCase();
        Permission perm = new Permission(this.permission);
        perm.addParent(Main.PERM_PREFIX + "." + baseCommand + ".*", true);
        Bukkit.getPluginManager().addPermission(perm);
    }

    /**
     * Sets the description of the command.
     *
     * @param desc the description of the command.
     */
    protected void description(String desc) { this.description = desc; }

    /**
     * Sets the arguments for the command.
     *
     * @param args the arguments to require.
     */
    protected void arguments(String args) { this.arguments = args.split(" "); }

    /** Sets the player requirement to true. */
    protected void requiresPlayer() { this.requiresPlayer = true; }

    /**
     * Handles tab completion for a subcommand.
     * Returns an empty list by default.
     *
     * @param sender The command's sender.
     * @param args The arguments passed.
     * @return An empty List of Strings.
     */
    protected List<String> onTabComplete(CommandSender sender, String[] args) { return Arrays.asList(); }

    /**
     * Wrapper for {@link #onTabComplete(CommandSender, String[])}.
     * Returns an empty list if the sender does not have permission for the subcommand or if they
     * have surpassed the max number of arguments.
     *
     * @param sender The command's sender.
     * @param args The passed arguments.
     * @return An empty List of Strings.
     */
    public List<String> executeOnTabComplete(CommandSender sender, String args[]) {
        if (!sender.hasPermission(getPermission()) || args.length > arguments.length) {
            return Arrays.asList();
        }
        return onTabComplete(sender, args);
    }

    /**
     * Formats the argument with a list of its options.
     *
     * @param arg the argument to format.
     * @return the reformatted argument.
     */
    private String formatArg(String arg) {
        List<String> options = Stream.of(arg.split(Pattern.quote("|")))
                .map(v -> ACCENT + v.replace("'", ACCENT + "\"" + SECONDARY))
                .collect(Collectors.toList());
        return PRIMARY + "<" + ACCENT + String.join(SEPARATOR + " | " + ACCENT, options) + PRIMARY + ">";
    }

    /** @return The command in the form: "/basecommand subcommand". */
    public String getCommand() {
        return PRIMARY + "/" + this.baseCommand + " " + SECONDARY + this.subCommand;
    }

    /** @return The subcommand's usage. */
    public String getUsage() {
        String usage = getCommand() + " ";
        for (String arg : this.arguments) {
            usage += formatArg(arg) + " ";
        }
        return usage.trim();
    }

    /** @return The command and its description. */
    public String getHelpLine() {
        return this.getCommand() + SEPARATOR + " - " + TEXT + this.description;
    }

    /** @return The permission required to use the command. */
    public String getPermission() {
        return this.permission;
    }

    /** @return The description of the subcommand. */
    public String getDescription() {
        return this.description;
    }

    /**
     * Runs when the subcommand was properly executed (with correct permissions and arguments).
     *
     * @param sender The command's sender.
     * @param args   The arguments passed.
     * @return Whether the command has been run or not.
     */
    protected abstract boolean onCommand(CommandSender sender, String[] args);

    /**
     * Wrapper function to {@link #onCommand(CommandSender, String[])}.
     * Checks permissions of the sender and for a valid number of arguments.
     * If these checks fail, the command routine is not run.
     *
     * @param sender The command's sender.
     * @param args The passed arguments.
     * @return Whether or not the command has been executed.
     */
    public boolean executeSubCommand(CommandSender sender, String[] args) {
        // warn user if current sender does not have sufficient permissions
        if (!sender.hasPermission(getPermission())) {
            Messenger.msg(sender, Message.NO_PERMISSION);
            return true;
        }

        // warn user if command requires player and current sender is not a player
        if (this.requiresPlayer && !(sender instanceof Player)) {
            Messenger.msg(sender, Message.MUST_BE_PLAYER);
            return true;
        }

        // warn if not enough arguments provided
        if (args.length - 1 < this.arguments.length) {
            List<String> missingArgsList = new ArrayList<>();
            for (int ind = args.length - 1; ind < arguments.length; ind++) {
                missingArgsList.add(formatArg(arguments[ind]));
            }
            String missingArgs = String.join("&7, ", missingArgsList);

            Messenger.usageMissingArgs(sender, missingArgs, description, getUsage());
            return true;
        }

        return onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
    }

}

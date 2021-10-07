package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command for displaying information about the current destination.
 * Command: "/destination info"
 */
public class DestinationInfo extends AbstractSubCommand {

    /**
     * Constructs a DestinationInfo.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the subcommand keyword.
     */
    public DestinationInfo(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Gives information about a destination");
        super.arguments("destination");
    }

    /**
     * {@inheritDoc}
     *
     * Sends information about the current destination to the sender.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // get the current destination
        Destination dest = Destination.getDestination(args[0]);

        // notify if destination does not exist and abort command execution
        if (dest == null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // sends the destination information
        Messenger.sendDestinationInfo(sender, dest);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Destination.getTabCompletedDestinations(args[0]);
    }

}

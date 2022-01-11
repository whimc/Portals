package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command for removing a destination.
 * Command: "/destination remove"
 */
public class DestinationRemove extends AbstractSubCommand {

    /**
     * Constructs a DestinationRemove.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public DestinationRemove(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Removes a destination");
        super.arguments("destination");
    }

    /**
     * {@inheritDoc}
     *
     * Removes the current destination.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // get the current destination
        Destination dest = Destination.getDestination(args[0]);

        // notify if portal does not exist and abort command execution
        if (dest == null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // remove the current destination
        dest.remove();
        Messenger.msg(sender, ReplaceMessage.DESTINATION_REMOVE_SUCCESS, dest.getName());
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

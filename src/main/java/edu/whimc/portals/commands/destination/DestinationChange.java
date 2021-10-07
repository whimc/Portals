package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to set the location of a destination to the user's current position.
 * Command: "/destination change"
 */
public class DestinationChange extends AbstractSubCommand {

    /**
     * Constructs a DestinationChange.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public DestinationChange(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Sets the location of a destination to your current position");
        super.arguments("destination");
        super.requiresPlayer();
    }

    /**
     * {@inheritDoc}
     *
     * Sets the destination to the sender's current location if it exists.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // get the sender's current destination
        Destination dest = Destination.getDestination(args[0]);

        // notify if destination does not exist and abort command execution
        if (dest == null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // set the destination to the sender's current location
        dest.setLocation(((Player) sender).getLocation());
        Messenger.msg(sender, ReplaceMessage.DESTINATION_CHANGE_SUCCESS, dest.getName());
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

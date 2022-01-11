package edu.whimc.portals.commands.destination;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to create a new destination at the user's current position.
 * Command: "/destination create"
 */
public class DestinationCreate extends AbstractSubCommand {

    /**
     * Constructs a DestinationCreate.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public DestinationCreate(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Creates a new destination at your current location");
        super.arguments("name");
        super.requiresPlayer();
    }

    /**
     * {@inheritDoc}
     *
     * Creates a new destination at the user's current position if it exists.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if reserved destination name provided and abort command execution
        if (args[0].equalsIgnoreCase(Destination.NONE)) {
            Messenger.msg(sender, Message.NONE_RESERVED_WORD);
            return true;
        }

        // get the current destination
        Destination dest = Destination.getDestination(args[0]);

        // notify if destination does exist and abort command execution
        if (dest != null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_ALREADY_EXISTS, dest.getName());
            return true;
        }

        // creates a new destination at the user's current location
        Destination.createDestination(plugin, args[0], ((Player) sender).getLocation());
        Messenger.msg(sender, ReplaceMessage.DESTINATION_CREATE_SUCCESS, args[0]);
        return true;
    }

}

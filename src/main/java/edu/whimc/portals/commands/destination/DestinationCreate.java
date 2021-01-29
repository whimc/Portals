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
 * Allows a user to create a {@link Destination} for
 * the purpose of later linking a {@link edu.whimc.portals.Portal}
 * to that {@link Destination}.
 *
 * @see DestinationCommand
 */
public final class DestinationCreate extends AbstractSubCommand {

    public DestinationCreate(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.setDescription("Creates a new destination at your current location");
        super.provideArguments("name");
        super.setRequiresPlayer(true);
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(Destination.NONE)) {
            Messenger.msg(sender, Message.NONE_RESERVED_WORD);
            return true;
        }

        Destination dest = Destination.getDestination(args[0]);
        if (dest != null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_ALREADY_EXISTS, dest.getName());
            return true;
        }

        Destination.createDestination(plugin, args[0], ((Player) sender).getLocation());
        Messenger.msg(sender, ReplaceMessage.DESTINATION_CREATE_SUCCESS, args[0]);
        return true;
    }

}

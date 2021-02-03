package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Provide a user with information about a {@link Destination}.
 *
 * @see DestinationCommand
 */
public final class DestinationInfo extends AbstractSubCommand {

    public DestinationInfo(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Gives information about a destination");
        super.provideArguments("destination");
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        Destination dest = Destination.getDestination(args[0]);
        if (dest == null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[0]);
            return true;
        }

        Messenger.sendDestinationInfo(sender, dest);
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Destination.getTabCompletedDestinations(args[0]);
    }

}

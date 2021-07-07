package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allow a user to set the {@link Destination} of a {@link Portal}.
 *
 * @see DestinationCommand
 */
public final class DestinationSet extends AbstractSubCommand {

    public DestinationSet(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Sets the destination of a portal");
        super.provideArguments("portal destination");
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        Destination dest = Destination.getDestination(args[1]);
        if (dest == null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[1]);
            return true;
        }

        portal.setDestination(dest);
        Messenger.msg(sender, ReplaceMessage.DESTINATION_SET, portal.getName(), dest.getName());
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Destination.getTabCompletedDestinations(args[1]);
        }
        return Portal.getTabCompletedPortals(args[0]);
    }

}

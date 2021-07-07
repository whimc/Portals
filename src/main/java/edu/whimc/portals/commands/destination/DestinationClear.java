package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allows a user to remove a {@link edu.whimc.portals.Destination}.
 *
 * @see DestinationCommand
 */
public final class DestinationClear extends AbstractSubCommand {

    public DestinationClear(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Removes the destination of a portal");
        super.provideArguments("portal");
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        portal.setDestination(null);
        Messenger.msg(sender, ReplaceMessage.PORTAL_DEST_CLEARED, portal.getName());
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}

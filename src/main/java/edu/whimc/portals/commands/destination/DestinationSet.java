package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command for setting the destination of a portal.
 * Command: "/destination set"
 */
public class DestinationSet extends AbstractSubCommand {

    /**
     * Constructs a DestinationSet.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public DestinationSet(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Sets the destination of a portal");
        super.arguments("portal destination");
    }

    /**
     * {@inheritDoc}
     *
     * Sets the destination of a portal.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // get the current portal
        Portal portal = Portal.getPortal(args[0]);

        // notify if portal does not exist and abort command execution
        if (portal == null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // get the current destination
        Destination dest = Destination.getDestination(args[1]);

        // notify if destination does not exist and abort command execution
        if (dest == null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[1]);
            return true;
        }

        // set the portal's destination
        portal.setDestination(dest);
        Messenger.msg(sender, ReplaceMessage.DESTINATION_SET, portal.getName(), dest.getName());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Destination.getTabCompletedDestinations(args[1]);
        }
        return Portal.getTabCompletedPortals(args[0]);
    }

}

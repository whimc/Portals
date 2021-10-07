package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to remove the destination of a portal.
 * Command: "/destination clear"
 */
public class DestinationClear extends AbstractSubCommand {

    /**
     * Construct a DestinationClear.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the subcommand keyword.
     */
    public DestinationClear(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Removes the destination of a portal");
        super.arguments("portal");
    }

    /**
     * {@inheritDoc}
     *
     * Removes the current destination of a portal if the portal exists.
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

        // clear the current portal's destination
        portal.setDestination(null);
        Messenger.msg(sender, ReplaceMessage.PORTAL_DEST_CLEARED, portal.getName());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}

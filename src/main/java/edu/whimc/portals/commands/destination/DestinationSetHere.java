package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to set the destination of the portal to the user's current location using
 * the name of the portal.
 * Command: "/destination sethere"
 */
public class DestinationSetHere extends AbstractSubCommand {

    /**
     * Constructs a DestinationSetHere.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the subcommand keyword.
     */
    public DestinationSetHere(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Sets the destination of a portal to your current location using the name of the portal");
        super.arguments("portal");
        super.requiresPlayer();
    }

    /**
     * {@inheritDoc}
     *
     * Set the destination of the current portal to the player's location using the name
     * of the portal.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // get the current portal
        Portal portal = Portal.getPortal(args[0]);

        // notify if portal does not exist and abort command execution
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        String destName = portal.getName();
        Player player = (Player) sender;
        Destination dest = Destination.getDestination(destName);

        // create a new destination if none exists, otherwise set the existing destination
        if (dest == null) {
            dest = Destination.createDestination(plugin, destName, player.getLocation());
        } else {
            dest.setLocation(player.getLocation());
        }

        // set the new destination of the portal
        portal.setDestination(dest);
        Messenger.msg(sender, ReplaceMessage.DESTINATION_SETHERE, portal.getName(), portal.getName());
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

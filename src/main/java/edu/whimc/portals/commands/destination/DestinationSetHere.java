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
 * Allow a user to create a {@link Destination} and set it to
 * a {@link Portal}. This is a simplification of using both
 * {@link DestinationCreate} and {@link DestinationSet}.
 *
 * @see DestinationCommand
 */
public final class DestinationSetHere extends AbstractSubCommand {

    public DestinationSetHere(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.setDescription("Sets the destination of a portal to your current location using the name of the portal");
        super.provideArguments("portal");
        super.setRequiresPlayer(true);
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        String destName = portal.getName();
        Player player = (Player) sender;
        Destination dest = Destination.getDestination(destName);

        if (dest == null) {
            dest = Destination.createDestination(plugin, destName, player.getLocation());
        } else {
            dest.setLocation(player.getLocation());
        }


        portal.setDestination(dest);
        Messenger.msg(sender, ReplaceMessage.DESTINATION_SETHERE, portal.getName(), portal.getName());
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}

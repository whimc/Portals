package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

public class DestinationSet extends AbstractSubCommand {

    public DestinationSet(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Sets the destination of a portal");
        super.arguments("portal destination");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
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
        Messenger.msg(sender, Messenger.prefix + "&aThe destination of '&2" + portal.getName() + "&a' " +
                "has been set to '&2" + dest.getName() + "&a'!");
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Portal.getTabCompletedPortals(args[1]);
        }
        return Destination.getTabCompletedDestinations(args[0]);
    }

}

package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.listeners.PortalEnterListener;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allow a user to teleport to the location of a {@link Portal}.
 * This is not to be confused with {@link edu.whimc.portals.commands.destination.DestinationTeleport}.
 *
 * @see PortalCommand
 */
public final class PortalTeleport extends AbstractSubCommand {

    public PortalTeleport(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Teleports you to a portal");
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

        if (!portal.isValid()) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_INVALID, portal.getName());
            Messenger.msg(sender, ReplaceMessage.SUGGEST_DELETE, "/portal remove " + portal.getName());
            return true;
        }

        Player player = (Player) sender;
        PortalEnterListener.addDebugPlayer(player);
        Location loc = portal.getSafeTeleportLocation();

        if (loc == null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_TELEPORT_FAILED, portal.getName());
            return true;
        }

        player.teleport(loc);
        Messenger.msg(sender, ReplaceMessage.PORTAL_TELEPORTED, portal.getName());
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}

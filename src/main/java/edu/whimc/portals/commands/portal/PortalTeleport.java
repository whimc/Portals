package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.listeners.PortalEnterListener;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

public class PortalTeleport extends AbstractSubCommand {

    public PortalTeleport(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Teleports you to a portal");
        super.arguments("portal");
        super.requiresPlayer();
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
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
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}

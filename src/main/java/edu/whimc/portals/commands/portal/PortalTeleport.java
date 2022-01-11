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

/**
 * The command to teleport the user to a portal.
 * Command: "/portal teleport"
 */
public class PortalTeleport extends AbstractSubCommand {

    /**
     * Constructs a PortalTeleport.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalTeleport(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Teleports you to a portal");
        super.arguments("portal");
        super.requiresPlayer();
    }

    /**
     * {@inheritDoc}
     *
     * Teleports the user to the provided portal.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if portal does not exist and abort command execution
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // notify if portal is invalid and abort command execution
        if (!portal.isValid()) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_INVALID, portal.getName());
            Messenger.msg(sender, ReplaceMessage.SUGGEST_DELETE, "/portal remove " + portal.getName());
            return true;
        }

        // get the player and portal location
        Player player = (Player) sender;
        PortalEnterListener.addDebugPlayer(player);
        Location loc = portal.getSafeTeleportLocation();

        // notify if portal location not found and abort command execution
        if (loc == null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_TELEPORT_FAILED, portal.getName());
            return true;
        }

        // teleport player to portal
        player.teleport(loc);
        Messenger.msg(sender, ReplaceMessage.PORTAL_TELEPORTED, portal.getName());
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

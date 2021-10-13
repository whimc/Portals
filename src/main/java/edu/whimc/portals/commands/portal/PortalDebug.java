package edu.whimc.portals.commands.portal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.listeners.PortalEnterListener;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * Allow a player to enable debug mode for portals, which
 * will provide the player with useful information when they would
 * normally teleport through the portal.
 *
 * @see edu.whimc.portals.Portal
 * @see PortalCommand
 */
public final class PortalDebug extends AbstractSubCommand {

    public PortalDebug(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Gives information about the portal you're entering instead of teleporting");
        super.setRequiresPlayer(true);
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (PortalEnterListener.playerIsDebug(player)) {
            PortalEnterListener.removeDebugPlayer(player);
            Messenger.msg(player, Message.DEBUG_DISABLE);
        } else {
            PortalEnterListener.addDebugPlayer(player);
            Messenger.msg(player, Message.DEBUG_ENABLE);
        }

        return true;
    }

}

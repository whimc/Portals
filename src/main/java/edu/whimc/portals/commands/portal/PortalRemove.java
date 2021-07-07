package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allow a user to remove a {@link Portal}.
 *
 * @see PortalCommand
 */
public final class PortalRemove extends AbstractSubCommand {

    public PortalRemove(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Removes a portal");
        super.provideArguments("portal");
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        portal.remove();
        Messenger.msg(sender, ReplaceMessage.PORTAL_REMOVE_SUCCESS, portal.getName());
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}

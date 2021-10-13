package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allow a user to regenerate the substance within a {@link Portal} in
 * the case that it was damaged.
 *
 * @see PortalCommand
 */
public final class PortalRefill extends AbstractSubCommand {

    public PortalRefill(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Regenerates the filler of a portal");
        super.provideArguments("portal");
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

        portal.addFiller();
        Messenger.msg(sender, ReplaceMessage.PORTAL_REFILLED, portal.getName(), portal.getFiller().toString());
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}

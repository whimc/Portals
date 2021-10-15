package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to regenerate the portal's filler.
 * Command: "/portal refill"
 */
public class PortalRefill extends AbstractSubCommand {

    /**
     * Constructs a PortalRefill.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalRefill(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Regenerates the filler of a portal");
        super.arguments("portal");
    }

    /**
     * {@inheritDoc}
     *
     * Regenerate the portal filler.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if portal does not exist and abort command execution
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // notify if portal is not valid, suggest deletion, and abort command execution
        if (!portal.isValid()) {
            Messenger.msg(sender, ReplaceMessage.PORTAL_INVALID, portal.getName());
            Messenger.msg(sender, ReplaceMessage.SUGGEST_DELETE, "/portal remove " + portal.getName());
            return true;
        }

        // regenerate the portal filler
        portal.addFiller();
        Messenger.msg(sender, ReplaceMessage.PORTAL_REFILLED, portal.getName(), portal.getFiller().toString());
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

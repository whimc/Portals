package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to remove a portal.
 * Command: "/portal remove"
 */
public class PortalRemove extends AbstractSubCommand {

    /**
     * Constructs a PortalRemove.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalRemove(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Removes a portal");
        super.arguments("portal");
    }

    /**
     * {@inheritDoc}
     *
     * Removes the portal.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if portal does not exist and abort command execution
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // remove the portal
        portal.remove();
        Messenger.msg(sender, ReplaceMessage.PORTAL_REMOVE_SUCCESS, portal.getName());
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

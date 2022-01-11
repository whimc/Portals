package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to display information about a portal.
 * Command: "/portal info"
 */
public class PortalInfo extends AbstractSubCommand {

    /**
     * Constructs a PortalInfo.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalInfo(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Gives information about a portal");
        super.arguments("portal");
    }

    /**
     * {@inheritDoc}
     *
     * Displays portal information to user.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if portal does not exist and abort command execution
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // display portal info to user
        Messenger.sendPortalInfo(sender, portal);
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

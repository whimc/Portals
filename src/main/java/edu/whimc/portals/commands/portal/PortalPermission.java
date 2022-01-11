package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command to set / remove the permissions of a portal.
 * Command: "/portal permission"
 */
public class PortalPermission extends AbstractSubCommand {

    /** Reserved word to clear permissions */
    private static final String PERMISSION_NONE = "none";

    /**
     * Constructs a PortalPermission.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalPermission(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Sets or removes portal permissions");
        super.arguments("portal permission|'" + PERMISSION_NONE + "'");
    }

    /**
     * {@inheritDoc}
     *
     * Sets / removes the permissions of a portal.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if portal does not exist and abort command execution
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // set / remove portal permissions
        String perm = args[1];
        if (perm.equalsIgnoreCase(PERMISSION_NONE)) {
            perm = null;
            Messenger.msg(sender, ReplaceMessage.PORTAL_PERM_CLEARED, portal.getName());
        } else {
            Messenger.msg(sender, ReplaceMessage.PORTAL_PERM_SET, portal.getName(), Portal.formatPermission(perm));
        }

        portal.setPermission(perm);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> res = Portal.getTabCompletedPermissions(args[1]);
            if (PERMISSION_NONE.startsWith(args[1].toLowerCase())) {
                res.add(PERMISSION_NONE);
            }
            return res;
        }

        return Portal.getTabCompletedPortals(args[0]);
    }

}

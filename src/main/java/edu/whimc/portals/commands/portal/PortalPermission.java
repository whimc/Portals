package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allow a user to set the permission associated with accessing a {@link Portal}.
 *
 * @see PortalCommand
 */
public final class PortalPermission extends AbstractSubCommand {

    private static final String PERMISSION_NONE = "none";

    public PortalPermission(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Sets or removes portal permissions");
        super.provideArguments("portal permission|'" + PERMISSION_NONE + "'");
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

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

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
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

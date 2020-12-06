package edu.whimc.portals.commands.portal;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

public class PortalRemove extends AbstractSubCommand {

    public PortalRemove(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Removes a portal");
        super.arguments("portal");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null){
            Messenger.msg(sender, ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        portal.setDestination(null);
        portal.remove();
        plugin.getPortalData().removeKey("Portals." + portal.getName());
        plugin.getPortalData().saveConfig();
        plugin.getPortalData().reloadConfig();

        Messenger.msg(sender, ReplaceMessage.PORTAL_REMOVE_SUCCESS, portal.getName());
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Portal.getTabCompletedPortals(args[0]);
    }

}

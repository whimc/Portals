package edu.whimc.portals.commands.portal;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PortalAllowCitizens extends AbstractSubCommand {
    /**
     * Constructs a PortalAllowCitizens command with the given arguments.
     *
     * @param plugin      the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand  the sub command keyword.
     */
    public PortalAllowCitizens(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Toggles Citizens NPCs' ability to travel through the portal.");
        super.requiresPlayer();
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        // notify if portal does not exist and abort command execution
        Portal portal = Portal.getPortal(args[0]);
        if (portal == null) {
            Messenger.msg(sender, Messenger.ReplaceMessage.PORTAL_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // notify if portal is invalid, suggest deletion, and abort command execution
        if (!portal.isValid()) {
            Messenger.msg(sender, Messenger.ReplaceMessage.PORTAL_INVALID, portal.getName());
            Messenger.msg(sender, Messenger.ReplaceMessage.SUGGEST_DELETE, "/portal remove " + portal.getName());
            return true;
        }

        portal.setAllowCitizens(!portal.getAllowCitizens());
        Messenger.msg(sender, portal.getName() + " allows citizens: " + portal.getAllowCitizens());

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

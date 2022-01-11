package edu.whimc.portals.commands.portal;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * The command to list all existing portals.
 * Command: "/portal list"
 */
public class PortalList extends AbstractSubCommand {

    /**
     * Constructs a PortalList.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public PortalList(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Lists all portals");
    }

    /**
     * {@inheritDoc}
     *
     * Displays a list of all existing portals to the user.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if there are no existing portals and abort command execution
        if (Portal.getPortals().size() == 0) {
            Messenger.msg(sender, Message.NO_PORTALS);
            return true;
        }

        // send a list of all existing portals to the user
        Messenger.msg(sender, Message.LINE_PORTAL_LIST);
        for (Portal portal:Portal.getPortals()) {
            Messenger.msg(sender, ChatColor.ITALIC + "- " + portal.toString());
        }
        Messenger.msg(sender, Message.LINE);

        return true;
    }

}

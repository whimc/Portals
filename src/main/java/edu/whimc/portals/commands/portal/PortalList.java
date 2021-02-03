package edu.whimc.portals.commands.portal;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * Provide a user with all registered {@link Portal}s.
 *
 * @see PortalCommand
 */
public final class PortalList extends AbstractSubCommand {

    public PortalList(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Lists all portals");
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        if (Portal.getPortals().size() == 0) {
            Messenger.msg(sender, Message.NO_PORTALS);
            return true;
        }

        Messenger.msg(sender, Message.LINE_PORTAL_LIST);
        for (Portal portal:Portal.getPortals()) {
            Messenger.msg(sender, ChatColor.ITALIC + "- " + portal.toString());
        }
        Messenger.msg(sender, Message.LINE);

        return true;
    }

}

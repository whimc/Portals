package edu.whimc.portals.commands.portal;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

public class PortalList extends AbstractSubCommand {

    public PortalList(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Lists all portals");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
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

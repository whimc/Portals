package edu.whimc.portals.commands.destination;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

public class DestinationList extends AbstractSubCommand {

    public DestinationList(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Lists all destinations");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        if (Destination.getDestinations().size() == 0) {
            Messenger.msg(sender, Message.NO_DESTINATIONS);
            return true;
        }

        Messenger.msg(sender, Message.LINE_DESTINATION_LIST);
        for (Destination dest : Destination.getDestinations()) {
            Messenger.msg(sender, ChatColor.ITALIC + "- " + dest.toString());
        }
        Messenger.msg(sender, Message.LINE);

        return true;
    }

}

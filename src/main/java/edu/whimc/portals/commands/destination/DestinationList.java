package edu.whimc.portals.commands.destination;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * Provides a user with a list of all registered {@link Destination}s.
 *
 * @see DestinationCommand
 */
public final class DestinationList extends AbstractSubCommand {

    public DestinationList(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Lists all destinations");
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
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

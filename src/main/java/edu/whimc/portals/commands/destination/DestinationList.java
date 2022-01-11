package edu.whimc.portals.commands.destination;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;

/**
 * The command for displaying a list of all the existing destinations.
 * Command: "/destination list"
 */
public class DestinationList extends AbstractSubCommand {

    /**
     * Constructs a DestinationList.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public DestinationList(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Lists all destinations");
    }

    /**
     * {@inheritDoc}
     *
     * Displays a list of all of the existing destinations in the chat / server log.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // notify if no existing destinations and abort command execution
        if (Destination.getDestinations().size() == 0) {
            Messenger.msg(sender, Message.NO_DESTINATIONS);
            return true;
        }

        // display list of all existing destinations
        Messenger.msg(sender, Message.LINE_DESTINATION_LIST);
        for (Destination dest : Destination.getDestinations()) {
            Messenger.msg(sender, ChatColor.ITALIC + "- " + dest.toString());
        }
        Messenger.msg(sender, Message.LINE);

        return true;
    }

}

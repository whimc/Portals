package edu.whimc.portals.commands.destination;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.Message;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

public class DestinationCreate extends AbstractSubCommand {

    public DestinationCreate(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Creates a new destination at your current location");
        super.arguments("name");
        super.requiresPlayer();
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(Destination.NONE)) {
            Messenger.msg(sender, Message.NONE_RESERVED_WORD);
            return true;
        }

        Destination dest = Destination.getDestination(args[0]);
        if (dest != null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_ALREADY_EXISTS, dest.getName());
            return true;
        }

        Destination.createDestination(plugin, args[0], ((Player) sender).getLocation());
        Messenger.msg(sender, ReplaceMessage.DESTINATION_CREATE_SUCCESS, args[0]);
        return true;
    }

}

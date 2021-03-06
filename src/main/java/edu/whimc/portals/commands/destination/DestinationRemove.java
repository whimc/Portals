package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

public class DestinationRemove extends AbstractSubCommand {

    public DestinationRemove(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Removes a destination");
        super.arguments("destination");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        Destination dest = Destination.getDestination(args[0]);
        if (dest == null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[0]);
            return true;
        }

        dest.remove();
        Messenger.msg(sender, ReplaceMessage.DESTINATION_REMOVE_SUCCESS, dest.getName());
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Destination.getTabCompletedDestinations(args[0]);
    }

}

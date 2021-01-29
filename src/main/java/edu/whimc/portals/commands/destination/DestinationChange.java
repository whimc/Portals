package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allow a user to change the location of a {@link Destination}
 * to ones current position while in the game.
 *
 * @see DestinationCommand
 */
public final class DestinationChange extends AbstractSubCommand {

    public DestinationChange(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.setDescription("Sets the location of a destination to your current position");
        super.provideArguments("destination");
        super.setRequiresPlayer(true);
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        Destination dest = Destination.getDestination(args[0]);
        if (dest == null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[0]);
            return true;
        }

        dest.setLocation(((Player) sender).getLocation());
        Messenger.msg(sender, ReplaceMessage.DESTINATION_CHANGE_SUCCESS, dest.getName());
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Destination.getTabCompletedDestinations(args[0]);
    }

}

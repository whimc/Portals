package edu.whimc.portals.commands.destination;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import edu.whimc.portals.Destination;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allow a player to teleport directly to a {@link Destination}.
 *
 * @see DestinationCommand
 */
public final class DestinationTeleport extends AbstractSubCommand {

    public DestinationTeleport(String baseCommand, String subCommand) {
        super(baseCommand, subCommand);
        super.setDescription("Teleports you to the given destination");
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

        if (!dest.isValid()) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_INVALID, dest.getName());
            return true;
        }

        dest.teleport((Player) sender);
        Messenger.msg(sender, ReplaceMessage.DESTINATION_TELEPORTED, dest.getName());
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Destination.getTabCompletedDestinations(args[0]);
    }

}

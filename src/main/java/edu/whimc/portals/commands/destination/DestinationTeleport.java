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
 * The command for teleporting the user to the provided destination.
 * Command: "/destination teleport"
 */
public class DestinationTeleport extends AbstractSubCommand {

    /**
     * Constructs a DestinationTeleport.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public DestinationTeleport(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Teleports you to the given destination");
        super.arguments("destination");
        super.requiresPlayer();
    }

    /**
     * {@inheritDoc}
     *
     * Teleports the user to the provided destination if it is valid.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        // get the provided destination
        Destination dest = Destination.getDestination(args[0]);

        // notify if destination does not exist and abort command execution
        if (dest == null) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_DOES_NOT_EXIST, args[0]);
            return true;
        }

        // notify if destination is not valid and abort command execution
        if (!dest.isValid()) {
            Messenger.msg(sender, ReplaceMessage.DESTINATION_INVALID, dest.getName());
            return true;
        }

        // teleport player to destination
        dest.teleport((Player) sender);
        Messenger.msg(sender, ReplaceMessage.DESTINATION_TELEPORTED, dest.getName());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Destination.getTabCompletedDestinations(args[0]);
    }

}

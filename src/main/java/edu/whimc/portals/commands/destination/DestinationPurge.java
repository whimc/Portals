package edu.whimc.portals.commands.destination;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * The command for purging any unused and / or invalid destinations.
 * Command: "/destination purge 'invalid'|'no-portals'|'both'"
 */
public class DestinationPurge extends AbstractSubCommand {

    /**
     * Constructs a DestinationPurge.
     *
     * @param plugin the instance of the plugin.
     * @param baseCommand the base command keyword.
     * @param subCommand the sub command keyword.
     */
    public DestinationPurge(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.description("Purge unused destinations");
        super.arguments("'invalid'|'no-portals'|'both'");
    }

    /**
     * {@inheritDoc}
     *
     * Deletes any unused and / or invalid destinations.
     */
    @Override
    protected boolean onCommand(CommandSender sender, String[] args) {
        String action = args[0];
        List<Destination> targets;
        ReplaceMessage msg;

        // filters targets by invalid destinations
        if (action.equalsIgnoreCase("invalid")) {
            msg = ReplaceMessage.DESTINATION_PURGED_INVALID;
            targets = Destination.getDestinations().stream()
                    .filter(v -> !v.isValid())
                    .collect(Collectors.toList());
        }

        // filters targets by unused destinations
        else if (action.equalsIgnoreCase("no-portals")) {
            msg = ReplaceMessage.DESTINATION_PURGED_NO_PORTALS;
            targets = Destination.getDestinations().stream()
                    .filter(v -> v.getLinkedPortals().isEmpty())
                    .collect(Collectors.toList());
        }

        // sets targets as all unused and invalid destinations
        else if (action.equalsIgnoreCase("both")) {
            msg = ReplaceMessage.DESTINATION_PURGED_BOTH;
            targets = Destination.getDestinations().stream()
                    .filter(v -> !v.isValid() || v.getLinkedPortals().isEmpty())
                    .collect(Collectors.toList());
        }

        // send the user the command's usage and abort command execution
        else {
            Messenger.usageUnknownArg(sender, action, super.getDescription(), super.getUsage());
            return true;
        }

        // delete the target destinations
        targets.stream().forEachOrdered(Destination::remove);
        Messenger.msg(sender, msg, Integer.toString(targets.size()));
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Arrays.asList("invalid", "no-portals", "both").stream()
                .filter(v -> v.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }

}

package edu.whimc.portals.commands.destination;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Destination;
import edu.whimc.portals.Main;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allows a user to delete all {@link Destination}s which are not currently
 * used by {@link edu.whimc.portals.Portal}s.
 *
 * @see DestinationCommand
 */
public final class DestinationPurge extends AbstractSubCommand {

    public DestinationPurge(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.setDescription("Purge unused destinations");
        super.provideArguments("'invalid'|'no-portals'|'both'");
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        String action = args[0];
        List<Destination> targets;
        ReplaceMessage msg;
        if (action.equalsIgnoreCase("invalid")) {
            msg = ReplaceMessage.DESTINATION_PURGED_INVALID;
            targets = Destination.getDestinations().stream()
                    .filter(v -> !v.isValid())
                    .collect(Collectors.toList());
        } else if (action.equalsIgnoreCase("no-portals")) {
            msg = ReplaceMessage.DESTINATION_PURGED_NO_PORTALS;
            targets = Destination.getDestinations().stream()
                    .filter(v -> v.getLinkedPortals().isEmpty())
                    .collect(Collectors.toList());
        } else if (action.equalsIgnoreCase("both")) {
            msg = ReplaceMessage.DESTINATION_PURGED_BOTH;
            targets = Destination.getDestinations().stream()
                    .filter(v -> !v.isValid() || v.getLinkedPortals().isEmpty())
                    .collect(Collectors.toList());
        } else {
            Messenger.usageUnknownArg(sender, action, super.getDescription(), super.getUsage());
            return true;
        }

        targets.forEach(Destination::remove);
        Messenger.msg(sender, msg, Integer.toString(targets.size()));
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Stream.of("invalid", "no-portals", "both")
                .filter(v -> v.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }

}

package edu.whimc.portals.commands.portal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;

import edu.whimc.portals.Main;
import edu.whimc.portals.Portal;
import edu.whimc.portals.commands.AbstractSubCommand;
import edu.whimc.portals.utils.Messenger;
import edu.whimc.portals.utils.Messenger.ReplaceMessage;

/**
 * Allow the user to delete all unnecessary {@link Portal}s.
 *
 * @see PortalCommand
 */
public final class PortalPurge extends AbstractSubCommand {

    public PortalPurge(Main plugin, String baseCommand, String subCommand) {
        super(plugin, baseCommand, subCommand);
        super.setDescription("Purge unused portals");
        super.provideArguments("'invalid'|'no-destination'|'both'");
    }

    @Override
    protected final boolean onCommand(CommandSender sender, String[] args) {
        String action = args[0];
        List<Portal> targets;
        ReplaceMessage msg;
        if (action.equalsIgnoreCase("invalid")) {
            msg = ReplaceMessage.PORTAL_PURGED_INVALID;
            targets = Portal.getPortals().stream()
                    .filter(v -> !v.isValid())
                    .collect(Collectors.toList());
        } else if (action.equalsIgnoreCase("no-destination")) {
            msg = ReplaceMessage.PORTAL_PURGED_NO_DESTINATION;
            targets = Portal.getPortals().stream()
                    .filter(v -> v.getDestination() == null)
                    .collect(Collectors.toList());
        } else if (action.equalsIgnoreCase("both")) {
            msg = ReplaceMessage.PORTAL_PURGED_BOTH;
            targets = Portal.getPortals().stream()
                    .filter(v -> !v.isValid() || v.getDestination() == null)
                    .collect(Collectors.toList());
        } else {
            Messenger.usageUnknownArg(sender, action, super.getDescription(), super.getUsage());
            return true;
        }

        targets.forEach(Portal::remove);
        Messenger.msg(sender, msg, Integer.toString(targets.size()));
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        return Stream.of("invalid", "no-destination", "both")
                .filter(v -> v.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }

}
